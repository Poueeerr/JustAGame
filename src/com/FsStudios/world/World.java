package com.FsStudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.FsStudios.entities.Bullet;
import com.FsStudios.entities.Enemy;
import com.FsStudios.entities.Entity;
import com.FsStudios.entities.Flor;
import com.FsStudios.entities.LifePack;
import com.FsStudios.entities.NPC;
import com.FsStudios.entities.Particle;
import com.FsStudios.entities.Player;
import com.FsStudios.entities.Torch;
import com.FsStudios.entities.Weapon;
import com.FsStudios.graficos.Spritesheet;
import com.FsStudios.main.Game;


public class World {
	
	public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	public World(String path) {
	
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
		    int pixels[] = new int[map.getWidth() * map.getHeight()]; 
		    WIDTH = map.getWidth();
		    HEIGHT = map.getHeight();
		    
		    tiles = new Tile[map.getWidth() * map.getHeight()];
		    map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
		    for(int xx = 0; xx < map.getWidth(); xx++) {
		    	for(int yy = 0; yy < map.getHeight(); yy++) {
		    		int pixelAtual = pixels[xx + (yy*map.getWidth())];
		    		tiles[xx + (yy * map.getWidth())] = new FloorTile(xx*16 , yy*16, Tile.TILE_FLOOR);
		    		if(pixelAtual == 0xFF000000) {
		    			//Floor
		    			tiles[xx + (yy * map.getWidth())] = new FloorTile(xx*16 , yy*16, Tile.TILE_FLOOR);
		    		
		    		}else if(pixelAtual == 0xFFFFFFFF) {
		    			//wall
		    			tiles[xx + (yy * map.getWidth())] = new WallTile(xx*16 , yy*16, Tile.TILE_WALL);
		    		
		    		}else if(pixelAtual == 0xFF0000FF) {
		    			//player
		    			Game.player.setX(xx*16);
		    			Game.player.setY(yy*16);
		    			
		    		}else if(pixelAtual == 0xFFFF0000) {
		    			//enemy
		    			Enemy en = new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_ENT);
		    			Game.entities.add(en);
		    			Game.enemies.add(en);
		    			
		    		}else if(pixelAtual == 0xFFFF006D) {
		    			//Tocha
		    			Torch torch = new Torch(xx*16, yy*16, 16, 16, Entity.TORCH);	
		    			Game.entities.add(torch);
		    		}else if(pixelAtual == 0xFFFF6A00) {
		    			//Weapon
		    			Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_ENT));
		    			
		    		}else if(pixelAtual == 0xFFFF8989) {
		    			//LifePack
		    			LifePack pack = new LifePack(xx*16,yy*16,16,16,Entity.LIFEPACK_ENT);
						Game.entities.add(pack);
		    		}else if(pixelAtual == 0xFFFFD800) {
		    			//Bullet
		    			Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_ENT));
		    		}else if(pixelAtual == 0xFF4CFF00) {
		    			tiles[xx + (yy * map.getWidth())] = new FloorTile(xx*16,yy*16,Entity.FLOR);
		    		}else if(pixelAtual == 0xFF878685) {
		    			tiles[xx + (yy * map.getWidth())] = new FloorTile(xx*16,yy*16,Entity.TEIA);
		    		}
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void restartGame(String level) 
	{
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16, Game.spritesheet.getSprite(32, 0,16,16));
		Game.entities.add(Game.player);
		Game.npc = new NPC(32,32,16,16, Game.spritesheet.getSprite(0, 64, 16, 16));
		Game.entities.add(Game.npc);
		Game.world = new World("/"+level); 
		Game.minimapa = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Game.minimapaPixels = ((DataBufferInt)Game.minimapa.getRaster().getDataBuffer()).getData();
		return;
	}
	
	public static void generateParticles(int amount,int x,int y) {
		for(int i=0;i < amount; i++) {
			Game.entities.add(new Particle(x, y, 1, 1, null));
			
		}
	}
	
	
	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height ) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
	 
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE ;
		int y3 = (ynext+height-1) / TILE_SIZE;
		
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;
		
		return !((tiles [x1 + (y1*World.WIDTH)] instanceof WallTile ||
				tiles [x2 + (y2*World.WIDTH)] instanceof WallTile ||
				tiles [x3 + (y3*World.WIDTH)] instanceof WallTile ||
				tiles [x4 + (y4*World.WIDTH)] instanceof WallTile)); 
	
		}
	
	
	public static boolean isFree(int xnext, int ynext ) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
	 
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE ;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles [x1 + (y1*World.WIDTH)] instanceof WallTile ||
				tiles [x2 + (y2*World.WIDTH)] instanceof WallTile ||
				tiles [x3 + (y3*World.WIDTH)] instanceof WallTile ||
				tiles [x4 + (y4*World.WIDTH)] instanceof WallTile)); 
	
		}
	
	
	public void render(Graphics g){
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	public static void renderMinimap() {
		for(int i = 0; i < Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;
		}
		for(int xx = 0; xx < World.WIDTH; xx++) {
			for(int yy = 0; yy < World.HEIGHT; yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile) {
					Game.minimapaPixels[xx + (yy*WIDTH)] = 0xFFFFFF;
					
				}
			}
		}
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(Game.entities.get(i) instanceof Enemy) {
				int xEnemy = e.getX()/16;
				int yEnemy = e.getY()/16;
				Game.minimapaPixels[xEnemy + (yEnemy*WIDTH)] = 0xc40233;
			}
		}
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(Game.entities.get(i) instanceof Bullet) {
				int xAmmo = e.getX()/16;
				int yAmmo = e.getY()/16;
				Game.minimapaPixels[xAmmo+ (yAmmo*WIDTH)] = 0xa6dced;
			}
		}
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(Game.entities.get(i) instanceof LifePack) {
				int lifex = e.getX()/16;
				int lifey = e.getY()/16;
				Game.minimapaPixels[lifex+ (lifey*WIDTH)] = 0xffc0cb;
			}
		}
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(Game.entities.get(i) instanceof Weapon) {
				int gunx = e.getX()/16;
				int guny = e.getY()/16;
				Game.minimapaPixels[gunx+ (guny*WIDTH)] = 0xffa500;
			}
		}
		
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;
		Game.minimapaPixels[xPlayer + (yPlayer*WIDTH)] = 0xffff00;
	
	}
}
