package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Player;
import Entity.EnemyTroll.Troll;
import Main.GamePanel;
import TileMap.*;

public class Level2State extends GameState {
	
	private TileMap2 tileMap;
	
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	
	public Level2State(GameStateManager gsm) {
		this.gsm = gsm;
		
		init();
	}

	@Override
	public void init() {
		
		tileMap = new TileMap2(15);
		tileMap.loadTiles("/tilesets/cobble_blood1.png");
		tileMap.loadMap("/maps/level2.txt");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/backgrounds/background_2.png", 0.0);
		
		player = new Player(tileMap);
		player.setPosition(40, 40);
		
		populateEnemies();

	}
	
	private void populateEnemies() {
		enemies = new ArrayList<>();
		
		Troll t;
		
		Point[] points = new Point[] {
				new Point(200, 100),
				new Point(400,100),
				new Point(500,100),
				new Point(800,100),
				new Point(1200,100)

		};
		
		for (int i = 0; i < points.length; i++) {
			t = new Troll(tileMap);
			
			t.setPosition(points[i].x, points[i].y);
			
			enemies.add(t);
		}
	}

	@Override
	public void update() {
		//update player
		player.update();
		
		tileMap.setPosition(
				GamePanel.WIDTH/2-player.getX(),
				GamePanel.HEIGHT/2-player.getY()
				);
		
		//setBackground
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		//attack enemies
		player.checkAttack(enemies);

		//update all enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();

			if (e.isDead()) {
				enemies.remove(i);
				i--;
			}
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		try {
			//draw background
			bg.draw(g);
			
			//draw tilemap
			tileMap.draw(g);
			
			//draw player
			player.draw(g);
			
			//draw enemies
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).draw(g);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT) player.setLeft(true);
		if (k == KeyEvent.VK_RIGHT) player.setRight(true);
		if (k == KeyEvent.VK_DOWN) player.setDown(true);
		if (k == KeyEvent.VK_UP) player.setJumping(true);
		if (k == KeyEvent.VK_SPACE) player.setFiring();
		
		//changing gamestate
		if (k == KeyEvent.VK_ESCAPE) {
			this.gsm.setState(GameStateManager.MENUSTATE);
		}
		
	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT) player.setLeft(false);
		if (k == KeyEvent.VK_RIGHT) player.setRight(false);
		if (k == KeyEvent.VK_DOWN) player.setDown(false);
		if (k == KeyEvent.VK_UP) player.setJumping(false);
		
	}

	

}
