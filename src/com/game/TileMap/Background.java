package com.game.TileMap;

/**
 * Created by Master Don Pro (Prodige) on 28/01/2016.
 * @author Prodige Tukala
 *
 * This Class will set, handle and move the background of the game in most states.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.game.Main.GamePanel;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private int width;
	private int height;

    //Movement Scales
	private double xscale;
	private double yscale;

    //Four Overloaded Constructors
	public Background(String s) {
		this(s, 0.1);
	}
	
	public Background(String s, double d) {
		this(s, d, d);
	}
	
	public Background(String s, double d1, double d2) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			height = image.getHeight();
			xscale = d1;
			yscale = d2;
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
	}//END Overloaded Constructor 3
	
	public Background(String s, double ms, int x, int y, int w, int h) {
		try { image = ImageIO.read(getClass().getResourceAsStream(s));
			image = image.getSubimage(x, y, w, h);
			width = image.getWidth();
			height = image.getHeight();
			xscale = ms;
			yscale = ms;
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
	}//END Overloaded Constructor 4

    //Getters & Setters

    /*Prevents background from extending off screen
      Resets once it goes past screen
      Provides smoother scrolling in-game */
	public void setPosition(double x, double y) {
		this.x = (x * xscale) % width;
		this.y = (y * yscale) % height;
	}

    //This method is for Auto-scrolling the background
    //Necessary for pre-use testing (to test map files w/o playing the full game) & Menu
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}//END setVector
	
	public void setScale(double xscale, double yscale) {
		this.xscale = xscale;
		this.yscale = yscale;
	}
	
	public void setDimensions(int i1, int i2) {
		width = i1;
		height = i2;
	}
	
	public double getx() { return x; }
	public double gety() { return y; }
	
	public void update() {
		x += dx;
		while(x <= -width) x += width;
		while(x >= width) x -= width;
		y += dy;
		while(y <= -height) y += height;
		while(y >= height) y -= height;
	}//END Update
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, null);

        //Verify the background is always drawing on-screen
		if(x < 0) {
			g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
		}
		if(x > 0) {
			g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
		if(y < 0) {
			g.drawImage(image, (int)x, (int)y + GamePanel.HEIGHT, null);
		}
		if(y > 0) {
			g.drawImage(image, (int)x, (int)y - GamePanel.HEIGHT, null);
		}
	}//END draw
	
}//END Class







