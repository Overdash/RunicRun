package com.game.Handlers;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Global Class. contains a boolean array of current and previous key states
 * and the keys that are used for this game. A key k is down when keyState[k] is true.
 * Prevents concurrency issues.
 */

import java.awt.event.KeyEvent;

public class Keys {
	
	public static final int NUM_KEYS = 16;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int JUMP_BUTTON = 4;
	public static int RUN = 5;
	public static int WIND_SLASH = 6;
	public static int SHOULDER_CHARGE = 7;
	public static int ENTER = 8;
	public static int ESCAPE = 9;
    public static int SAVE = 10;
    public static int DEBUG = 11;
	//Assigning keys
	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_UP) keyState[UP] = b;
		else if(i == KeyEvent.VK_LEFT) keyState[LEFT] = b;
		else if(i == KeyEvent.VK_DOWN) keyState[DOWN] = b;
		else if(i == KeyEvent.VK_RIGHT) keyState[RIGHT] = b;
		else if(i == KeyEvent.VK_SPACE) keyState[JUMP_BUTTON] = b;
		else if(i == KeyEvent.VK_E) keyState[RUN] = b;
		else if(i == KeyEvent.VK_R) keyState[WIND_SLASH] = b;
		else if(i == KeyEvent.VK_W) keyState[SHOULDER_CHARGE] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
        else if(i == KeyEvent.VK_S) keyState[SAVE] = b;
        else if(i == KeyEvent.VK_M) keyState[DEBUG] = b;
	}//END keySet
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}//END Update
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}//END isPressed
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}//END anyKeyPress
	
}//END Class
