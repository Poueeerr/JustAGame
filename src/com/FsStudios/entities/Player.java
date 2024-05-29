package com.FsStudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.FsStudios.graficos.Spritesheet;
import com.FsStudios.main.Game;
import com.FsStudios.main.Sound;
import com.FsStudios.world.Camera;
import com.FsStudios.world.World;

public class Player extends Entity{

	public boolean right = false,up,left,down;
	public int right_dir = 0, left_dir = 1,up_dir = 2,down_dir = 3;
	public int dir = right_dir;
	public double speed = 1.5;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage playerDamage;
	
	
	private boolean arma = false;
	private int damageFrames = 0;	 
	
	public boolean shoot = false, mouseShoot = false;
	
 	
 	
 	public boolean isDamaged = false;
 	
 	public double maxLife = 99;
 	public int mx, my;
 	
 	
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private boolean moved  = false;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		
		
		 for(int i = 0; i < 4; i++) {
		rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		 }
		 for(int i = 0; i < 4; i++) {
				leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
				 }
		 for(int i = 0; i < 4; i++) {
				upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
				 }
		 for(int i = 0; i < 4; i++) {
				downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 32, 16, 16);
				 }
	}
	
	public void tick() {
		depth = 1;
		moved = false;
			
		
		
		if(right && World.isFree((int)(x+speed),this.getY())) 
		{
			
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY()))   
		{ 
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed)))  
		{ 
			moved = true;
			dir = up_dir;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed)))  
		{
			moved = true;
			dir = down_dir;
			y+=speed;
		}
        if(moved) {
        	frames++;
        	if(frames == maxFrames) {
        		frames = 0;
        		index++;
        		if(index > maxIndex) {
        			index = 0;
        		}
        	}
        	
        }
        
        checkCollisionLifePack();
        checkCollisionAmmo();       
        checkCollisionGun();        
        
        if(isDamaged) {
        	this.damageFrames++;
        	if(this.damageFrames == 8) {
        		this.damageFrames = 0;
        		isDamaged = false;	
        	}
        }
        
        if(shoot) {
        	//Create bullet and shoot 
        	shoot = false;
        	if(arma && Game.ammo > 0) {
        		Sound.shootEffect.play();
        	Game.ammo--;
        	int dy = 0;
        	int dx = 0;
        	int px = 0;
        	int py = 0;
        	
        	if(dir == right_dir) {
        		px = 16;
        		 dx = 1;
        	}else if(dir == left_dir){
        		px = -7;
        		 dx = -1;
        	}
        	else if(dir == down_dir){
        		py = 16;
        		 dy = 1;
        	}else if(dir == up_dir){
        		py = -7;
        		 dy = -1;
        	}
        	
        	
   
       		BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 4, 4, null, dx, dy);
       		
        	Game.bullets.add(bullet);
        	
        	}
        }
        if(mouseShoot) {
        	//Create bullet and shoot 
        	mouseShoot = false;
        	if(arma && Game.ammo > 0) {
        		Sound.shootEffect.play();
        	Game.ammo--;
        	
        	double angle = Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x));
  
        	double dx = Math.cos(angle);
        	double dy = Math.sin(angle);
        	int px = 6;
        	int py = 0;
        	
        	
       		BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 4, 4, null, dx, dy);
        	Game.bullets.add(bullet);
        	
        }
        }
        
        if(Game.life <= 0) {
        //GAME OVER
        	Game.life = 0;
        	Game.gameState = "GAME_OVER";
        }
        
        updateCamera();
        
	}
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
    	Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Weapon) {
				if(Entity.isColidding(this, e)) {
					Sound.lifeEffect.play();
					arma = true;
					
					//System.out.println("Ammo: " + ammo);
					Game.entities.remove(i);
					return;	
	           }			
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Bullet) {
				if(Entity.isColidding(this, e)) {
					Sound.lifeEffect.play();
					Game.ammo+=30;
					
					//System.out.println("Ammo: " + ammo);
					Game.entities.remove(i);
					return;	
	           }			
			}
		}
	}
	
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof LifePack) {
				if(Entity.isColidding(this, e)) {
					Sound.lifeEffect.play();
					Game.life+=8;
					if(Game.life >= 99)
						Game.life = 99;
					Game.entities.remove(i);
					return;	
	           }			
			}
		}
	}

	public void render(Graphics g) {
		
		if(!isDamaged) {
		
		if(dir == right_dir) {
		g.drawImage(rightPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y-z,null );
		if(arma) {
			//desenhar arma para direita
			g.drawImage(Entity.GUN_RIGHT, this.getX()+4 - Camera.x, this.getY() - Camera.y-z ,null);
		}
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y-z,null );
			if(arma) {
				//desenhar arma para esquerda
				g.drawImage(Entity.GUN_LEFT, this.getX()-4 - Camera.x, this.getY() - Camera.y-z ,null);
			}
		}
		if(dir == up_dir) {
			if(arma) {
				//desenhar arma para direita
				g.drawImage(Entity.GUN_UP, this.getX()+4 - Camera.x, this.getY() - Camera.y-z ,null);
			}
			g.drawImage(upPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y-z,null );
			
		}
		else if(dir == down_dir) {
		g.drawImage(downPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y-z,null );
		if(arma) {
			//desenhar arma para esquerda
			g.drawImage(Entity.GUN_DOWN, this.getX()+4 - Camera.x, this.getY() - Camera.y-z ,null);
		}
		  }
		
	   }else {
		   g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y-z, null);
		   if(arma) {
				if(dir == left_dir) {
					g.drawImage(Entity.GUN_DAMAGE, this.getX()-4 - Camera.x,this.getY() - Camera.y-z, null);
				}else {
					g.drawImage(Entity.GUN_DAMAGE, this.getX()+4 - Camera.x,this.getY() - Camera.y-z, null);
				}
			}
	   }
		
	}
}
