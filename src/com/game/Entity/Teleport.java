package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Teleporting System
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.game.TileMap.TileMap;

public class Teleport extends MapObject {
	
	private BufferedImage[] sprites;
	
	public Teleport(TileMap tm) {
		super(tm);
		facingRight = true;
		width = height = 40;
		cwidth = 20;
		cheight = 40;
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/Teleport.gif"));
			sprites = new BufferedImage[9];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width, 0, width, height
				);
			}
			animation.setFrames(sprites);
			animation.setDelay(1);
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
	}//END Constructor
	
	public void update() { animation.update(); }//END Update
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}//END Draw
	
}//END Class
