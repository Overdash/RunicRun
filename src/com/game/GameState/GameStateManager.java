package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) 28/01/2016
 *
 * This Class handles every state in the game. Whenever the interface requires changing,
 * it first goes through this class.
 * */

import com.game.Audio.JukeBox;
import com.game.Main.GamePanel;
import java.awt.Graphics2D;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
    private static int currentLevel;

	private PauseState pauseState;
	private boolean paused;

	private DeathState deathState;
	private boolean dead;

	public static final int MENUSTATE = 0;
    public static final int DEATHSTATE = 1;
	public static final int LEVEL1ASTATE = 2;
	public static final int LEVEL1BSTATE = 3;
	public static final int LEVEL1CSTATE = 4;
    public static final int LEVEL2ASTATE = 5;
    //public static final int SAVESTATE = 8;
    public static final int LOADSTATE = 9;
	public static final int ACIDSTATE = 15;
	public static final int MAXGAMESTATES = 16;
	
	public GameStateManager() {
		
		JukeBox.init();
		
		gameStates = new GameState[MAXGAMESTATES];
		
		pauseState = new PauseState(this);
		paused = false;

		deathState = new DeathState(this);
		dead = false;
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}//END Constructor
	
	private void loadState(int state) {
		if(state == MENUSTATE) gameStates[state] = new MenuState(this);
		else if(state == LEVEL1ASTATE) gameStates[state] = new Level1AState(this);
		else if(state == LEVEL1BSTATE) gameStates[state] = new Level1BState(this);
		else if(state == LEVEL1CSTATE) gameStates[state] = new Level1CState(this);
		else if(state == ACIDSTATE) gameStates[state] = new AcidState(this);
		else if(state == DEATHSTATE) gameStates[state] = new DeathState(this);
        else if(state == LOADSTATE) gameStates[state] = new LoadState(this);
        else if(state == LEVEL2ASTATE) gameStates[state] = new Level2AState(this);

        currentLevel = state;
	}//END loadState
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}//END unloadState

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}//END setState
	
	public void setPaused(boolean b) { paused = b; }//END setPaused
	
	public void update() {
		if(paused) {
			pauseState.update();
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].update();
	}//END update
	
	public void draw(Graphics2D g) {
		if(paused) {
			pauseState.draw(g);
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].draw(g);
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}//END draw

    //Getters
    public static int getCurrentLevel(){
        return currentLevel;
    }

}