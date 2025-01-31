package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Energy particle physics. Used mainly in boss room.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.game.Handlers.Content;
import com.game.TileMap.TileMap;

public class EnergyParticle extends MapObject {
	
	private int count;
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	
	public EnergyParticle(TileMap tm, double x, double y, int dir) {
		super(tm);
		this.x = x;
		this.y = y;
		double d1 = Math.random() * 2.5 - 1.25;
		double d2 = -Math.random() - 0.8; 
		if(dir == UP) {
			dx = d1;
			dy = d2;
		}
		else if(dir == LEFT) {
			dx = d2;
			dy = d1;
		}
		else if(dir == DOWN) {
			dx = d1;
			dy = -d2;
		}
		else {
			dx = -d2;
			dy = d1;
		}
		
		count = 0;
		sprites = Content.EnergyParticle[0];
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}//END Constructor
	
	public void update() {
		x += dx;
		y += dy;
		count++;
		if(count == 60) remove = true;
	}//END Update
	
	public boolean shouldRemove() { return remove; }//END shouldRemove
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}//END draw
	
}//END Class
