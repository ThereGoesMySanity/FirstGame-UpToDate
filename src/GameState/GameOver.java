package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.Background;

public class GameOver extends GameState {
	private Background bg;
	private int currentChoice = 0;
	private Font font;
	private BufferedImage image;
	
	private String[] options = {
			"Start Over",
			"Menu"
	};
	public GameOver(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	public void init() {
		
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/ThrowingStar1grey.png"));
			bg = new Background("/Background/menubgmod.png", 0);
		}catch(Exception e){e.printStackTrace();}
		font = new Font("Fixedsys", Font.TRUETYPE_FONT, 48/GamePanel.SCALE);
	}

	public void update() {}

	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(Color.RED);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
        int x = ((GamePanel.WIDTH - fm.stringWidth("Game Over")) / 2);
		g.drawString("Game Over", x, 64/GamePanel.SCALE);
		for(int i = 0; i<options.length; i++){
			x = ((GamePanel.WIDTH - fm.stringWidth(options[i])) / 2);
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.WHITE);
			}
			
			g.drawString(options[i], x, 200/GamePanel.SCALE+(64/GamePanel.SCALE*i));
			g.drawImage(image, 280/GamePanel.SCALE-(fm.stringWidth(options[currentChoice])/2), 
					164/GamePanel.SCALE+currentChoice*64/GamePanel.SCALE,
					image.getWidth()*2/GamePanel.SCALE, image.getHeight()*2/GamePanel.SCALE, null);
		}
	}

	public void keyPressed(int k) {
		switch(k){
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_UP:
			currentChoice--;
			if (currentChoice == -1){
				currentChoice = options.length - 1;
			}
			break;
		case KeyEvent.VK_DOWN:
			currentChoice++;
			if (currentChoice == options.length){
				currentChoice = 0;
			}
			break;
		}
	}

	public void keyReleased(int k) {

	}
	public void select(){
		switch(currentChoice){
		case 0:
			gsm.setState(GameStateManager.LEVEL1STATE);
			break;
		case 1:
			gsm.setState(GameStateManager.MENUSTATE);
			break;
		}
	}

}
