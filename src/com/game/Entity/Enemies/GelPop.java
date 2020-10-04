package com.game.Entity.Enemies;

/**
 * Created by Master Don Pro (Prodige) on 02/02/2016.
 * @author Prodige Tukala
 *
 * Blue print for Enemy: GelPop. No attack, slides on platforms
 * Causes damage if Player comes in contact without attacking.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.game.Entity.Enemy;
import com.game.Entity.Player;
import com.game.Handlers.Content;
import com.game.Main.GamePanel;
import com.game.TileMap.TileMap;

public class GelPop extends Enemy {
	
	private BufferedImage[] sprites;
	private Player player;
	private boolean active;
	
	public GelPop(TileMap tm, Player p) {
		
		super(tm);
		player = p;
		
		health = maxHealth = 1;
		
		width = 25;
		height = 25;
		cwidth = 20;
		cheight = 18;
		
		damage = 1;
		moveSpeed = 0.8;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		sprites = Content.GelPop[0];
		
		animation.setFrames(sprites);
		animation.setDelay(4);
		
		left = true;
		facingRight = false;
		
	}//END Constructor
	
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping && !falling) {
			dy = jumpStart;
		}
	}//END getNextPosition
	
	public void update() {
		
		if(!active) {
			if(Math.abs(player.getx() - x) < GamePanel.WIDTH) active = true;
			return;
		}
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		setPosition(xtemp, ytemp);
		
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		// update animation
		animation.update();
		
	}//END Update
	
	public void draw(Graphics2D g) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.draw(g);
		
	}//END Draw
	
}//END Class
