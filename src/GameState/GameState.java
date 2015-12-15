package GameState;

import java.awt.image.BufferedImage;

public abstract class GameState {
	protected GameStateManager gsm;
	public BufferedImage[] bulletSprites;
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
}
