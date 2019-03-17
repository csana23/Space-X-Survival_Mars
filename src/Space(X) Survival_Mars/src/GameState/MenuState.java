package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	
	private String title = "Space(X) Survival: Mars";
	
	private String[] options = {"Level 1 - Easy", "Level 2 - Medium",
			"Level 3 - Hard", "Help", "Quit"};
	
	private Color titleColor;
	
	private Font titleFont;
	private Font font;
	
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/backgrounds/rsz_mars-planet-surface-with-dust-blowing_rtsmo0wm__f0000.png", 1);
			
			titleColor = Color.GREEN;
			titleFont = new Font("Courier New", Font.BOLD, 14);
			
			
			font = new Font("Courier New", Font.BOLD, 12);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {}

	@Override
	public void update() {
		bg.update();
	}
	
	public void drawCenteredString(String s, int w, int h, Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		
		int x = (w - fm.stringWidth(s)) / 2;
		int y = 40;

		g.drawString(s, x, y);
	}
	
	public void drawCenteredIterationString(String[] st, int w, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		
		for (int i = 0; i < st.length; i++) {
			
			if (i == currentChoice) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.BLACK);
			}
			
			int x = (w - fm.stringWidth(st[i])) / 2;
			int y = 105 + i*15;
			
			g.drawString(st[i], x, y);
		}
		
		 
	}
	
	@Override
	public void draw(Graphics2D g) {
		//draw background
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);

		//kiveszem a *SCALE-t
		drawCenteredString(title, (GamePanel.WIDTH), (GamePanel.HEIGHT), g);
		
		//draw menu options
		g.setFont(font);
		
		//itt is kiveszem a SCALE-t
		drawCenteredIterationString(options, (GamePanel.WIDTH), g);	
	}
	
	public void select() {
		if (currentChoice == 0) {
			//Level 1
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		
		if (currentChoice == 1) {
			//Level 2
			gsm.setState(GameStateManager.LEVEL2STATE);
		}
		
		if (currentChoice == 2) {
			//Level 3
			gsm.setState(GameStateManager.LEVEL3STATE);
		}
		
		if (currentChoice == 3) {
			//Help
			gsm.setState(GameStateManager.HELPSTATE);
		}
		
		if (currentChoice == 4) {
			//Quit
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			
			if (currentChoice == -1) {
				currentChoice = options.length-1;
			}
		}
		
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
		
	}
	
	public void keyReleased(int k) {}

	
	

}
