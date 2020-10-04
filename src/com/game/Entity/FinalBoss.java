package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 18/08/2016.
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.*;

import com.game.Entity.Enemies.DarkEnergy;
import com.game.TileMap.TileMap;

public class FinalBoss extends Enemy {

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private boolean finalAttack;
    private boolean active;
    private int xDmg;
    private int step;
    private int stepCount;
    private int[] steps = {4, 0, 1, 0, 1, 2, 0, 0, 3, 2, 1, 0};

    /////////////    Attacks:    /////////////////
    // Spear Charge (0)
    // Beam (1)
    // Orbs of Destruction (2)
    // Recharge (3)
    // Idle Taunt (4)
    ////////////    Specials:   /////////////////
    // After half hp: Create shield
    // After quarter hp: Bullet hell with Shield
    // *NOTE* 60 = 1s. Need to reduce sprite size. Also fix how fast it ticks.

    //Animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] NUMFRAMES = {12, 7, 7, 9, 9, 4, 7, 4};
    private final int[] FRAMEWIDTHS = {80, 80, 120, 100, 80, 80, 80, 80};
    private final int[] FRAMEHEIGHTS = {80, 80, 80, 120, 80, 80, 80, 80};
    private final int[] SPRITEDELAYS = {2, 3, 5, 12, 15, 3, 3, 20};

    //Actions
    private static final int IDLE = 0;
    private static final int CHARGING = 1;
    private static final int SPEAR = 2;
    private static final int BOMBARD = 3;
    private static final int DEATHBALLS = 4;
    private static final int SHIELD = 5;
    private static final int MATERIALISE = 6;
    private static final int BEAM = 7;

    private double ticks;
    private DarkEnergy[] shield;
    //private DarkEnergy[] beam;

    public FinalBoss (TileMap tm, Player p, ArrayList<Enemy> enemies, ArrayList<Explosion> explosions){
        super(tm);
        player = p;
        this.enemies = enemies;
        this.explosions = explosions;

        health = maxHealth = 120;
        moveSpeed = 1.8;

        damage = 1;

        //Load Sprites
        try{
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/FinalBoss.gif"));
            int count = 0;
            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < NUMFRAMES.length; i++){

                BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];

                for (int j = 0; j < NUMFRAMES[i]; j++){
                    bi[j] = sheet.getSubimage(j * FRAMEWIDTHS[i], count, FRAMEWIDTHS[i], FRAMEHEIGHTS[i]);
                }//END Inner-for

                sprites.add(bi);
                count += FRAMEHEIGHTS[i];
            }// END Outer-for

        }catch (Exception e){
            e.printStackTrace();
        }

        facingRight = false;
        shield = new DarkEnergy[4];
        //beam = new DarkEnergy[8];
        step = 0;
        stepCount = 0;
        setAnimation(MATERIALISE);
    }

    private void setAnimation(int i){
        currentAction = i;
        animation.setFrames(sprites.get(currentAction));
        animation.setDelay(SPRITEDELAYS[currentAction]);
        width = FRAMEWIDTHS[currentAction];
        height = FRAMEHEIGHTS[currentAction];
    }

    public void setActive(){ active = true; }

    public void update(){

        if (health == 0) return;

        //Reset pattern
        if(step == steps.length) step = 0;

        ticks++;

        if (flinching) {
            flinchCount++;
            if(flinchCount == 8) flinching = false;
        }

        x += dx;
        y += dy;

        animation.update();

        if(!active) return;

        //////////////
        // Specials //
        //////////////

        if(health <= maxHealth/2){
            createShield();
        }//END If

        if(!finalAttack && health <= maxHealth / 4){
            stepCount = 0;
            finalAttack = true;
        }

        if (finalAttack) {
            if (stepCount == 1){
                createShield();
                //x = -9000; //Disappears char off screen!
                //y = 9000;
                //dx = dy = 0;
            }
            if(stepCount == 60) {
                //x = tileMap.getWidth() / 2;
                //y = tileMap.getHeight() / 2;
                explosions.add(new Explosion(tileMap, (int) x, (int) y));
            }
            if(stepCount >= 90 && stepCount % 30 == 0){
                createRain();
            }
            if(stepCount >= 120 && stepCount % 10 == 0){
                rageMode();
            }
        }//END Outer-If

        /////////////
        // Attacks //
        /////////////

        if(steps[step] == 0) atkSpear();
        else if(steps[step] == 1) fireBeam();
        else if(steps[step] == 2) createRain();
        else if(steps[step] == 3) charging();
        else if(steps[step] == 4) idle();
    }//END Update

    private void createShield(){
        setAnimation(SHIELD);
        explosions.add(new Explosion(tileMap, (int)x, (int)y));
        for(int i = 0; i < shield.length; i++){
            if(shield[i] == null){
                shield[i] = new DarkEnergy(tileMap);
                shield[i].health = shield[i].maxHealth = 5;
                shield[i].setPermanent(true);
                enemies.add(shield[i]);
            }
        }
        double pos = ticks/32;
        shield[0].setPosition(x + 45 * Math.sin(pos), y + 45 * Math.cos(pos));
        shield[2].setPosition(x - 45 * Math.sin(pos), y - 45 * Math.cos(pos));
        pos += 3.1415;
        shield[1].setPosition(x + 45 * Math.sin(pos), y + 45 * Math.cos(pos));
        shield[3].setPosition(x - 45 * Math.sin(pos), y - 45 * Math.cos(pos));

        for(DarkEnergy e: shield){
            if(player.intersects(e)) e.health--;
            if(e.isDead()){
                e.setPermanent(false);
                enemies.remove(e);
            }
        }//END For-each
    }//END Create Shield

    private void createRain(){
        if(stepCount >= 90) stepCount = 1;
        if(stepCount == 1){
            setAnimation(BOMBARD);
        }
        if(stepCount > 60 && stepCount < 559 && stepCount % 10 == 0) {
            explosions.add(new Explosion(tileMap, tileMap.getWidth() / 2, 40));
            DarkEnergy de = new DarkEnergy(tileMap);
            de.setPosition(tileMap.getWidth() / 2, 40);
            de.setVector(3 * Math.sin(stepCount / 32), 3 * Math.cos(stepCount / 32));
            if (finalAttack) de.setType(DarkEnergy.BOUNCE);
            else de.setType(DarkEnergy.VECTOR);
            enemies.add(de);
            //if (de.notOnScreen()) enemies.remove(de);
        }
        if(stepCount == 559){
            stepCount = 0;
            step++;
        }
    }//END Create Rain

    private void atkSpear(){
        xDmg = damage;
        stepCount++;
        if(stepCount == 1){
            explosions.add(new Explosion(tileMap,(int)x,(int)y));
            setAnimation(SPEAR);
            damage += (int)(damage * Math.random());
        }
        if(stepCount == 60){
            if(x < tileMap.getWidth() / 2){
                dx = -6;
                facingRight = true;
            }
            else{
                dx = 6;
                facingRight = false;
            }
            checkTileMapCollision();
        }
        if((dx == -6 && x < 30) || (dx == 6 && x > tileMap.getWidth() - 30)){
            stepCount = 0;
            step++;
            dx = dy = 0;
            damage = xDmg;
        }
    }//END Spear

    private void charging(){
        //stepCount++;
        dx = dy = 0;
        setAnimation(CHARGING);
        stepCount = 0;
    }//END Charging

    private void fireBeam(){
        xDmg = damage;
        stepCount++;
        if(stepCount == 1){
            if(facingRight) explosions.add(new Explosion(tileMap, (int) x+5, (int) y));
            else explosions.add(new Explosion(tileMap,(int) x-5, (int) y));
            setAnimation(BEAM);
            damage += (int)(damage * Math.random());
        }
        if(stepCount % 20 == 0){
            DarkEnergy de = new DarkEnergy(tileMap);
            de.setType(DarkEnergy.VECTOR);
            if(facingRight){
                de.setPosition(x+5,y);
                de.setVector(player.getx(),player.gety());
            }
            else{
                de.setPosition(x-5,y);
                de.setVector(-player.getx(),player.gety());
            }
            enemies.add(de);
            if(de.notOnScreen()) enemies.remove(de);
        }
        if(stepCount == 240){
            stepCount = 0;
            step++;
            damage = xDmg;
        }
    }//END Beam

    private void rageMode(){
       /* int temp = 0;
        if(stepCount >= 120){
            temp = stepCount;
            stepCount = 1;
        } else stepCount++; */
        if(stepCount == 120){
            explosions.add(new Explosion(tileMap, (int)x, (int)y));
            setAnimation(DEATHBALLS);
        }
        if(stepCount > 120 && stepCount % 10 == 0){
            explosions.add(new Explosion(tileMap, tileMap.getWidth() / 2, 40));
            DarkEnergy de = new DarkEnergy(tileMap);
            de.setType(DarkEnergy.VECTOR);
            de.setPosition(tileMap.getWidth() / 2, 40);
            de.setVector(player.getx(),player.gety());
            enemies.add(de);
        }
        //stepCount = temp;
    }//END Rage

    private void idle(){
        stepCount++;
        if(stepCount == 1) setAnimation(IDLE);
        if(stepCount == 20) explosions.add(new Explosion(tileMap, (int)x, (int)y));
        if(stepCount == 60){
            stepCount = 0;
            step++;
        }
    }//END Idle

    public void draw(Graphics2D g){
        if(flinching)
            if(flinchCount == 0 || flinchCount == 2) return; // Was like Spirit boss
        super.draw(g);
    }//END Draw
}
