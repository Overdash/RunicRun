package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 01/02/2016.
 * @author Prodige Tukala
 *
 * Mechanics used for enemy projectiles
 */

import java.awt.Graphics2D;

import com.game.TileMap.TileMap;


public abstract class EnemyProjectile extends MapObject {
	
	protected boolean hit;
	protected boolean remove;
	protected int damage;
	
	public EnemyProjectile(TileMap tm) {
		super(tm);
	}//END Constructor
	
	public int getDamage() { return damage; }
	public boolean shouldRemove() { return remove; }
	
	public abstract void setHit();
	
	public abstract void update();
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}//END Class
