package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import GameState.Level1State;
import Main.GamePanel;

public class Explosion {
	
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	public static int width;
	public static int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	public Explosion(int x, int y) {
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		sprites = Level1State.explosionSprites;
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
		
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void draw(Graphics2D g) {
		if(GamePanel.ninjaSlayer){
			g.drawImage(
					animation.getImage(),
					(x + xmap - width)*2/GamePanel.SCALE,
					(y + ymap - height)*2/GamePanel.SCALE,
					width*4/GamePanel.SCALE,
					height*4/GamePanel.SCALE,
					null);
		}else{
			g.drawImage(
				animation.getImage(),
				(x + xmap - width / 2)*2/GamePanel.SCALE,
				(y + ymap - height / 2)*2/GamePanel.SCALE,
				width*2/GamePanel.SCALE,
				height*2/GamePanel.SCALE,
				null);
		}
	}
}

















