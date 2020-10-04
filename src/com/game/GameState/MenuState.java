package com.game.GameState;
/**
 * Created by Master Don Pro (Prodige) 28/01/2016
 * @author Prodige Tukala
 *
 * This Class is for the menu, it will handle menu activity and be where the program starts.
 * */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.game.Audio.JukeBox;
import com.game.Entity.PlayerSave;
import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.Misc.infoLoadGUI;
import com.game.TileMap.Background;

public class MenuState extends GameState {
	
	private BufferedImage head;
    private Background bg;
	
	private int currentChoice = 0;

	private String[] options = {
		"Start", "Load", "Instructions", "Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private Font font2;
	
	public MenuState(GameStateManager gameStateManager) {
		
		super(gameStateManager);

		try {

            //Load Menu Background
            bg = new Background("/Backgrounds/menubg.gif",1,1);
            bg.setVector(-0.5,0);

			//Load Floating head sprite
			head = ImageIO.read(
				getClass().getResourceAsStream("/HUD/Hud.gif")
			).getSubimage(0, 12, 12, 11);
			
			//Titles and Colour
			titleColor = Color.RED;
			titleFont = new Font("Castellar", Font.BOLD, 34);
			font = new Font("Arial", Font.PLAIN, 14);
			font2 = new Font("Arial", Font.PLAIN, 10);
			
			//Load sound FX
			JukeBox.load("/SFX/menuoption.mp3", "menuoption");
			JukeBox.load("/SFX/menuselect.mp3", "menuselect");
			JukeBox.stop("level1");
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
		
	}//END Constructor
	
	public void init() {}
	
	public void update() {
		
		//Check Keys
		handleInput();

        //Check Background Movement
        bg.update();
		
	}//END Update
	
	public void draw(Graphics2D g) {
		
		//Draw background
        bg.draw(g);
		//g.setColor(Color.BLACK);
		//g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		//Draw Game Title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Runic Run", 35, 90);
		
		//Draw menu Options
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("Start", 145, 145);
		g.drawString("Load", 145, 165);
        g.drawString("Instructions", 145, 185);
		g.drawString("Quit", 145, 205);
		
		//Draw Floating Head Sprite
		if(currentChoice == 0) g.drawImage(head, 125, 134, null);
		else if(currentChoice == 1) g.drawImage(head, 125, 154, null);
		else if(currentChoice == 2) g.drawImage(head, 125, 174, null);
        else if(currentChoice == 3) g.drawImage(head, 125, 194, null);

		//Signature/ Honours.
		g.setFont(font2);
		g.drawString("Main Coder: Prodige - *I don't take full credit.*", 10, 232);
		
	}//END draw
	
	private void select() {
		if(currentChoice == 0) {
			JukeBox.play("menuselect");
			PlayerSave.init();
			gameStateMngr.setState(GameStateManager.LEVEL1ASTATE);
		}
        else if(currentChoice == 1) {
            JukeBox.play("menuselect");
            PlayerSave.init();
            gameStateMngr.setState(GameStateManager.LOADSTATE);
        }
        else if (currentChoice == 2) {
            JukeBox.play("menuselect");
            new infoLoadGUI();
        }
        else if (currentChoice == 3) {
            System.exit(0);
        }
    }//END select
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ENTER)) select();
		if(Keys.isPressed(Keys.UP)) {
			if(currentChoice > 0) {
				JukeBox.play("menuoption", 0);
				currentChoice--;
			}//END inner-IF
		}//END Outer-IF

		if(Keys.isPressed(Keys.DOWN)) {
			if(currentChoice < options.length - 1) {
				JukeBox.play("menuoption", 0);
				currentChoice++;
			}//END inner-IF
		}//END Outer-IF

	}//END handleInput
	
}










