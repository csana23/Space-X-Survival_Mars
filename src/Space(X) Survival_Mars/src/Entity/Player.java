package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.*;


public class Player extends MapObject {
	
	//player
	private int health;
	private int maxHealth;
	private long fire;
	private long maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	//laser "beam"
	private boolean firing;
	private int fireCost = 0;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	//gliding not included
	
	//animations
	private ArrayList<BufferedImage[]> sprites;
	
	private final int[] numFrames = {
			1, 1, 2, 1, 1 
	};
	
	//animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 4;
	
	public Player(TileMap2 tm) {
		super(tm);
		
		width = 25;
		height = 40;
		//20
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 100000000;
		
		fire = maxFire = 999999999;
		
		//fireBallDamege has to be equal with the maxhealth of 
		//enemy entity
		fireBallDamage = 1;
		fireBalls = new ArrayList<>();
		
		//load sprites 
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/entities/player/Sheet2.gif")
					);
			
			//5 animations
			sprites = new ArrayList<BufferedImage[]>();
			
			for (int i = 0; i < 5; i++) {
				
				BufferedImage[] bi = 
						new BufferedImage[numFrames[i]];
				
				for (int j = 0; j < numFrames[i]; j++) {
					
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height);
				}
				
				sprites.add(bi);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public long getFire() {
		return fire;
	}
	
	public long maxFire() {
		return maxFire;
	}
	
	public void setFiring() {
		firing = true;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// fireballs
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}

			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
			
		}
	}
	
	public void hit(int damage) {
		if(flinching) return;
		
		health -= damage;
		
		if(health < 0) health = 0;
		
		if(health == 0) dead = true;
		
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition() {
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		//attacking midair
		if ( (currentAction == FIREBALL) && 
				!(jumping || falling) ) {
			dx = 0;
			
		}
		
		// jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if(falling) {

			if(dy > 0 ) dy += fallSpeed * 1;
			else dy += fallSpeed;

			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;

			if(dy > maxFallSpeed) dy = maxFallSpeed;

		}
	}
	
	public void update() {
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//has attack stopped?
		if (currentAction == FIREBALL) {
			if (animation.hasPlayedOnce()) firing = false;
		}
		
		//firballll
		fire += 1;
		
		if (fire > maxFire) fire = maxFire;
		
		if (firing && currentAction != FIREBALL) {
			if (fire > fireCost) {
				fire -= fireCost; //inifinite FIRE
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
		
		//update fireBall
		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			
			if (fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		//check done flinching
		if (flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer)/1000000;
			
			if (elapsed > 1000) {
				flinching = false;
			}
		}
		
		//set animation
		if (firing) {
			if (currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		
		animation.update();
		
		//set direction
		if (currentAction != FIREBALL) {
			if (right) facingRight = true;
			if (left) facingRight = false;
		}
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		//draw fireballs
		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		//draw player
		if (flinching) {
			long elapsed = 
					(System.nanoTime()-flinchTimer)/1000000;
			
			if (elapsed/100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
	}

}




























