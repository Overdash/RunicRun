package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 04/02/2016.
 * @author Prodige Tukala
 *
 * Handles the 3rd Stage (Boss Room). Handles Quake event and stores quest. Retrieves
 * player info from Third stage.
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.game.Audio.JukeBox;
import com.game.Entity.Enemy;
import com.game.Entity.EnergyParticle;
import com.game.Entity.Explosion;
import com.game.Entity.HUD;
import com.game.Entity.Player;
import com.game.Entity.PlayerSave;
import com.game.Entity.Portal;
import com.game.Entity.Spirit;
import com.game.Entity.Artfact.BottomLeftPiece;
import com.game.Entity.Artfact.BottomRightPiece;
import com.game.Entity.Artfact.TopLeftPiece;
import com.game.Entity.Artfact.TopRightPiece;
import com.game.Entity.Enemies.DarkEnergy;
import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.TileMap.Background;
import com.game.TileMap.TileMap;

public class Level1CState extends GameState {
	
	private Background temple;
	
	private Player player;
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnergyParticle> energyParticles;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private TopLeftPiece tlp;
	private TopRightPiece trp;
	private BottomLeftPiece blp;
	private BottomRightPiece brp;
	private Portal portal;
	
	private Spirit spirit;
	
	//Events
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;
	private boolean eventFinish;
	private boolean eventDead;
	private boolean eventPortal;
	private boolean flash;
	private boolean eventBossDead;
	
	public Level1CState(GameStateManager gsm) {
		super(gsm);
		init();
	}//END Constructor
	
	public void init() {
		
		//Backgrounds
		temple = new Background("/Backgrounds/temple.gif", 0.5, 0);
		
		//Tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap("/Maps/level1c.map");
		tileMap.setPosition(140, 0);
		tileMap.setTween(1);
		
		//Player
		player = new Player(tileMap);
		player.setPosition(50, 190);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());
		
		//Explosions
		explosions = new ArrayList<Explosion>();
		
		//Enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		
		//Energy particle
		energyParticles = new ArrayList<EnergyParticle>();
		
		//Init player
		player.init(enemies, energyParticles);
		
		//Hud
		hud = new HUD(player);
		
		//Portal
		portal = new Portal(tileMap);
		portal.setPosition(160, 154);
		
		//Artifact
		tlp = new TopLeftPiece(tileMap);
		trp = new TopRightPiece(tileMap);
		blp = new BottomLeftPiece(tileMap);
		brp = new BottomRightPiece(tileMap);
		tlp.setPosition(152, 102);
		trp.setPosition(162, 102);
		blp.setPosition(152, 112);
		brp.setPosition(162, 112);
		
		//Start event
		eventStart = blockInput = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
		//SFX
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
		
		//Music
		JukeBox.load("/Music/level1boss.mp3", "level1boss");
		JukeBox.load("/Music/fanfare.mp3", "fanfare");
		
	}//END init
	
	private void populateEnemies() {
		enemies.clear();
		spirit = new Spirit(tileMap, player, enemies, explosions);
		spirit.setPosition(-9000, 9000);
		enemies.add(spirit);
	}//END populateEnemies
	
	public void update() {
		
		//Check keys
		handleInput();
		
		//Check if boss dead event should execute
		if(!eventFinish && spirit.isDead()) {
            eventBossDead = blockInput = true;
            if(portal.contains(player)) {
                eventFinish = true;
            }
        }

		//Check if player dead event should execute
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		//Play events
		if(eventStart) eventStart();
		if(eventDead) eventDead();
		if(eventFinish) eventFinish();
		if(eventPortal) eventPortal();
		if(eventBossDead) eventBossDead();
		
		//Move backgrounds
		temple.setPosition(tileMap.getx(), tileMap.gety());
		
		//Update player
		player.update();
		
		//Update tilemap
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		tileMap.update();
		tileMap.fixBounds();
		
		//Update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead() || e.shouldRemove()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
			}
		}
		
		//Update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		//Update Portal
		portal.update();
		
		//Update Artifact
		tlp.update();
		trp.update();
		blp.update();
		brp.update();
		
	}//END Update
	
	public void draw(Graphics2D g) {
		
		//Draw background
		temple.draw(g);
		
		//Draw tilemap
		tileMap.draw(g);
		
		//Draw portal
		portal.draw(g);
		
		//Draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		//Draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}
		
		//Draw Artifact
		tlp.draw(g);
		trp.draw(g);
		blp.draw(g);
		brp.draw(g);
		
		//Draw Player
		player.draw(g);
		
		//Draw HUD
		hud.draw(g);
		
		//Draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}
		
		//Flash
		if(flash) {
			g.setColor(java.awt.Color.WHITE);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
		
	}//END draw
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) {
            JukeBox.stop();
            gameStateMngr.setPaused(true);
        }
		if(blockInput || player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.JUMP_BUTTON]);
		player.setDashing(Keys.keyState[Keys.RUN]);
		if(Keys.isPressed(Keys.WIND_SLASH)) player.setAttacking();
		if(Keys.isPressed(Keys.SHOULDER_CHARGE)) player.setCharging();
	}//END handleInput

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////// EVENTS *IMPORTANT AF! FIND WAYS TO MAKE MORE INTERESTING!* ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Reset level (upon death)
	private void reset() {
		player.reset();
		player.setPosition(50, 190);
		populateEnemies();
		eventStart = blockInput = true;
		eventCount = 0;
		eventStart();
	}//END reset
	
	//Level started
	private void eventStart() {
		eventCount++;
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			if(!portal.isOpened()) tileMap.setShaking(true, 10);
			JukeBox.stop("level1");
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if(eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			eventPortal = blockInput = true;
			tb.clear();
			
		}
	}//END eventStart
	
	//Player has died
	private void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventCount >= 120) {
			if(player.getLives() == 0) {
                JukeBox.stop("level1boss");
                gameStateMngr.setState(GameStateManager.DEATHSTATE);
			}
			else {
				eventDead = blockInput = false;
				eventCount = 0;
				player.loseLife();
				reset();
			}//END Inner if-else
		}//END Outer-if
	}//END eventDead
	
	//Finished level
	private void eventFinish() {
		eventCount++;
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 1) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventCount == 60) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			gameStateMngr.setState(GameStateManager.LEVEL2ASTATE);
		}
		
	}//END eventFinish
	
	private void eventPortal() {
		eventCount++;
		if(eventCount == 1) {
			if(portal.isOpened()) {
				eventCount = 360;
			}
		}
		if(eventCount > 60 && eventCount < 180) {
			energyParticles.add(
				new EnergyParticle(tileMap, 157, 107, (int) (Math.random() * 4)));
		}
		if(eventCount >= 160 && eventCount <= 180) {
			if(eventCount % 4 == 0 || eventCount % 4 == 1) flash = true;
			else flash = false;
		}
		if(eventCount == 181) {
			tileMap.setShaking(false, 0);
			flash = false;
			tlp.setVector(-0.3, -0.3);
			trp.setVector(0.3, -0.3);
			blp.setVector(-0.3, 0.3);
			brp.setVector(0.3, 0.3);
			player.setEmote(Player.SURPRISED);
		}
		if(eventCount == 240) {
			tlp.setVector(0, -5);
			trp.setVector(0, -5);
			blp.setVector(0, -5);
			brp.setVector(0, -5);
		}
		if(eventCount == 300) {
			player.setEmote(Player.NONE);
			portal.setOpening();
		}
		if(eventCount == 360) {
			flash = true;
			spirit.setPosition(160, 160);
			DarkEnergy de;
			for(int i = 0; i < 20; i++) {
				de = new DarkEnergy(tileMap);
				de.setPosition(160, 160);
				de.setVector(Math.random() * 10 - 5, Math.random() * -2 - 3);
				enemies.add(de);
			}//END Inner for
		}
		if(eventCount == 362) {
			flash = false;
			JukeBox.loop("level1boss", 0, 60000, JukeBox.getFrames("level1boss") - 4000);
		}
		if(eventCount == 420) {
			eventPortal = blockInput = false;
			eventCount = 0;
			spirit.setActive();
		}
		
	}//END eventPortal
	
	public void eventBossDead() {
		eventCount++;
		if(eventCount == 1) {
			player.stop();
			JukeBox.stop("level1boss");
			enemies.clear();
		}
		if(eventCount <= 120 && eventCount % 15 == 0) {
			explosions.add(new Explosion(tileMap, spirit.getx(), spirit.gety()));
			JukeBox.play("explode");
		}
		if(eventCount == 180) {
			JukeBox.play("fanfare");
		}
		if(eventCount == 390) {
			eventBossDead = false;
			eventCount = 0;
			eventFinish = true;

		}
	}//END eventBossDead

}