package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Draws the HUD (Head-up display)
 */


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	
	private BufferedImage heart;
	private BufferedImage life;
	
	public HUD(Player p) {

		player = p;
		try {
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/HUD/Hud.gif"));
			heart = image.getSubimage(0, 0, 13, 12);
			life = image.getSubimage(0, 12, 12, 11);
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
	}//END Constructor
	
	public void draw(Graphics2D g) {

		for(int i = 0; i < player.getHealth(); i++) {
			g.drawImage(heart, 10 + i * 15, 10, null);
		}

		for(int i = 0; i < player.getLives(); i++) {
			g.drawImage(life, 10 + i * 15, 25, null);
		}

		g.setColor(Color.WHITE);
		g.drawString(player.getTimeToString(), 290, 15);
	}//END Draw
	
}//END Class













