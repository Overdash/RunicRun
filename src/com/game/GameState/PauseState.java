package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Handles pause menu and inputs while the game is paused.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.Audio.JukeBox;

public class PauseState extends GameState {
	
	private Font font;
	private Color colour;
	public PauseState(GameStateManager gameStateManager) {
		
		super(gameStateManager);
        JukeBox.stop();
		//Fonts
		font = new Font("Century Gothic", Font.PLAIN, 14);
		
	}
	
	public void init() {}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		colour = new Color(0f,0f,0f,.5f);
		g.setColor(colour);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 100, 120);
        g.drawString("Press SpaceBar to return to the Main Menu", 14, 140);
        g.drawString("Or S to save the game", 90, 160);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)){
			gameStateMngr.setPaused(false);
			JukeBox.resume();
		}
		if(Keys.isPressed(Keys.JUMP_BUTTON)) {
			gameStateMngr.setPaused(false);
			gameStateMngr.setState(GameStateManager.MENUSTATE);
			//JukeBox.resume("level1");
		}
        if(Keys.isPressed(Keys.SAVE)){
            new SaveState();
        }
	}

}
