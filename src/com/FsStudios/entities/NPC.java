package com.FsStudios.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.FsStudios.main.Game;
import com.FsStudios.main.Pixel;


public class NPC extends Entity{	

	public String[] frasesNpc = new String[2];
	
	public boolean showMessage = false;
	public boolean show = false;
	
	public int curIndexMsg = 0;
	public int fraseIndex = 0;
	
	public int time = 0;
	public int maxTime = 5;
	
	public NPC(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		depth = 2;
		
		frasesNpc[0] = "Help me, please someone!!";
		frasesNpc[1] = "Save me my hero!!";
	}
	public void tick() {
	
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer-xNpc) < 20 &&
				Math.abs(yPlayer-yNpc) < 20) {
			
				if(show == false) {
					showMessage = true;
					show = true;
				}
			
		}else {
			
		}
		
		
		this.time++;
		if(showMessage ) {
			if(this.time >= maxTime) {
				this.time = 0;
				if(curIndexMsg < frasesNpc[fraseIndex].length() - 1) {
					curIndexMsg++;
				}else {
					
					if(fraseIndex < frasesNpc.length - 1) {
						fraseIndex++;
						curIndexMsg =0;
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		super.render(g);
		if(showMessage) {
			
			g.setColor(new Color(255,255,255,100));
			g.fillRect(0, 128, 300, 2);
			
			g.setColor(new Color(0,0,0,100));
			g.fillRect(0, 130, Game.WIDTH, Game.HEIGHT/5);
			
			
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.setColor(Color.WHITE);
			g.drawString(frasesNpc[fraseIndex].substring(0,curIndexMsg), (int)x + 10, (int) y + 110);
			
			
			g.drawString(">", (int)x + 180, (int) y + 120);
		}
	}
}
