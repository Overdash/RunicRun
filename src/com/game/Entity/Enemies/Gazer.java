package com.game.Entity.Enemies;

/**
 * Created by Master Don Pro (Prodige) on 02/02/2016.
 * @author Prodige Tukala
 *
 * Blue print for Enemy: Gazer. Has no attacks just flies in the air
 * causes damage if player runs into it without attacking.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.game.Entity.Enemy;
import com.game.Handlers.Content;
import com.game.TileMap.TileMap;


public class Gazer extends Enemy {
	
	private BufferedImage[] idleSprites;
	
	private int tick;
	private double a;
	private double b;
	
	public Gazer(TileMap tm) {
		
		super(tm);
		
		health = maxHealth = 2;
		
		width = 39;
		height = 20;
		cwidth = 25;
		cheight = 15;
		
		damage = 1;
		moveSpeed = 5;
		
		idleSprites = Content.Gazer[0];
		
		animation.setFrames(idleSprites);
		animation.setDelay(4);
		
		tick = 0;
		a = Math.random() * 0.06 + 0.07;
		b = Math.random() * 0.06 + 0.07;
		
	}//END Constructor
	
	public void update() {
		
		//Flinch Check
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		tick++;
		x = Math.sin(a * tick) + x;
		y = Math.sin(b * tick) + y;
		
		//Animation Update
		animation.update();
		
	}//END Update
	
	public void draw(Graphics2D g) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.draw(g);
		
	}//END Draw
	
}//END Class
