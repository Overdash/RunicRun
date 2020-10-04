package com.game.GameState;

/**
 * Created by Master Don Pro (Prodige) on 03/02/2016.
 *
 * @author Prodige Tukala
 * <p/>
 * Handles 2nd stage. Populates enemy in areas of stage and retrieves player info
 * from first stage.
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
import com.game.Entity.Enemies.Tengu;
import com.game.Handlers.Keys;
import com.game.Main.GamePanel;
import com.game.TileMap.Background;
import com.game.TileMap.TileMap;


public class Level1BState extends GameState {

    private Background temple;

    //Interactive Entities
    private Player player;
    private TileMap tileMap;
    private ArrayList<Enemy> enemies;
    private ArrayList<EnemyProjectile> eprojectiles;
    private ArrayList<EnergyParticle> energyParticles;
    private ArrayList<Explosion> explosions;

    //GFX
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
    private boolean eventQuake;

    public Level1BState(GameStateManager gsm) {
        super(gsm);
        init();
    }

    public void init() {

        //Backgrounds
        temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

        //TileMap
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/ruinstileset.gif");
        tileMap.loadMap("/Maps/level1b.map");
        tileMap.setPosition(140, 0);
        tileMap.setTween(1);

        //Player
        player = new Player(tileMap);
        player.setPosition(300, 131);
        player.setHealth(PlayerSave.getHealth());
        player.setLives(PlayerSave.getLives());
        player.setTime(PlayerSave.getTime());

        //Enemies
        enemies = new ArrayList<Enemy>();
        eprojectiles = new ArrayList<EnemyProjectile>();
        populateEnemies();

        //Energy Particles
        energyParticles = new ArrayList<EnergyParticle>();

        player.init(enemies, energyParticles);

        //Explosion
        explosions = new ArrayList<Explosion>();

        //Hud
        hud = new HUD(player);

        //Title and Subtitle
        try {
            hageonText = ImageIO.read(getClass().getResourceAsStream("/HUD/HageonTemple.gif"));
            title = new Title(hageonText.getSubimage(0, 0, 178, 20));
            title.sety(60);
            subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
            subtitle.sety(85);
        }//END Try
        catch (Exception e) {
            e.printStackTrace();
        }//END Catch

        //Teleport
        teleport = new Teleport(tileMap);
        teleport.setPosition(2850, 371);

        //Start Event
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        //Load Stage SFX
        JukeBox.load("/SFX/teleport.mp3", "teleport");
        JukeBox.load("/SFX/explode.mp3", "explode");
        JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

    }//END Constructor

    private void populateEnemies() {
        enemies.clear();
        GelPop gp;
        Gazer g;
        Tengu t;

        gp = new GelPop(tileMap, player);
        gp.setPosition(750, 100);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(900, 150);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1320, 250);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1570, 160);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(1590, 160);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(2600, 370);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(2620, 370);
        enemies.add(gp);
        gp = new GelPop(tileMap, player);
        gp.setPosition(2640, 370);
        enemies.add(gp);

        g = new Gazer(tileMap);
        g.setPosition(904, 130);
        enemies.add(g);
        g = new Gazer(tileMap);
        g.setPosition(1080, 270);
        enemies.add(g);
        g = new Gazer(tileMap);
        g.setPosition(1200, 270);
        enemies.add(g);
        g = new Gazer(tileMap);
        g.setPosition(1704, 300);
        enemies.add(g);

        t = new Tengu(tileMap, player, enemies);
        t.setPosition(1900, 580);
        enemies.add(t);
        t = new Tengu(tileMap, player, enemies);
        t.setPosition(2330, 550);
        enemies.add(t);
        t = new Tengu(tileMap, player, enemies);
        t.setPosition(2400, 490);
        enemies.add(t);
        t = new Tengu(tileMap, player, enemies);
        t.setPosition(2457, 430);
        enemies.add(t);

    }//END populateEnemies

    public void update() {

        //Check Keys
        handleInput();

        //Late Stage Quake Event Check
        if (player.getx() > 2175 && !tileMap.isShaking()) {
            eventQuake = blockInput = true;
        }

        //End of Level Check
        if (teleport.contains(player)) {
            eventFinish = blockInput = true;
        }

        //Play stage events
        if (eventStart) eventStart();
        if (eventDead) eventDead();
        if (eventQuake) eventQuake();
        if (eventFinish) eventFinish();

        //Move title and subtitle
        if (title != null) {
            title.update();
            if (title.shouldRemove()) title = null;
        }
        if (subtitle != null) {
            subtitle.update();
            if (subtitle.shouldRemove()) subtitle = null;
        }

        //Move backgrounds
        temple.setPosition(tileMap.getx(), tileMap.gety());

        //Update player
        player.update();
        if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
            eventDead = blockInput = true;
        }

        //Update tilemap
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety()
        );
        tileMap.update();
        tileMap.fixBounds();

        //Update enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                i--;
                explosions.add(
                        new Explosion(tileMap, e.getx(), e.gety()));
            }//END Inner-if
        }//END Outer-for

        //Update enemy projectiles
        for (int i = 0; i < eprojectiles.size(); i++) {
            EnemyProjectile ep = eprojectiles.get(i);
            ep.update();
            if (ep.shouldRemove()) {
                eprojectiles.remove(i);
                i--;
            }//END Inner-if
        }//END Outer-for

        //Update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }//END Inner-if
        }//END Outer-for

        //Update teleport
        teleport.update();

    }//END Update

    public void draw(Graphics2D g) {

        //Draw background
        temple.draw(g);

        //Draw tilemap
        tileMap.draw(g);

        //Draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //Draw enemy projectiles
        for (int i = 0; i < eprojectiles.size(); i++) {
            eprojectiles.get(i).draw(g);
        }

        //Draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).draw(g);
        }

        //Draw player
        player.draw(g);

        //Draw teleport
        teleport.draw(g);

        //Draw hud
        hud.draw(g);

        //Draw title
        if (title != null) title.draw(g);
        if (subtitle != null) subtitle.draw(g);

        //Draw transition boxes
        g.setColor(java.awt.Color.BLACK);
        for (int i = 0; i < tb.size(); i++) {
            g.fill(tb.get(i));
        }

    }

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

    //Reset level (upon death)
    private void reset() {
        player.loseLife();
        player.reset();
        player.setPosition(300, 131);
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

    // player has died
    private void eventDead() {
        eventCount++;
        if (eventCount == 1) player.setDead();
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
                reset();
            }//END Inner if-else
        }//END Outer if
    }//END eventDead

    // earthquake
    private void eventQuake() {
        eventCount++;
        if (eventCount == 1) {
            player.stop();
            player.setPosition(2175, player.gety());
        }
        if (eventCount == 60) {
            player.setEmote(Player.CONFUSED);
        }
        if (eventCount == 120) player.setEmote(Player.NONE);
        if (eventCount == 150) tileMap.setShaking(true, 10);
        if (eventCount == 180) player.setEmote(Player.SURPRISED);
        if (eventCount == 300) {
            player.setEmote(Player.NONE);
            eventQuake = blockInput = false;
            eventCount = 0;
        }
    }//END eventQuake

    //Finished level
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
            gameStateMngr.setState(GameStateManager.LEVEL1CSTATE);
        }

    }//END eventFinish

}//END Class