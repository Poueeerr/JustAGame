package com.FsStudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.FsStudios.entities.Enemy;
import com.FsStudios.world.World;



public class Menu {

	public String[] options = {"New Game", "Load Game","Quit Game", "Save Game" };
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up,down, enter;
	
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	public Font newfont;
	
	public Menu() {
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(45f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		File file = new File("Save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;	
			//Sound.menuEffect.Play();
			currentOption--;
		}if(currentOption < 0) {
			currentOption = maxOption;
		}
		if(down) {
			down = false;
			//Sound.menuEffect.Play();
			currentOption++;
		}if(currentOption > maxOption) {
			currentOption = 0;
		}if(enter) {
			enter = false;
			Sound.music.loop();
			
			if(options[currentOption] == "New Game" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("Save.txt");
				file.delete();
			}else if(options[currentOption] == "Quit Game") {
				System.exit(1);
			}else if(options[currentOption] == "Load Game") {
			file = new File("Save.txt");
			if(file.exists()) {
				String saver = loadGame(10);
				applySave(saver);
			}
			if(pause == true) {
				 String NewWorld = "level"+Game.CUR_LEVEL+".png";
		    	   World.restartGame(NewWorld);
		    	   Game.gameState = "NORMAL";
			}
				
			} 
				
			
			else if(options[currentOption] == "Save Game"){
				if(Game.CUR_LEVEL > 1 ) {	
				saveGame = true;
					if(saveGame) {
						saveGame = false;
						String[] opt1 = {"level","currentLife", "score", "Ammo"};
						int[] opt2 = {Game.CUR_LEVEL,(int) Game.life,(int)Enemy.point, Game.ammo };
						Menu.saveGame(opt1,opt2,10);
						System.out.println("Jogo salvo");
					}
			}}
		}

	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i =0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			    case "level":
			    	Game.CUR_LEVEL = Integer.parseInt(spl2[1]);
				World.restartGame("level" + spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			    case "currentLife":{
                 Game.life = Integer.parseInt(spl2[1]);
			    	break;
			}
			    case "score":{
			    	Enemy.point = Integer.parseInt(spl2[1]);
			    	break;
			}case "Ammo":{
                Game.ammo = Integer.parseInt(spl2[1]);
			    	break;
			}
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = ("");
		File file = new File("Save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader 	reader = new BufferedReader(new FileReader("Save.txt"));
				try {
					while((singleLine = reader.readLine())!= null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
					    trans[1] = "";
					    for(int i = 0; i < val.length; i++) {
					    	val[i]-= encode;
					    	trans[1]+= val[i];
					    }
					    line+=trans[0];
					    line+=":";
					    line+=trans[1];
					    line+="/";
					    
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {
				
			}
		}
		return line;
	}
	
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("Save.txt") );
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < val1.length; i++)
		{
			String current =val1[i];
			current+=":";
			char[]  value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try{
				write.write(current);
				if(i < val1.length - 1)
                    write.newLine();					
			}catch(IOException e) {
				
			}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {
		}
	}
	
	public void render(Graphics g) {
		
		
		
		Graphics2D g2 = (Graphics2D) g; 
		g2.setColor(new Color (0,0,0,200));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE );
		
		if(Game.CUR_LEVEL == 1) {
		//Weapon
		
				g.setColor(new Color(0xffa500));
				g.fillRect(560, 200, 10, 10);
				g.drawString("Weapon", 580, 210);
				//Player
				g.setColor(new Color(0xffff00));
				g.fillRect(560, 220, 10, 10);
				g.drawString("Player", 580, 230);
				//mana
				g.setColor(new Color(0xa6dced));
				g.fillRect(560, 240, 10, 10);
				g.drawString("Ammo / Mana", 580, 250);
				//LifePack
				g.setColor(new Color(0xffc0cb));
				g.fillRect(560, 260, 10, 10);
				g.drawString("Heal Potion", 580, 270);
				//Enemy
				g.setColor(new Color(0xc40233));
				g.fillRect(560, 280, 10, 10);
				g.drawString("Enemy", 580, 290);
				
		}else if(Game.CUR_LEVEL >= 2) {
			//Weapon
			
			g.setColor(new Color(0xffa500));
			g.fillRect(560, 220, 10, 10);
			g.drawString("Weapon", 580, 230);
			//Player
			g.setColor(new Color(0xffff00));
			g.fillRect(560, 240, 10, 10);
			g.drawString("Player", 580, 250);
			//mana
			g.setColor(new Color(0xa6dced));
			g.fillRect(560, 260, 10, 10);
			g.drawString("Ammo / Mana", 580, 270);
			//LifePack
			g.setColor(new Color(0xffc0cb));
			g.fillRect(560, 280, 10, 10);
			g.drawString("Heal Potion", 580, 290);
			//Enemy
			g.setColor(new Color(0xc40233));
			g.fillRect(560, 300, 10, 10);
			g.drawString("Enemy", 580, 310);
			
	}
		g.setColor(Color.white);
		g.fillRect(10,50,245,40);
		g.setColor(Color.BLACK);
		g.setFont(new Font("arial",Font.BOLD,36));
		g.drawString("|Just a Game|", (Game.WIDTH*Game.SCALE) - 705, (Game.HEIGHT*Game.SCALE) /2 - 160);
		
		
		
		//Opções do menu

		
		g.setColor(Color.WHITE);
		g.setFont(newfont);
		if(pause == false)
		g.drawString("New Game", (Game.WIDTH*Game.SCALE) - 680 , 160);
		else
			g.drawString("Resume", (Game.WIDTH*Game.SCALE) - 680 , 160);
		if(pause == false)
			g.drawString("Load Game", (Game.WIDTH*Game.SCALE) - 680 , 210);
		else
			g.drawString("Restart Level", (Game.WIDTH*Game.SCALE) - 680 , 210);
		if(pause == true) 
			g.drawString("Save Game", (Game.WIDTH*Game.SCALE) - 680 , 310);
		
		g.drawString("Quit Game", (Game.WIDTH*Game.SCALE) - 680, 260);
		if(pause == false) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString("How to play: Move on W,S,A,D and shoot at mouse", (Game.WIDTH*Game.SCALE) - 705 , 460);
		g.setFont(new Font("arial",Font.BOLD,15));
		g.drawString("Made by Poueeerr", (Game.WIDTH*Game.SCALE) - 160 , 20);
		}
		if(options [currentOption] == "New Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) - 705, 160);
		}else if(options[currentOption] == "Load Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) - 705, 210);
		}else if(options[currentOption] == "Quit Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) - 705, 260);
		}else  if(pause == true) {
			if(options[currentOption] == "Save Game") {
				g.drawString(">", (Game.WIDTH*Game.SCALE) - 705, 310);
		}
	}
	}
}
