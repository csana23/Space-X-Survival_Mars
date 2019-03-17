package Entity.EnemyTroll;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap2;

public class Troll extends Enemy {
	
	private BufferedImage[] sprites;
	
	public Troll(TileMap2 tm) {
		super(tm);
		
		moveSpeed = 0.6;
		maxSpeed = 0.6;
		fallSpeed = 0.5;
		maxFallSpeed = 10;
		
		width = 32;
		height = 32;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 2;
		damage = 1;
		
		//sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/entities/enemies/troll.gif"
							)	
					);
			
			sprites = new BufferedImage[1];
			
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
						i*width,
						0,
						width,
						height
						);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
		
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

		// falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			
			if (elapsed > 400) {
				flinching = false;
			}
		}
		
		//hit wall
		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = true;
			
		} else if (left && dx == 0) { 
			right = true;
			left = false;
			facingRight = false;
		}
		
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}

}






















