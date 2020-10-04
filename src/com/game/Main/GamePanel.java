package com.game.Main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.game.GameState.GameStateManager;
import com.game.Handlers.Keys;


@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	//Frame Dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	//Game Thread *FPS Handling*
	private Thread thread;
	private boolean running;
	private final int FPS = 60;
	private long targetTime = 1000 / FPS; //(Will be in ms)
	
	//Image
	private BufferedImage image;
	private Graphics2D g;
	
	//Game State Manager
	private GameStateManager gameStateManager;
	
	//Misc - Would be nice to finish this
	private boolean recording = false;
	private int recordingCount = 0;
	private boolean screenshot;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true); //Allows the game panel to have the focus of the application/ window
		requestFocus();
	}//END Constructor

	@Override
	public void addNotify() { //Overriding a method in AWT Library
		super.addNotify();
            if(thread == null) {
			thread = new Thread(this); //Creates a new thread, separate from input threads.
			addKeyListener(this);
			thread.start(); //When called will execute the method run()
		}
	}//END addNotify
	
	private void init() { //Initialisation method. Called post-execution.
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics(); //Cast (Down-casted g). Prints graphics
		/*g.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);*/
		
		
		running = true;
		
		gameStateManager = new GameStateManager();
		
	}//END init
	
	public void run() { //Works like main -> thread.start() will run this method.
		init();

        //Timers
		long start;
		long elapsed;
		long wait;
		
		//Game Loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start; //(in ns)
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			}//END Try
			catch(Exception e) {
				e.printStackTrace();
			}//END Catch
			
		}//END while
		
	}//END run
	
	private void update() {
		gameStateManager.update();
		Keys.update();
	}//END Update

	private void draw() {
		gameStateManager.draw(g);
	}//END draw

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); //Stretches graphics to fill screen
		g2.dispose();

        //TRY FINISH THIS!! *NOT IMPORTANT*
		if(screenshot) {
			screenshot = false;
			try {
				java.io.File out = new java.io.File("/screenshot " + System.nanoTime() + ".gif");
				javax.imageio.ImageIO.write(image, "gif", out);
			}//END Try
			catch(Exception e) {e.printStackTrace();} //END Catch
		}
		if(!recording) return;
		try {
			java.io.File out = new java.io.File("/frame" + recordingCount + ".gif");
			javax.imageio.ImageIO.write(image, "gif", out);
			recordingCount++;
		}//END Try
		catch(Exception e) { e.printStackTrace(); }//END Catch
	}//END drawToScreen

	//Following methods must be implemented due to KeyListener being an implementation
    //for this class
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		if(key.isControlDown()) {
			if(key.getKeyCode() == KeyEvent.VK_R) {
				recording = !recording;
				return;
			}
			if(key.getKeyCode() == KeyEvent.VK_S) {
				screenshot = true;
				return;
			}
		}
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}

}