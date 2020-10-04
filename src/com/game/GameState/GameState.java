package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Abstract Class where all game states root from. Uses Factory Pattern.
 */


import java.awt.Graphics2D;

public abstract class GameState {
	
	protected GameStateManager gameStateMngr;
	
	public GameState(GameStateManager gameStateMngr) {
		this.gameStateMngr = gameStateMngr;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();
	
}//END Class
