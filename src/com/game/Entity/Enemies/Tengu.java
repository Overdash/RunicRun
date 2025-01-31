package com.game.Entity.Enemies;

/**
 * Created by Master Don Pro (Prodige) on 02/02/2016.
 * @author Prodige Tukala
 *
 * Blue print for Enemy: Tengu. Initially jumps back when player is
 * detected. Can attack while jumping.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.game.Entity.Enemy;
import com.game.Entity.Player;
import com.game.Handlers.Content;
import com.game.TileMap.TileMap;

public class Tengu extends Enemy {
	
	private Player player;
	private ArrayList<Enemy> enemies;
	
	private BufferedImage[] idleSprites;
	private BufferedImage[] jumpSprites;
	private BufferedImage[] attackSprites;
	
	private boolean jumping;
	
	private static final int IDLE = 0;
	private static final int JUMPING = 1;
	private static final int ATTACKING = 2;
	
	private int attackTick;
	private int attackDelay = 30;
	private int step;
	
	public Tengu(TileMap tm, Player p, ArrayList<Enemy> en) {
		
		super(tm);
		player = p;
		enemies = en;
		
		health = maxHealth = 4;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 26;
		
		damage = 1;
		moveSpeed = 1.5;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		idleSprites = Content.Tengu[0];
		jumpSprites = Content.Tengu[1];
		attackSprites = Content.Tengu[2];
		
		animation.setFrames(idleSprites);
		animation.setDelay(-1);
		
		attackTick = 0;
		
	}//END Constructor
	
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping && !falling) dy = jumpStart;

	}//END getNextPosition
	
	public void update() {
		
		//Flinch check
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//Animation Update
		animation.update();
		
		if(player.getx() < x) facingRight = false;
		else facingRight = true;
		
		//Idle actions
		if(step == 0) {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idleSprites);
				animation.setDelay(-1);
			}//END Inner-if
			attackTick++;
			if(attackTick >= attackDelay && Math.abs(player.getx() - x) < 60) {
				step++;
				attackTick = 0;
			}//END Inner-if
		}//END Outer-if

		//Jump away
		if(step == 1) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}//END Inner-if
			jumping = true;
			if(facingRight) left = true;
			else right = true;
			if(falling) {
				step++;
			}//END Inner-if
		}//END Outer-if

		//Attack (Do while jumping if possible)
		if(step == 2) {
			if(dy > 0 && currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attackSprites);
				animation.setDelay(3);
				DarkEnergy de = new DarkEnergy(tileMap);
				de.setPosition(x, y);
				if(facingRight) de.setVector(3, 3);
				else de.setVector(-3, 3);
				enemies.add(de);
			}//END Inner-if
			if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
				step++;
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}//END Inner-if
		}//END Outer-if

		//Attack Complete
		if(step == 3) {
			if(dy == 0) step++;
		}

		//Land
		if(step == 4) {
			step = 0;
			left = right = jumping = false;
		}
		
	}//END Update
	
	public void draw(Graphics2D g) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.draw(g);
		
	}//END Draw
	
}//END Class
