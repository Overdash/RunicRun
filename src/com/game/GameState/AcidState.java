package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 03/02/2016.
 * @author Prodige Tukala
 *
 * Handles the Acid-state (To prevent unnecessary reloading during game start up)
 */


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.game.Handlers.Keys;
import com.game.Main.GamePanel;

public class AcidState extends GameState {

	private Color colour;
	private float hue;

	private BufferedImage img;
	private double angle;
	
	public AcidState(GameStateManager gameStateMngr) {
		super(gameStateMngr);
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/PlayerSprites.gif"
            )).getSubimage(0, 0, 40, 40);
		}//END Try
		catch(Exception e) {}
	}//END Catch
	
	public void init() {}
	
	public void update() {
		handleInput();
		colour = Color.getHSBColor(hue, 1f, 1f);
		hue += 0.01;
		if(hue > 1) hue = 0;
		angle += 0.1;
	}//END Update
	
	public void draw(Graphics2D g) {
		g.setColor(colour);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		AffineTransform at = new AffineTransform();
		at.translate(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		at.rotate(angle);
		g.drawImage(img, at, null);
	}//END draw
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gameStateMngr.setState(GameStateManager.MENUSTATE);
	}//END handleInput

}//END Class
