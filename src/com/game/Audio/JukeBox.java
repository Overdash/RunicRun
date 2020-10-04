package com.game.Audio;
/**
 * Created by Master Don Pro (Prodige) on 04/01/2016
 * @author Prodige Tukala
 *
 * Global class, used to play music.
 * Uses mp3spi1.9.5.jar (w/ jl1.0.1.jar & tritonus.jar) to allow compressed audio
 * format compatibility (I'll be using .mp3).
 * */
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip; //Allows to start and stop clips, inherits Data Line

public class JukeBox {
	
	private static HashMap<String, Clip> clips;
	private static int gap;
	private static boolean mute = false;
    private static int position, clipStart, clipEnd;
    private static String clipOn;
	
	public static void init() {
		clips = new HashMap<String, Clip>();
		gap = 0;
	}//END init
	
	public static void load(String s, String n) {
		if(clips.get(n) != null) return;
		Clip clip;
		try {			
			AudioInputStream ais = AudioSystem.getAudioInputStream(JukeBox.class.getResourceAsStream(s));

			AudioFormat baseFormat = ais.getFormat();

			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
				16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false
            );
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);

			clips.put(n, clip);
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
	}//END load
	
	public static void play(String s) {
		play(s, gap);
	}
	
	public static void play(String s, int i) {
		if(mute) return;
		Clip c = clips.get(s);
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}

    public static void stop(){
        if(clips.get(getClip()) == null) return;
        if(clips.get(getClip()).isRunning()) clips.get(getClip()).stop();
    }

    public static void pause(){
        if(mute) return;
        if(clips.get(getClip()) == null || !clips.get(getClip()).isRunning()) return;
        if(position == -1){
            position = getPosition(getClip());
            clipOn = getClip();
            //System.out.println(position + " " + clipOn);
        }
    }
	
	public static void stop(String s) {
		if(clips.get(s) == null) return;
		if(clips.get(s).isRunning()) clips.get(s).stop();
        clipOn = s;
        position = getPosition(s);
	}
	
	public static void resume(String s) {
		if(mute) return;
		if(clips.get(s).isRunning()) return;
		clips.get(s).start();
	}

    public static void resume(){
        if(mute || clips.get(clipOn).isRunning()) return;
        clips.get(clipOn).start();
        clips.get(clipOn).setLoopPoints(clipStart, clipEnd);
        clips.get(clipOn).loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void mute(){
        if(mute) return;
        mute = true;
    }

    public static String getClip(){
        String clip = "";
        for (String s: clips.keySet()){
            if(clips.get(s).isRunning()) clip = s;
        }
        return clip;
    }
	
	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}
	
	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
        clipStart = start;
        clipEnd = end;
	}
	
	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}
	
	public static int getFrames(String s) { return clips.get(s).getFrameLength(); }
	public static int getPosition(String s) { return clips.get(s).getFramePosition(); }
	
	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}
	
}