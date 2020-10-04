package com.game.Main;

/**
 * Created by Master Don Pro (Prodige) on 28/01/2016.
 * @author Prodige Tukala
 * Runic Run - Side-scroller, Platformer Mario/Sonic-esque Adventure Game
 * Read Instruction file for Bibliography
 *
 * This is the main method - the entry point of the program.
 */

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {//Game entry point
		JFrame window = new JFrame("Runic Run");
		window.add(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}//END Main
	
}//END Class
