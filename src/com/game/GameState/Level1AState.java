package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 31/01/2016.
 *
 * @author Prodige Tukala
 * <p/>
 * Class for the first stage of the game. Handles enemy population and visuals.
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.game.Audio.JukeBox;
import com.game.Entity.Enemy;
import com.game.Entity.EnemyProjectile;
import com.game.Entity.EnergyParticle;
import com.game.Entity.Explosion;
import com.game.Entity.HUD;
import com.game.Entity.Player;
import com.game.Entity.PlayerSave;
import com.game.Entity.Teleport;
import com.game.Entity.Title;
import com.game.Entity.Enemies.Gazer;
import com.game.Entity.Enemies.GelPop;
import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.TileMap.Background;
import com.game.TileMap.TileMap;

public class Level1AState extends GameState {

    private Background sky;
    private Background clouds;
    private Background mountains;

    private Player player;
    private TileMap tileMap;
    private ArrayList<Enemy> enemies;
    private ArrayList<EnemyProjectile> eprojectiles;
    private ArrayList<EnergyParticle> energyParticles;
    private ArrayList<Explosion> explosions;

    private HUD hud;
    private BufferedImage hageonText;
    private Title title;
    private Title subtitle;
    private Teleport teleport;

    //Events
    private boolean blockInput = false;
    private int eventCount = 0;
    private boolean eventStart;
    private ArrayList<Rectangle> tb;
    private boolean eventFinish;
    private boolean eventDead;

    public Level1AState(GameStateManager gameStateManager) {
        super(gameStateManager);
        init();
    }//END Constructor

    public void init() {

        //Backgrounds
        sky = new Background("/Backgrounds/sky.gif", 0);
        clouds = new Background("/Backgrounds/clouds.gif", 0.1);
        mountains = new Background("/Backgrounds/mountains.gif", 0.2);

        //TileMap
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/ruinstileset.gif");
        tileMap.loadMap("/Maps/level1a.map");
        tileMap.setPosition(140, 0);
        tileMap.setBounds(
                tileMap.getWidth() - tileMap.getTileSize(),
                tileMap.getHeight() - 2 * tileMap.getTileSize(),
                0, 0
        );
        tileMap.setTween(1);

        //Player
        player = new Player(tileMap);
        player.setPosition(300, 161);
        player.setHealth(PlayerSave.getHealth());
        player.setLives(PlayerSave.getLives());
        player.setTime(PlayerSave.getTime());

        //Enemies
        enemies = new ArrayList<Enemy>();
        eprojectiles = new ArrayList<EnemyProjectile>();
        populateEnemies();

        //Energy particle
        energyParticles = new ArrayList<EnergyParticle>();

        //init player
        player.init(enemies, energyParticles);

        //Explosions
        explosions = new ArrayList<Explosion>();

        //HUD
        hud = new HUD(player);

        //Title & Subtitle
        try {
            hageonText = ImageIO.read(
                    getClass().getResourceAsStream("/HUD/HageonTemple.gif")
            );
            title = new Title(hageonText.getSubimage(0, 0, 178, 20));
            title.sety(60);
            subtitle = new Title(hageonText.getSubimage(0, 20, 82, 13));
            subtitle.sety(85);
        }//END Try
        catch (Exception e) {
            e.printStackTrace();
        }//END Catch

        //Teleport
        teleport = new Teleport(tileMap);
        teleport.setPosition(3700, 131);

        //Start Event
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        //SFX
        JukeBox.load("/SFX/teleport.mp3", "teleport");
        JukeBox.load("/SFX/explode.mp3", "explode");
        JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

        //Music
        JukeBox.load("/Music/level1.mp3", "level1");
        JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);

    }//END init

    //Set Enemies *Need to make Get Rekt Mode*
    private void populateEnemies() {
        enemies.clear();
        /*Tengu t = new Tengu(tileMap, player, enemies);
		t.setPosition(1300, 100);
		enemies.add(t);
		t = new Tengu(tileMap, player, enemies);
		t.setPosition(1330, 100);
		enemies.add(t);
		t = new Tengu(tileMap, player, enemies);
		t.setPosition(1360, 100);
		enemies.add(t);*/
        GelPop gp;
        Gazer g;

        gp = new GelPop(tileMap, player);
        gp.setPosition(1300, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1320, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1340, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1660, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1680, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1700, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(2177, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(2960, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(2980, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(3000, 100);
        enemies.add(gp);

        g = new Gazer(tileMap);
        g.setPosition(2600, 100);
        enemies.add(g);
        g = new Gazer(tileMap);
        g.setPosition(3500, 100);
        enemies.add(g);
    }//END populateEnemies

    public void update() {

        //Check Keys
        handleInput();

        //Check if end of Level-event should execute
        if (teleport.contains(player)) {
            eventFinish = blockInput = true;
        }

        //Check if player dead-event should execute
        if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
            eventDead = blockInput = true;
        }

        //Play events
        if (eventStart) eventStart();
        if (eventDead) eventDead();
        if (eventFinish) eventFinish();

        //Move title & Subtitle
        if (title != null) {
            title.update();
            if (title.shouldRemove()) title = null;
        }
        if (subtitle != null) {
            subtitle.update();
            if (subtitle.shouldRemove()) subtitle = null;
        }
        //Will Update and reposition the following:

        //Move Background
        clouds.setPosition(tileMap.getx(), tileMap.gety());
        mountains.setPosition(tileMap.getx(), tileMap.gety());

        //Player
        player.update();

        //Tilemap
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety()
        );
        tileMap.update();
        tileMap.fixBounds();

        //Enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
            }//END Inner-if
        }//END Outer-for

        //Enemy Projectiles
        for (int i = 0; i < eprojectiles.size(); i++) {
            EnemyProjectile ep = eprojectiles.get(i);
            ep.update();
            if (ep.shouldRemove()) {
                eprojectiles.remove(i);
                i--;
            }//END Inner-if
        }//END Outer-for

        //Explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }

        //Teleport
        teleport.update();

    }//END Update

    public void draw(Graphics2D g) {
        //Will Draw the following in level:

        //Background
        sky.draw(g);
        clouds.draw(g);
        mountains.draw(g);

        //Tilemap
        tileMap.draw(g);

        //Enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //Enemy Projectiles
        for (int i = 0; i < eprojectiles.size(); i++) {
            eprojectiles.get(i).draw(g);
        }

        //Explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).draw(g);
        }

        //Player
        player.draw(g);

        //Teleport
        teleport.draw(g);

        //HUD
        hud.draw(g);

        //Title
        if (title != null) title.draw(g);
        if (subtitle != null) subtitle.draw(g);

        //Transition boxes
        g.setColor(java.awt.Color.BLACK);
        for (int i = 0; i < tb.size(); i++) {
            g.fill(tb.get(i));
        }

    }//END Draw

    public void handleInput() {
        if (Keys.isPressed(Keys.ESCAPE)) {
            JukeBox.stop();
            gameStateMngr.setPaused(true);
        }
        if (blockInput || player.getHealth() == 0) return;
        player.setUp(Keys.keyState[Keys.UP]);
        player.setLeft(Keys.keyState[Keys.LEFT]);
        player.setDown(Keys.keyState[Keys.DOWN]);
        player.setRight(Keys.keyState[Keys.RIGHT]);
        player.setJumping(Keys.keyState[Keys.JUMP_BUTTON]);
        player.setDashing(Keys.keyState[Keys.RUN]);
        if (Keys.isPressed(Keys.WIND_SLASH)) player.setAttacking();
        if (Keys.isPressed(Keys.SHOULDER_CHARGE)) player.setCharging();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////// EVENTS *IMPORTANT AF! FIND WAYS TO MAKE MORE INTERESTING!* ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Reset level
    private void reset() {
        player.reset();
        player.setPosition(300, 161);
        populateEnemies();
        blockInput = true;
        eventCount = 0;
        tileMap.setShaking(false, 0);
        eventStart = true;
        eventStart();
        title = new Title(hageonText.getSubimage(0, 0, 178, 20));
        title.sety(60);
        subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
        subtitle.sety(85);
    }//END reset

    //Level started
    private void eventStart() {
        eventCount++;
        if (eventCount == 1) {
            tb.clear();
            tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
            tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
            tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
            tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
        }
        if (eventCount > 1 && eventCount < 60) {
            tb.get(0).height -= 4;
            tb.get(1).width -= 6;
            tb.get(2).y += 4;
            tb.get(3).x += 6;
        }
        if (eventCount == 30) title.begin();
        if (eventCount == 60) {
            eventStart = blockInput = false;
            eventCount = 0;
            subtitle.begin();
            tb.clear();
        }
    }//END eventStart

    //Player Death Handling
    private void eventDead() {
        eventCount++;
        if (eventCount == 1) {
            player.setDead();
            player.stop();
        }
        if (eventCount == 60) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
        } else if (eventCount > 60) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
        }
        if (eventCount >= 120) {
            if (player.getLives() == 0) {
                JukeBox.stop("level1");
                gameStateMngr.setState(GameStateManager.DEATHSTATE);
            } else {
                eventDead = blockInput = false;
                eventCount = 0;
                player.loseLife();
                reset();
            }//END Inner if-else
        }//END Outer if
    }//END eventDead

    //Completed Level
    private void eventFinish() {
        eventCount++;
        if (eventCount == 1) {
            JukeBox.play("teleport");
            player.setTeleporting(true);
            player.stop();
        } else if (eventCount == 120) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
        } else if (eventCount > 120) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
            JukeBox.stop("teleport");
        }
        if (eventCount == 180) {
            PlayerSave.setHealth(player.getHealth());
            PlayerSave.setLives(player.getLives());
            PlayerSave.setTime(player.getTime());
            gameStateMngr.setState(GameStateManager.LEVEL1BSTATE);
        }

    }//END eventFininsh

}//END Class