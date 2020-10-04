package com.game.TileMap;

/**
 * Created by Master Don Pro (Prodige) on 28/01/2016.
 * @author Prodige Tukala
 *
 * This Class will allow us to create "walk-able" and "collide-able" tiles
 */

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private int type;
	
	//Tile Types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}//END Constructor

    //Getters
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }
	
}
