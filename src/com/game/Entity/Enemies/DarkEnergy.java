package com.game.Entity.Enemies;

/**
 * Created by Master Don Pro (Prodige) on 03/02/2016.
 * @author Prodige Tukala
 *
 * Blue print for Enemy projectile.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.game.Entity.Enemy;
import com.game.Handlers.Content;
import com.game.TileMap.TileMap;


public class DarkEnergy extends Enemy {
	
	private BufferedImage[] startSprites;
	private BufferedImage[] sprites;
	
	private boolean start;
	private boolean permanent;
	
	private int type = 0;
	public static int VECTOR = 0;
	public static int GRAVITY = 1;
	public static int BOUNCE = 2;
	
	private int bounceCount = 0;
	
	public DarkEnergy(TileMap tm) {
		
		super(tm);
		
		health = maxHealth = 1;
		
		width = 20;
		height = 20;
		cwidth = 12;
		cheight = 12;
		
		damage = 1;
		moveSpeed = 5;
		
		startSprites = Content.DarkEnergy[0];
		sprites = Content.DarkEnergy[1];
		
		animation.setFrames(startSprites);
		animation.setDelay(2);
		
		start = true;
		flinching = true;
		permanent = false;
		
	}//END Constructor

	//Setters
	public void setType(int i) { type = i; }
	public void setPermanent(boolean b) { permanent = b; }
	
	public void update() {
		
		if(start) {
			if(animation.hasPlayedOnce()) {
				animation.setFrames(sprites);
				animation.setNumFrames(3);
				animation.setDelay(2);
				start = false;
			}//END Inner-if
		}//END Outer If

        //Check Projectile's current state
		if(type == VECTOR) {
			x += dx;
			y += dy;
		}
		else if(type == GRAVITY) {
			dy += 0.2;
			x += dx;
			y += dy;
		}
		else if(type == BOUNCE) {
			double dx2 = dx;
			double dy2 = dy;
			checkTileMapCollision();
			if(dx == 0) {
				dx = -dx2;
				bounceCount++;
			}//END Inner-if
			if(dy == 0) {
				dy = -dy2;
				bounceCount++;
			}//END Inner-if
			x += dx;
			y += dy;
		}
		
		//Animation Update
		animation.update();
		
		if(!permanent) {
			if(x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight()) {
				remove = true;
			}//END Inner-if
			if(bounceCount == 3) {
				remove = true;
			}//END Inner-if
		}
		
	}//END Update

    public void setDamage(int dmg){ damage = dmg; }
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}//END Draw
	
}//END Class
