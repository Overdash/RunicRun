package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 * @author Prodige Tukala
 *
 * Displays new screen and controls upon death.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
//Local Imports
import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.Audio.JukeBox;

public class DeathState extends GameState {

    private Font font;
    private Color colour;

    public DeathState(GameStateManager gameStateManager){
        super(gameStateManager);
        JukeBox.stop();
        font = new Font("Georgia", Font.BOLD, 18);
    }

    public void draw(Graphics2D g){
        colour = new Color(0f,0f,0f,.5f);
        g.setColor(colour);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString("LOL! You Died!", 85, 120);
        g.drawString(" Press W to Play Again",70, 160);
    }

    public void update() { handleInput(); }

    public void init(){ }

    public void handleInput(){
        if (Keys.isPressed(Keys.ENTER) || Keys.isPressed(Keys.JUMP_BUTTON)) {
        gameStateMngr.setState(GameStateManager.MENUSTATE);
        JukeBox.play("level1");
        }
        if (Keys.isPressed(Keys.SHOULDER_CHARGE)) gameStateMngr.setState(GameStateManager.LEVEL1ASTATE);
    }

}
