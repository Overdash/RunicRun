package com.game.Entity.Enemies;

/**
 * Created by Master Don Pro (Prodige)on 22/08/2016.
 */

import java.awt.*;
import java.util.ArrayList;

import com.game.Entity.Enemy;
import com.game.TileMap.TileMap;
import com.game.Entity.Player;
import com.game.Entity.Enemies.DarkEnergy;

public class GelPrime extends GelPop {

    private boolean attacked = false;
    private int stepCount;

    private int xDmg;
    private Player player;

    //private ArrayList<Enemy> enemies;

    public GelPrime(TileMap tm, Player p){
        super(tm,p);
        player = p;
        stepCount = 0;
        //enemies = new ArrayList<Enemy>();
    }

    public void attack(){
        xDmg = damage + (int)(damage*Math.random());
        if(isFacingPlayer()){
            DarkEnergy de = new DarkEnergy(tileMap);
            de.setType(DarkEnergy.VECTOR);
            de.setPosition(x,y);
            de.setDamage(xDmg);
            if(player.getx() > getx()) de.setVector(6,0);
            else de.setVector(-6,0);
            stepCount = 0;
            attacked = true;
        }
    }

    public void update(){
        super.update();
        if(!attacked || stepCount >= 30)attack();
        else stepCount++;
    }

    public boolean isFacingPlayer(){
        return ((facingRight && player.getx() > getx()) ||
                (!facingRight && player.getx() < getx()));
    }

    public void draw(Graphics2D g){ super.draw(g);}
}
