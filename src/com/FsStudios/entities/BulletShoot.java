package com.FsStudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.FsStudios.main.Game;
import com.FsStudios.world.Camera;
import com.FsStudios.world.World;

public class BulletShoot extends Entity{

	private double dx;
	private double dy;
	private double spd = 6;
	
	private BufferedImage bulletS;
	
	private int life = 50, curLife = 0;
	
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
		
		bulletS = Game.spritesheet.getSprite(16, 32, 16, 16);
	}
	
	
	public void tick() {
		if(World.isFreeDynamic((int)(x+(dx*spd)), (int)(y+(dy*spd)),4,4)) {
		x+=dx * spd;
		y+=dy * spd;
		}else {
			Game.bullets.remove(this);
			World.generateParticles(20, (int)x, (int)y);
			return;
		}
		curLife++;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
			}
		}
	
	
	public void render(Graphics g) {
		g.drawImage(bulletS,this.getX() - Camera.x ,this.getY() - Camera.y, width*2, height*2, null);
	}
	
}
