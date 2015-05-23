package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx*2/GamePanel.SCALE;
		this.dy = dy*2/GamePanel.SCALE;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		
		if(x < 0) {
			g.drawImage(
					image,
					(int)x%GamePanel.WIDTH,
					(int)y,
					GamePanel.WIDTH,
					GamePanel.HEIGHT,
					null
				);
				g.drawImage(
						image,
						(int)x%GamePanel.WIDTH+GamePanel.WIDTH,
						(int)y,
						GamePanel.WIDTH,
						GamePanel.HEIGHT,
						null
				);
		}
		if(x > 0) {
			g.drawImage(
				image,
				(int)x%GamePanel.WIDTH,
				(int)y,
				GamePanel.WIDTH,
				GamePanel.HEIGHT,
				null
			);
			g.drawImage(
					image,
					(int)x%GamePanel.WIDTH-GamePanel.WIDTH,
					(int)y,
					GamePanel.WIDTH,
					GamePanel.HEIGHT,
					null
			);
			
		}
	}
	
}