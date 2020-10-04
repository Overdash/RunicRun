package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Base Class for Enemies. Extention of MapObject (of course)
 * using the Factory Pattern.
 */

import com.game.Audio.JukeBox;
import com.game.TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	
	protected boolean flinching;
	protected long flinchCount;
	
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
	}//END Constructor
	
	public boolean isDead() { return dead; }//END isDead
	public boolean shouldRemove() { return remove; }//END shouldRemove

	public int getDamage() { return damage; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		JukeBox.play("enemyhit");
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
		flinching = true;
		flinchCount = 0;
	}//END hit
	
	public void update() {}
	
}//END Class














