package com.FsStudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.FsStudios.main.Game;
import com.FsStudios.main.Sound;
import com.FsStudios.world.AStar;
import com.FsStudios.world.Camera;
import com.FsStudios.world.Vector2i;
import com.FsStudios.world.World;


public class Enemy extends Entity {
	
	private double speed = 0.8;
	
	public static int point = 0;
	
	private double dx;
	private double dy;
	
    private int frames = 0,maxFrames = 20,index = 0,maxIndex = 1;
	private BufferedImage[] sprites;

	private int life = 10;	
	
	public boolean isDamaged = false;
	private int damageFrames = 10, damageCur = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		sprites = new BufferedImage[2];
		
		
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}
    public void tick() {
    	maskx = 5;
    	masky = 10;
    	mwidth = 8;
    	mheight = 8;
    	
    	depth = 0;
    	
    	if(!isCollidingWithPlayer()) {
    	if(path == null || path.size() == 0) {
    		Vector2i start = new Vector2i ((int)(x/16), (int)(y/16));
    		Vector2i end = new Vector2i ((int)(Game.player.x/16), (int)(Game.player.y/16));
    		path = AStar.findPath(Game.world, start, end);
    	}}else {
    		//estamos perto do player
    		if(Game.rand.nextInt(100) < 10) {
    			
    			Sound.hurtEffect.play();
    		Game.life-= Game.rand.nextInt(3);
    		Game.player.isDamaged = true;
    		
    		System.out.println(Game.life);
    		}
    		}
    	if(new Random().nextInt(100) < 90)
    	    followPath(path);
    	if(new Random().nextInt(100) < 5) {
    		Vector2i start = new Vector2i ((int)(x/16), (int)(y/16));
    		Vector2i end = new Vector2i ((int)(Game.player.x/16), (int)(Game.player.y/16));
    		path = AStar.findPath(Game.world, start, end);
    	}
    	
         frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
				
			}
			 
					collidingBullet();
			         if(life <= 0) {
			        	 Sound.deadEnemyEffect.play();
			        	 point+= 1;
			        	 destroySelf();
			        	 return;
			        }
				
         
         if(isDamaged) {
        	 	this.damageCur++;
        	 	if(this.damageCur == this.damageFrames ) {
        	 		this.damageCur = 0;
        	 		this.isDamaged = false;
        	 	}
         }
         
    }
    public void destroySelf() {
    	if(World.isFreeDynamic((int)(x+(dx*speed)), (int)(y+(dy*speed)),16,16)) {
    		x+=dx * speed;
    		y+=dy * speed;
    		Game.enemies.remove(this);
        	Game.entities.remove(this);
        	World.generateParticles(20, (int)x, (int)y);
			return;
    		}else {
    			
    		}
    	
    }
    
    public void collidingBullet() {
    	for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
				if(Entity.isColidding(this,e)) {
					isDamaged = true;
						this.life--;
					Game.bullets.remove(i);
					return;
				}
		}
		
		
	}
	public boolean isCollidingWithPlayer() {
    	Rectangle enemyCur = new Rectangle(this.getX() + maskx, this.getY() + masky,mwidth,mheight);
    	Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(),16,16);
    	
    	return enemyCur.intersects(player);
    }
    
    
    public void render(Graphics g) {
    	if (!isDamaged) 
    	g.drawImage(sprites[index], this.getX() - Camera.x,this.getY() - Camera.y,null );
     else 
    	 g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x,this.getY() - Camera.y,null );
   //  g.setColor(Color.black);
   // g.fillRect(this.getX()	+ maskx - Camera.x, this.getY()	+ masky - Camera.y, mwidth, mheight);
     
    	 
    }
    
}
    
