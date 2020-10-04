package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 06/02/2016.
 * @author Prodige Tukala
 *
 * Responsible for loading game if save game exists.
 */

import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class LoadState extends GameState{

    private int currentLevel;

    public LoadState(GameStateManager gameStateManager){
        super(gameStateManager);

        try{
            Scanner findLevel = new Scanner(new FileReader("GameSave.txt"));
            String level;
            if(findLevel.hasNext()){
                level = findLevel.next();
                currentLevel = Integer.parseInt(level);
                if(!level.equals(null)) gameStateMngr.setState(currentLevel);
            }
        }//END Try
        catch (Exception e){
            e.printStackTrace();
        }//END Catch
    }//END Constructor

    public void init(){}
    public void update(){}
    public void draw(Graphics2D g){}
    public void handleInput(){}

}//END Class
