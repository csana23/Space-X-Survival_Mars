package Main;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Space(X) Survival: Mars");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLayout(new BorderLayout());
		window.pack();
		window.setVisible(true);
		
	}

}
