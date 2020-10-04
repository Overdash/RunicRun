package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 01/02/2016.
 * @author Prodige Tukala
 *
 * Handles how explosions occur and how they are displayed on screen.
 */


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.game.Handlers.Content;
import com.game.TileMap.TileMap;

public class Explosion extends MapObject {
	
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	private Point[] points;
	private int speed;
	private double diagSpeed;
	
	public Explosion(TileMap tm, int x, int y) {
		
		super(tm);
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		speed = 2;
		diagSpeed = 1.41;
		
		sprites = Content.Explosion[0];
		
		animation.setFrames(sprites);
		animation.setDelay(6);
		
		points = new Point[8];
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point(x, y);
		}
		
	}//END Constructor
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
		points[0].x += speed;
		points[1].x += diagSpeed;
		points[1].y += diagSpeed;
		points[2].y += speed;
		points[3].x -= diagSpeed;
		points[3].y += diagSpeed;
		points[4].x -= speed;
		points[5].x -= diagSpeed;
		points[5].y -= diagSpeed;
		points[6].y -= speed;
		points[7].x += diagSpeed;
		points[7].y -= diagSpeed;
		
	}//END Update
	
	public boolean shouldRemove() { return remove; }//END shouldRemove
	
	public void draw(Graphics2D g) {
		setMapPosition();
		for (Point point : points) {
			g.drawImage(
					animation.getImage(),
					(int) (point.x + xmap - width / 2),
					(int) (point.y + ymap - height / 2),
					null
			);
		}//END for
	}//END draw
	
}//END Class