package GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.Background;

public class MenuState extends GameState{

	private Background bg;
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Options",
		"Help",
		"Quit"
	};
	private Color titleColor;
	private Font titleFont;
	private BufferedImage image;
	private BufferedImage loading;
	private BufferedImage logo;
	private AudioPlayer bgMusic;
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		try{
			logo = ImageIO.read(getClass().getResourceAsStream("/GUI/NS_Logo.png"));
			loading = ImageIO.read(getClass().getResourceAsStream("/Background/loading.png"));
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/ThrowingStar1grey.png"));
			bg = new Background("/Background/menubgmod.png", 1);
			bg.setVector(5, 0);
			bgMusic = new AudioPlayer("/Music/BACK IN BLACK - BOOM BOOM SATELITES.wav");
			bgMusic.setVolume(-10);
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Fixedsys", Font.TRUETYPE_FONT, 56/GamePanel.SCALE);
		}catch(Exception e){e.printStackTrace();}
		
	}
	public void init(){}
	public void update(){
		bg.update();
		if(GamePanel.ninjaSlayer&&!bgMusic.isRunning()){
			bgMusic.play();
		}
	}
	public void draw(Graphics2D g){
		
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		if(GamePanel.ninjaSlayer){
			g.drawImage(logo, 100, 64, 440, 140, null);
		}else{
			FontMetrics fm = g.getFontMetrics();
			int x = ((GamePanel.WIDTH - fm.stringWidth("Ninja Game")) / 2);
			int y = fm.getHeight();
			g.drawString("Ninja Game", x, y+32);
		}
		g.setFont(titleFont);
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 360/GamePanel.SCALE, 280/GamePanel.SCALE + i*60/GamePanel.SCALE);
			g.drawImage(image, GamePanel.WIDTH/2, (244/GamePanel.SCALE)+currentChoice*60/GamePanel.SCALE,
					image.getWidth()*2/GamePanel.SCALE, image.getHeight()*2/GamePanel.SCALE, null);
		}
	}
	private void select(){
		if(GamePanel.ninjaSlayer){
			bgMusic.stop();
		}
		switch(currentChoice){
		case 0:
			GamePanel.drawImage(loading);
			gsm.setState(GameStateManager.LEVEL1STATE);
			break;
		case 1:
			gsm.setState(GameStateManager.OPTIONSSTATE);
			break;
		case 2:
			//help
			break;
		case 3:
			System.exit(0);
		}
	}
	
	public void keyPressed(int k){
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
	public void keyReleased(int k){}
}
