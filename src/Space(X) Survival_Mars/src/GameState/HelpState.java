package GameState;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;

public class HelpState extends GameState {
	
	public HelpState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override
	public void init() {}

	@Override
	public void update() {}
	
	//modified drawCenteredIterationString
	private void drawCenteredIterationString(String[] st, int w, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		
		for (int i = 0; i < st.length; i++) {
			
			int x = (w - fm.stringWidth(st[i])) / 2;
			int y = 80 + i*15;
			
			g.drawString(st[i], x, y);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setColor(Color.RED);
		
		String[] help = {"Move around with arrows", 
				"Select level or option: press Enter", 
				"To get back to the menu: press Escape", 
				"Shoot by pressing Space", 
				"Made by: KZEADR, 2018",
				"\n",
				"Ohh... and you're invincible :)"};
		
		drawCenteredIterationString(help, GamePanel.WIDTH, g);
	}

	@Override
	public void keyPressed(int k) {
		//changing gamestate
		if (k == KeyEvent.VK_ESCAPE) {
			this.gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	@Override
	public void keyReleased(int k) {}
	
	

}
