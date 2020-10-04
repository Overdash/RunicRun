package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 06/02/2016.
 * @author Prodige Tukala
 *
 * Saves game/ overwrites previous game save. Saves into a .txt file.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.game.Entity.Player;
import com.game.Entity.PlayerSave;

public class SaveState{

    private int currentLevel = GameStateManager.getCurrentLevel();

    public SaveState(){

        String strLevel = String.valueOf(currentLevel);
        try{
            File saveFile = new File("GameSave.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));
            out.write(strLevel);

            out.close();
        }//END try
        catch(Exception e){
            e.printStackTrace();
        }//END Catch

    }//END Constructor

}//END Class
