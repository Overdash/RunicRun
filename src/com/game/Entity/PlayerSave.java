package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 01/02/2016.
 * @author Prodige Tukala
 *
 * Saves the player's starting stats.
 */


public class PlayerSave {
	
	private static int lives = 3;
	private static int health = 5;
	private static long time = 0;
	
	public static void init() {
		lives = 3;
		health = 5;
		time = 0;
	}//END init
	
	public static int getLives() { return lives; }
	public static void setLives(int i) { lives = i; }
	
	public static int getHealth() { return health; }
	public static void setHealth(int i) { health = i; }
	
	public static long getTime() { return time; }
	public static void setTime(long t) { time = t; }
	
}//END Class
