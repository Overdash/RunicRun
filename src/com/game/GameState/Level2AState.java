package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige)on 21/06/2016.
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.game.Audio.JukeBox;
import com.game.Entity.*;
import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.TileMap.Background;
import com.game.TileMap.TileMap;

public class Level2AState extends GameState {

    //private Background sky;
    //private Background clouds;
    private Background temple;

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


    public Level2AState(GameStateManager gameStateManager) {
        super(gameStateManager);
        init();
    }

    public void init() {

        //Background
        //sky = new Background("/Backgrounds/temple.gif", 0.5, 0);
        temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

        //Tilemap
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/firetileset.gif");
        tileMap.loadMap("/Maps/level2a.map");
        tileMap.setPosition(140, 0);
        /*tileMap.setBounds(tileMap.getWidth() - tileMap.getTileSize(),
                tileMap.getHeight() - 2 * tileMap.getTileSize(), 0, 0
        );*/
        tileMap.setTween(1);

        //Player
        player = new Player(tileMap);
        player.setPosition(110, 161);
        player.setLives(PlayerSave.getLives());
        player.setLives(PlayerSave.getLives());
        player.setTime(PlayerSave.getTime());

        //Enemies & Particles
        enemies = new ArrayList<Enemy>();
        eprojectiles = new ArrayList<EnemyProjectile>();
        energyParticles = new ArrayList<EnergyParticle>();
        explosions = new ArrayList<Explosion>();
        populateEnemies();

        //Player initialisation
        player.init(enemies, energyParticles);
        hud = new HUD(player);

        //Titles

        //Teleport
        teleport = new Teleport(tileMap);
        teleport.setPosition(3136,821);

        //Event Start
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        //eventStart();

        //SFX
        JukeBox.load("/SFX/teleport.mp3", "teleport");
        JukeBox.load("/SFX/explode.mp3", "explode");
        JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

        //Music


    }

    private void populateEnemies() {
        FinalBoss fb;
        enemies.clear();
        fb = new FinalBoss(tileMap,player, enemies, explosions);
        fb.setPosition(110, 161);
        enemies.add(fb);
    }

    public void update() {

        //Check input
        handleInput();

        //Check if end of level
        //if (teleport.contains(player)) eventFinish = blockInput = true;

        //Check player dead-event should execute
        if (player.getHealth() == 0 || player.gety() > tileMap.getHeight())
            eventDead = blockInput = true;

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
        temple.setPosition(tileMap.getx(), tileMap.gety());

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
        //sky.draw(g);
        temple.draw(g);

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
        //if(Keys.isPressed(Keys.DEBUG)) player.getPos();
    }





    private void reset() {
        //player.loseLife();
        player.reset();
        player.setPosition(110, 161);
        populateEnemies();
        blockInput = true;
        eventCount = 0;
        tileMap.setShaking(false, 0);
        eventStart = true;
        eventStart();
        /*title = new Title(hageonText.getSubimage(0, 0, 178, 20));
        title.sety(60);
        subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
        subtitle.sety(85);*/
    }//END reset

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
        //if (eventCount == 30) title.begin();
        if (eventCount == 60) {
            eventStart = blockInput = false;
            eventCount = 0;
            //subtitle.begin();
            tb.clear();
        }
    }

    private void eventFinish() {
    }

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
                JukeBox.stop();
                gameStateMngr.setState(GameStateManager.DEATHSTATE);
            } else {
                eventDead = blockInput = false;
                eventCount = 0;
                player.loseLife();
                reset();
            }//END Inner if-else
        }//END Outer if
    }
}
