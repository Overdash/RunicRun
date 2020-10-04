package com.game.Entity;
/**
 * Created by Master Don Pro (Prodige) on 29/01/2016.
 * @author Prodige Tukala
 *
 * Player Class. Using Singleton Pattern - Creates player and handles all
 * possible events capable by the player. Loads player sprites and animations.
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.game.Audio.JukeBox;
import com.game.TileMap.TileMap;

public class Player extends MapObject {
	
	//References
	private ArrayList<Enemy> enemies;
	
	//Basic Entity Instance Variables
	private int lives;
	private int health;
	private int maxHealth;
	private int damage;
	private int chargeDamage;
	private boolean knockback;
	private boolean flinching;
	private long flinchCount;
	private int score;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	private ArrayList<EnergyParticle> energyParticles;
	private long time;
	private String name;
	
	//Actions
	private boolean dashing;
	private boolean attacking;
	private boolean upattacking;
	private boolean charging;
	private int chargingTick;
	private boolean teleporting;
	
	//Animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
		1, 8, 5, 3, 3, 5, 3, 8, 2, 1, 3
	};
	private final int[] FRAMEWIDTHS = {
		40, 40, 80, 40, 40, 40, 80, 40, 40, 40, 40
	};
	private final int[] FRAMEHEIGHTS = {
		40, 40, 40, 40, 40, 80, 40, 40, 40, 40, 40
	};
	private final int[] SPRITEDELAYS = {
		-1, 3, 2, 6, 5, 2, 2, 2, 1, -1, 1
	};
	
	private Rectangle ar;
	private Rectangle aur;
	private Rectangle cr;
	
	//Animation Actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int ATTACKING = 2;
	private static final int JUMPING = 3;
	private static final int FALLING = 4;
	private static final int UPATTACKING = 5;
	private static final int CHARGING = 6;
	private static final int DASHING = 7;
	private static final int KNOCKBACK = 8;
	private static final int DEAD = 9;
	private static final int TELEPORTING = 10;
	
	//Emotes
	private BufferedImage confused;
	private BufferedImage surprised;
	public static final int NONE = 0;
	public static final int CONFUSED = 1;
	public static final int SURPRISED = 2;
	private int emote = NONE;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		ar = new Rectangle(0, 0, 0, 0);
		ar.width = 30;
		ar.height = 20;
		aur = new Rectangle((int)x - 15, (int)y - 45, 30, 30);
		cr = new Rectangle(0, 0, 0, 0);
		cr.width = 50;
		cr.height = 40;
		
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 38;
		
		moveSpeed = 1.6;
		maxSpeed = 1.6;
		stopSpeed = 1.6;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3;
		
		damage = 2;
		chargeDamage = 1;
		
		facingRight = true;
		
		lives = 3;
		health = maxHealth = 5;
		
		//Load Sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Player/PlayerSprites.gif"));
			
			int count = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < NUMFRAMES.length; i++) {

				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];

				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(j * FRAMEWIDTHS[i], count, FRAMEWIDTHS[i], FRAMEHEIGHTS[i]);
				}//END Inner-for

				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
			}//END Outer-for
			
			//Emotes
			spritesheet = ImageIO.read(getClass().getResourceAsStream(
				"/HUD/Emotes.gif"
			));
			confused = spritesheet.getSubimage(
				0, 0, 14, 17
			);
			surprised = spritesheet.getSubimage(
				14, 0, 14, 17
			);
			
		}//END Try
		catch(Exception e) {
			e.printStackTrace();
		}//END Catch
		
		energyParticles = new ArrayList<EnergyParticle>();
		
		setAnimation(IDLE);
		
		JukeBox.load("/SFX/playerjump.mp3", "playerjump");
		JukeBox.load("/SFX/playerlands.mp3", "playerlands");
		JukeBox.load("/SFX/playerattack.mp3", "playerattack");
		JukeBox.load("/SFX/playerhit.mp3", "playerhit");
		JukeBox.load("/SFX/playercharge.mp3", "playercharge");
		
	}//END Constructor
	
	public void init(ArrayList<Enemy> enemies, ArrayList<EnergyParticle> energyParticles) {
		this.enemies = enemies;
		this.energyParticles = energyParticles;
	}//END init

    //Getters and Setters
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public String getName() { return name; }
	
	public void setEmote(int i) {
		emote = i;
	}
	public void setTeleporting(boolean b) { teleporting = b; }
	public void setName(String name) { this.name = name; }
	
	public void setJumping(boolean b) {
		if(knockback) return;
		if(b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
	}

	public void setAttacking() {
		if(knockback) return;
		if(charging) return;
		if(up && !attacking) upattacking = true;
		else attacking = true;
	}

	public void setCharging() {
		if(knockback) return;
		if(!attacking && !upattacking && !charging) {
			charging = true;
			JukeBox.play("playercharge");
			chargingTick = 0;
		}
	}

	public void setDashing(boolean b) {
		if(!b) dashing = false;
		else if(b && !falling) {
			dashing = true;
		}
	}

	public boolean isDashing() { return dashing; }
	
	public void setDead() {
		health = 0;
		stop();
	}
	
	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}

	public long getTime() { return time; }
	public void setTime(long t) { time = t; }
	public void setHealth(int i) { health = i; }
	public void setLives(int i) { lives = i; }
	public void gainLife() { lives++; }
	public void loseLife() { lives--; }
	public int getLives() { return lives; }
	
	public void increaseScore(int score) {
		this.score += score; 
	}
	
	public int getScore() { return score; }
	
	public void hit(int damage) {
		if(flinching) return;
		JukeBox.play("playerhit");
		stop();
		health -= damage;
		if(health < 0) health = 0;
		flinching = true;
		flinchCount = 0;
		if(facingRight) dx = -1;
		else dx = 1;
		dy = -3;
		knockback = true;
		falling = true;
		jumping = false;
	}//END hit
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}//END reset
	
	public void stop() {
		left = right = up = down = flinching = 
			dashing = jumping = attacking = upattacking = charging = false;
	}//END stop
	
	private void getNextPosition() {
        //KnockBack
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}//END if
		
		double maxSpeed = this.maxSpeed;
		if(dashing) maxSpeed *= 1.75;
		
		//Movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) dx = -maxSpeed;
		}//END if
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) dx = maxSpeed;
		}//END else if
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) dx = 0;
			}//END Inner-if
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) dx = 0;
			}//END Inner-else if
		}//END else/ Outer-if
		
		//Restrict movement when attacking except in air.
		if((attacking || upattacking || charging) && (jumping || falling)) dx = 0;
		
		//Charging
		if(charging) {
			chargingTick++;
			if(facingRight) dx = moveSpeed * (3 - chargingTick * 0.07);
			else dx = -moveSpeed * (3 - chargingTick * 0.07);
		}
		
		//Jumping
		if(jumping && !falling) {
			//sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
			JukeBox.play("playerjump");
		}
		
		if(doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			JukeBox.play("playerjump");
			for(int i = 0; i < 6; i++) {
				energyParticles.add(new EnergyParticle(tileMap, x, y + cheight / 4, EnergyParticle.DOWN));
			}//END Inner-for
		}//END Outer-if
		
		if(!falling) alreadyDoubleJump = false;
		
		//Falling
		if(falling) {
			dy += fallSpeed;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}//END getNextPosition
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	public void update() {
		
		time++;
		
		//Check if Teleporting
		if(teleporting) {
			energyParticles.add(
				new EnergyParticle(tileMap, x, y, EnergyParticle.UP)
			);
		}
		
		//Change/ Update Position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(isFalling && !falling) {
			JukeBox.play("playerlands");
		}
		if(dx == 0) x = (int)x; //Unnecessary line
		
		//Check if flinching complete
		if(flinching) {
			flinchCount++;
			if(flinchCount > 120) {
				flinching = false;
			}//END Inner-if
		}//END Outer-if
		
		//EnergyParticle activity
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).update();
			if(energyParticles.get(i).shouldRemove()) {
				energyParticles.remove(i);
				i--;
			}//END Inner-if
		}//END Outer-for
		
		//Check if attack was finished
		if(currentAction == ATTACKING ||
			currentAction == UPATTACKING) {
			if(animation.hasPlayedOnce()) {
				attacking = false;
				upattacking = false;
			}//END Inner-if
		}//END Outer-if

		if(currentAction == CHARGING) {
			if(animation.hasPlayed(5)) {
				charging = false;
			}//END Inner-if
			cr.y = (int)y - 20;

			if (facingRight) cr.x = (int)x - 15;
			else cr.x = (int)x - 35;

			if(facingRight) energyParticles.add(new EnergyParticle(tileMap, x + 30, y, EnergyParticle.RIGHT));
			else energyParticles.add(new EnergyParticle(tileMap, x - 30, y, EnergyParticle.LEFT));
		}//END Outer-if
		
		//Check if character is interacting with anything
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			//Check Attack
			if(currentAction == ATTACKING &&
					animation.getFrame() == 3 && animation.getCount() == 0) {
				if(e.intersects(ar)) {
					e.hit(damage);
				}
			}
			
			//Check Vertical attack
			if(currentAction == UPATTACKING &&
					animation.getFrame() == 3 && animation.getCount() == 0) {
				if(e.intersects(aur)) {
					e.hit(damage);
				}
			}
			
			//Check Charging attack
			if(currentAction == CHARGING) {
				if(animation.getCount() == 0) {
					if(e.intersects(cr)) {
						e.hit(chargeDamage);
					}//END Inner Nested-if
					/*if(e.intersects(this)) {
						e.hit(chargeDamage);
					}//END Inner nested-if*/
				}//END Inner-if
			}//END Outer-if
			
			//Collision with Enemy (should take damage)
			if(!e.isDead() && intersects(e) && !charging) {
				hit(e.getDamage());
			}//END Inner-if
			
			if(e.isDead()) {
				JukeBox.play("explode", 2000);
			}//END Inner-if
			
		}//END Outer-for
		
		//Priority of Animations.
		if(teleporting) {
			if(currentAction != TELEPORTING) {
				setAnimation(TELEPORTING);
			}//END Inner-if
		}
		else if(knockback) {
			if(currentAction != KNOCKBACK) {
				setAnimation(KNOCKBACK);
			}//END Inner-if
		}
		else if(health == 0) {
			if(currentAction != DEAD) {
				setAnimation(DEAD);
			}//END Inner-if
		}
		else if(upattacking) {
			if(currentAction != UPATTACKING) {
				JukeBox.play("playerattack");
				setAnimation(UPATTACKING);
				aur.x = (int)x - 15;
				aur.y = (int)y - 50;
			}//END Inner-if
			else {
				if(animation.getFrame() == 4 && animation.getCount() == 0) {
					for(int c = 0; c < 3; c++) {
						energyParticles.add(
							new EnergyParticle(
								tileMap,
								aur.x + aur.width / 2,
								aur.y + 5,
								EnergyParticle.UP));
					}//END Inner Nested-for
				}//END Inner-if
			}//END Outer-else
		}//END Parent-else if
		else if(attacking) {
			if(currentAction != ATTACKING) {
				JukeBox.play("playerattack");
				setAnimation(ATTACKING);
				ar.y = (int)y - 6;
				if(facingRight) ar.x = (int)x + 10;
				else ar.x = (int)x - 40;
			}
			else {
				if(animation.getFrame() == 4 && animation.getCount() == 0) {
                    for (int c = 0; c < 3; c++) {
                        if (facingRight) energyParticles.add(new EnergyParticle(tileMap,
                                            ar.x + ar.width - 4, ar.y + ar.height / 2, EnergyParticle.RIGHT));
                        else energyParticles.add(new EnergyParticle(tileMap,
                                ar.x + 4, ar.y + ar.height / 2, EnergyParticle.LEFT));
                    }//END For loop
                }//END Inner Nested-if
            }//END Nested else
        }//END Outer-if
		else if(charging) {
			if(currentAction != CHARGING) {
				setAnimation(CHARGING);
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				setAnimation(JUMPING);
			}
		}
		else if(dy > 0) {
			if(currentAction != FALLING) {
				setAnimation(FALLING);
			}
		}
		else if(dashing && (left || right)) {
			if(currentAction != DASHING) {
				setAnimation(DASHING);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				setAnimation(WALKING);
			}
		}
		else if(currentAction != IDLE) {
			setAnimation(IDLE);
		}
		
		animation.update();
		
		//Set Direction
		if(!attacking && !upattacking && !charging && !knockback) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}//END Update

	//Draw emotes depending on situations
	public void draw(Graphics2D g) {
		
		//Draw Emote
		if(emote == CONFUSED) {
			g.drawImage(confused, (int)(x + xmap - cwidth / 2), (int)(y + ymap - 40), null);
		}
		else if(emote == SURPRISED) {
			g.drawImage(surprised, (int)(x + xmap - cwidth / 2), (int)(y + ymap - 40), null);
		}
		
		//Draw energy particles
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).draw(g);
		}
		
		//Flinch
		if(flinching && !knockback) {
			if(flinchCount % 10 < 5) return;
		}
		
		super.draw(g);
		
	}//END Draw
	
}//END Class