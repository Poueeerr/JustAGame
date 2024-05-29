package com.FsStudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.FsStudios.entities.Player;
import com.FsStudios.main.Game;

public class UI {

	private BufferedImage lifeUi; //, life2
	
	public void render(Graphics g) {
		
		lifeUi = Game.spritesheet.getSprite(0,48,16,16);
		//life2 = Game.spritesheet.getSprite(0,64,70,16);
		
		g.drawImage(lifeUi, (int)5.75, -20, 75, 50, null);
		g.drawImage(lifeUi, (int)5.75, -9, 35, 50, null);
		g.drawImage(lifeUi, (int)203, -20, 39, 50, null);
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(8, 4, 70, 8);
		//g.fillRect(8, 4, (int)((Game.player.life/Game.player.maxLife)*70), 8);
		//g.drawImage(life2, 8,1,(int)((Game.player.life/Game.player.maxLife)*70), 18, null );
		if(Game.life >= 61) {
			g.setColor(Color.GREEN);
			g.fillRect(8, 4, (int)((Game.life/Game.player.maxLife)*70), 8);
		}else if(Game.life >= 31) {
			g.setColor(Color.YELLOW);
			g.fillRect(8, 4, (int)((Game.life/Game.player.maxLife)*70), 8);
		}else if(Game.life <= 30) {
			g.setColor(Color.RED);
			g.fillRect(8, 4, (int)((Game.life/Game.player.maxLife)*70), 8);
		}
		
		g.setColor(Color.YELLOW);
		g.fillRect(8, 15, 30, 8);
		
		
		g.setColor(Color.WHITE);
		g.fillRect(205, 4, 34,8);
		
	}
	
}
