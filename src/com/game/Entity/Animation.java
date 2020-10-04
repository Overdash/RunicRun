package com.game.Entity;
/**
 * Created by Master Don Pro (Prodige) on 29/01/2016.
 *
 * This Class is in charge of processing all animation sprites/ images
 * Efficiently creates smooth animations... TRIES! to prevent LAG.
 */
import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames; //Must store the frames in an array
	private int currentFrame;
	private int numFrames;
	
	private int count;
	private int delay;
	
	private int timesPlayed;
	
	public Animation() {
		timesPlayed = 0;
	}//END Constructor

    //Setters
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numFrames = frames.length;
	}
	
	public void setDelay(int i) { delay = i; }
	// public void setFrame(int i) { currentFrame = i; }
	public void setNumFrames(int i) { numFrames = i; }
	
	public void update() {
		
		if(delay == -1) return;
		
		count++;
		
		if(count == delay) {
			currentFrame++;
			count = 0;
		}
		if(currentFrame == numFrames) {
			currentFrame = 0;
			timesPlayed++;
		}
		
	}//END Update

	//Getters
	public int getFrame() { return currentFrame; }
	public int getCount() { return count; }
	public BufferedImage getImage() { return frames[currentFrame]; }

	public boolean hasPlayedOnce() { return timesPlayed > 0; }//END hasPlayedOne
	public boolean hasPlayed(int i) { return timesPlayed == i; }//END hasPlayed
	
}//END Class