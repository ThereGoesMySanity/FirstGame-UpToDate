package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Item extends MapObject{
	public static final int THREEWAY_TOP = 0;
	public static final int THREEWAY = 1;
	public static final int THREEWAY_BOTTOM = 2;
	public static final int TWENTY_TWENTY = 3;
	public static final int WIGGLE = 4;
	public static final int DOUBLE = 5;
	public static final int NUM_ITEMS = 6;
	public int item;
	private BufferedImage[] sprites;
	private long elapsed;
	private boolean hoverUp;
	private boolean hit;
	public Item(TileMap tm, int item) {
		super(tm);
		this.item = item;
		width = height = cwidth = cheight = 16;
		try{
			BufferedImage threeway_img = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/threeway.png"));
			BufferedImage twenty_twenty_img = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/twenty_twenty.png"));
			BufferedImage wiggle_img = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/wiggle.png"));
			BufferedImage double_img = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/double.png"));
			BufferedImage[] spritesheet = {
					null, threeway_img, 
					null, twenty_twenty_img, 
						  wiggle_img, 
						  double_img
			};
			sprites = new BufferedImage[]{
					spritesheet[item], spritesheet[item]
			};
		}catch(Exception e){e.printStackTrace();}
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(1000);
		right = facingRight = true;
		left = false;
		elapsed = System.nanoTime();
	}
	public void update(){
		if(System.nanoTime() - elapsed>500000000){
			if(!hoverUp){
				hoverUp = true;
				y -= 2;
				
			}else{
				hoverUp = false;
				y += 2;
				
			}
			elapsed = System.nanoTime();
		}
		if(hit){
		}
	}
	public void draw(Graphics2D g){
		setMapPosition();
		super.draw(g);
	}
	public int getItem() {
		return item;
	}
}
