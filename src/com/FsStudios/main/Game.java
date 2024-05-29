package com.FsStudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.image.DataBufferInt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.FsStudios.entities.BulletShoot;
import com.FsStudios.entities.Enemy;
import com.FsStudios.entities.Entity;
import com.FsStudios.entities.NPC;
import com.FsStudios.entities.Player;
import com.FsStudios.graficos.Spritesheet;
import com.FsStudios.graficos.UI;
import com.FsStudios.world.Camera;
import com.FsStudios.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	public static int CUR_LEVEL = 1;
	private int MAX_LEVEL = 4;
	private BufferedImage image;
	
	public boolean minimapaON = false;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	public static NPC npc;
	
	public static Random rand;
	
	public UI ui;
	public static double life = 99;
	public static int ammo = 0, minAmmo = 0;
	
	//public int xx, yy;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	public Font newfont;
	
	public static String gameState = "MENU";
	
	
	//CutsceneStart
	public static int cutscene = 1;
	public static int entrada = 1;
	public static int começar = 2;
	public static int jogando = 3;
	public static int estado_cena = entrada;
	//Cutscene end
	public static int timeCena = 0;
	public static int maxTimeCena = 60*3;
	
	private boolean showMassageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;
	
	public static int[] pixels;
	public BufferedImage lightmap;
	public static int[] lightMapPixels;
	public static int[] minimapaPixels;
	
	public static BufferedImage minimapa;
	
	public boolean saveGame = false;
	
	public int mx,my;
	
	public Game() {
		
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		//Inicializando objetos º-º alguém me salva
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); 
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth()*lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		
		npc = new NPC(32,32,16,16, spritesheet.getSprite(0, 64, 16, 16));
		
	    entities.add(player);
	    entities.add(npc);

		world = new World("/level1.png");
		minimapa = new BufferedImage(World.WIDTH,World.HEIGHT,BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
	    
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menu = new Menu();

	}
	public void initFrame() {
		frame = new JFrame("Just a Game");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//Icone
		Image imagem = null;
		try {
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		
		Image cursor = toolkit.getImage(getClass().getResource("/cursor.png"));
		Cursor c = toolkit.createCustomCursor(cursor, new Point(16,16),"img");
		frame.setCursor(c);
		frame.setIconImage(imagem);
		
	}

    public synchronized void start() {
    	thread = new Thread(this);
    	thread.start();
    	isRunning = true;
    } 
    public synchronized void stop() {
    	isRunning = false;
    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
            Game game = new Game();
            game.start();
       }
	public void tick() {
		if(gameState == "NORMAL") {
			//xx++; bagui da manipulação de pixels ._.
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level","currentLife", "score", "Ammo"};
				int[] opt2 = {Game.CUR_LEVEL,(int) life, Enemy.point, ammo};
				Menu.saveGame(opt1,opt2,10);
			}
			
			this.restartGame = false;
			//if(Game.estado_cena == Game.jogando) {
				for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
       /*}else {
    	   if(Game.estado_cena == Game.entrada && cutscene == 1) {
    		   if(player.getX() < 200 ) {
    			   		player.updateCamera();
    				   player.x++;	
    			  }else {
    				  Game.estado_cena = Game.começar;	
    			  }
    		   }else if( Game.estado_cena == Game.começar) {
    			timeCena++;
    			if(timeCena >= maxTimeCena) {
    				CUR_LEVEL++;
    				   String NewWorld = "level"+CUR_LEVEL+".png";
    		    	   World.restartGame(NewWorld);
    				Game.estado_cena = Game.jogando;
    			}
    		}
    		*/
    	   //Implementar Cutscence
    	   		/*if(Game.CUR_LEVEL == LEVEL AQUI) {
    	   		if(Game.estado_cena == Game.entrada && cutscene == 2) {
    	   			if(player.getX() < DISTANCIA AQUI) {
     				   player.x++; OU Y++	
     			  }else {
     				  Game.estado_cena = Game.começar;	
     			  }
     		   }else if( Game.estado_cena == Game.começar) {
     			timeCena++;
     			if(timeCena >= maxTimeCena) {
     				Game.estado_cena = Game.jogando;
     			}
     		   	}
    	   		}
    	   		}
    	   		*/
    	   
       
       
       for(int i = 0; i < bullets.size(); i++) {
    	   bullets.get(i).tick();
       }
       
       if(enemies.size() == 0) {
    	   //Next lvl
    	   CUR_LEVEL++;
    	   if(CUR_LEVEL > MAX_LEVEL) {
    		   CUR_LEVEL = 1;
    	   }
    	   String NewWorld = "level"+CUR_LEVEL+".png";
    	   World.restartGame(NewWorld);
       }
       
	}else if(gameState == "GAME_OVER") {
		
         this.framesGameOver++;
         if(this.framesGameOver == 30) {
        	 this.framesGameOver = 0;
        	 if(this.showMassageGameOver)
        		 this.showMassageGameOver = false;
        		 else
        			 this.showMassageGameOver = true;        	 
         }
         if(restartGame) {
        	 if(gameState == "GAME_OVER") {
        		 life = player.maxLife;
        		 ammo = minAmmo;
        	 }else {
        		 
        	 }
        		 
        	Enemy.point = 0;
        	this.restartGame = false;
        	this.gameState = "NORMAL";
        	
        	  String NewWorld = "level"+CUR_LEVEL+".png";
       	      World.restartGame(NewWorld);
       	      
            }
	}else if(gameState == "MENU"){
		player.updateCamera();
		menu.tick();
		
	 }
	}
	/*
	public void drawRectangeExample(int xoff, int yoff) {
	  for(int xx = 0; xx < 32; xx++) {
		  for(int yy = 0; yy < 32; yy++) {
			  int xOff = xx + xoff;
			  int yOff = yy + yoff;
			  if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)
				  continue;
			  pixels[xOff + (yOff*WIDTH)] = 0xff0000;
		  }
	  }
	}
    */
	
	public void  applyLight() {
		for (int xx=0; xx < Game.WIDTH;xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightMapPixels[xx + (yy * Game.WIDTH)] == 0xffffffff) {
					if(player.isDamaged == false) {
					int pixel = Pixel.getLightBlend(pixels[xx +yy * WIDTH], 0x808080, 0);
					pixels[xx +yy * WIDTH] = pixel;
				}else {
					int pixel = Pixel.getLightBlend(pixels[xx +yy * WIDTH], 0xFF0000, 0);
					pixels[xx +yy * WIDTH] = pixel;
				}
				}
				}
			}
		}
	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
	    world.render(g);
	    Collections.sort(entities,Entity.nodeSorter);
	    
		for(int i = 0; i < entities.size(); i++) {
		   	   Entity e = entities.get(i);
		   	   e.render(g);	
		}
		 for(int i = 0; i < bullets.size(); i++) {
	    	   bullets.get(i).render(g);
	       }
		 
		applyLight();
		ui.render(g);
		/**/
		g.dispose();
		g = bs.getDrawGraphics();
		//drawRectangeExample(xx,yy);
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		
		g.setColor(Color.BLACK);
		g.setFont(newfont);
		g.drawString("Score: "+ (int)Enemy.point,630,30);
		
		g.setColor(Color.BLACK);
		g.setFont(newfont);
		g.drawString("HP: "+(int)life+"/"+(int)Game.player.maxLife,28,30);
		
		g.setFont(newfont);
		g.setColor(Color.BLACK);
		g.drawString("Mana: " + ammo, 28,65);
		
		
		
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0,WIDTH*SCALE, HEIGHT*SCALE);
			
			g.setFont(new Font("arial",Font.BOLD,35));
			g.setColor(Color.WHITE);
			g.drawString("GAME OVER", (WIDTH*SCALE)/2 - 90,(HEIGHT*SCALE)/2 + 10);
			
			g.setFont(new Font("arial",Font.BOLD,20));
			g.setColor(Color.WHITE);
			if(showMassageGameOver) {
			
			g.drawString(">PRESS ENTER TO RESTART<", (WIDTH*SCALE)/2 - 125,(HEIGHT*SCALE)/2 + 60);
		}
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		if(minimapaON == false) {
			World.renderMinimap();
			g.drawImage(minimapa, 560, 60,World.WIDTH*5,World.HEIGHT*5, null);
		}
		/*
		Graphics2D g2 = (Graphics2D) g;
		
		double angleMouse = Math.atan2(200+25-my, 200+25 - mx );
		g2.rotate(angleMouse, 200+25, 200+25);
		g.setColor(Color.red);
		g.fillRect(200, 200, 50, 50);
		*/
	
		if(Game.estado_cena == Game.começar) {
			g.setFont(new Font("arial",Font.BOLD,20));
			g.setColor(Color.WHITE);
			g.drawString("STARTING GAME", (WIDTH*SCALE)/2 - 80,(HEIGHT*SCALE)/2 + 60);	
			
		}
		
		bs.show();
		
		
		
	}
	
	public void run() {
		requestFocus();
	    long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        int frames = 0;
        double timer = System.currentTimeMillis();
        double delta = 0;
	    while(isRunning) {
              long now = System.nanoTime();
              delta+= (now - lastTime) / ns;
              lastTime = now;
              if (delta >= 1) {
            	  tick();
            	  render();
            	  frames++;
            	  delta--;
              }
              if(System.currentTimeMillis() - timer >= 1000 ) {
                     	 //System.out.println("FPS: " +frames);
                     	  frames = 0;
                     	  timer += 1000;
              }
	   }
	    stop();
	    
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			 npc.showMessage = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		 if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
        	player.up = true;
        	
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
        	player.down = true;
		}
		 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			 restartGame = true;
			 if(gameState == "MENU"){
				 menu.enter = true;
				
			 }
		 }
		 if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			 if(gameState == "NORMAL") {
				 
			 gameState = "MENU";
			 menu.pause = true;
		  }
		 }
		 
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		 if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
        	player.up = false;
        	
        	if(gameState == "MENU") {
        		menu.up = true;
        	}
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
        	player.down = false;
        	
        	if(gameState == "MENU") {
        		menu.down = true;
        	}
        	
		}
		 if(e.getKeyCode() == KeyEvent.VK_SPACE){
			 if(Game.gameState == "NORMAL")
			      this.saveGame = true;
		 }
		 
		 if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			 player.shoot = true;
		 }
		 
	}
	@Override
	public void mouseClicked(MouseEvent e) {	
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() /3); 
		player.my = (e.getY() /3); 
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//this.mx = e.getX();
		//this.my = e.getY();
	}

}