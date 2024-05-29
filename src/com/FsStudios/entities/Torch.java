package com.FsStudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.FsStudios.main.Game;
import com.FsStudios.world.Camera;

public class Torch extends Entity{

	private int frames = 0,maxFrames = 20,index = 0,maxIndex = 1;
	private BufferedImage[] sprites;
	
	public Torch(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		sprites = new BufferedImage[3];
		
		
		sprites[0] = Game.spritesheet.getSprite(128, 32, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 48, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(128, 64, 16, 16);
	}public void tick() {
    	depth = -1;

		 frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
				
			}
	}
	 public void render(Graphics g) {
	    	g.drawImage(sprites[index], this.getX() - Camera.x,this.getY() - Camera.y,null );
	 }
}
