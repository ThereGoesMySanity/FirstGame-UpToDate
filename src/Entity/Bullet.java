package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.*;

public class Bullet extends MapObject{
	public Animation animation;
	private boolean hit;
	public static String ninjaStar1 = "/Sprites/ThrowingStar1grey.png";
	public static String ninjaStar2 = "/Sprites/ThrowingStar2grey.png";
	private boolean remove;
	private int timer = 0;
	private BufferedImage[] sprites;
	private boolean[] items;
	private int scale;
	public Bullet(TileMap tm, boolean right, boolean[] currentItems) {
		super(tm);
		this.items = currentItems;
		scale = 1;
		if(items[Item.DOUBLE]){
			scale = 2;
		}
		moveSpeed = 6;
		if(right)dx = moveSpeed;
		else dx = -moveSpeed;
		if(currentItems[Item.THREEWAY_BOTTOM]){
			dy = 2;
		}else if(currentItems[Item.THREEWAY_TOP]){
			dy = -2;
		}else{
			dy = 0;
		}
		width = 16*scale;
		height = 16*scale;
		cwidth = 12*scale;
		cheight = 12*scale;
		
		try{
			animation = new Animation();
			if(ninjaStar1 == "/Sprites/Orange-tabby-cat-icon.png"){
				sprites = new BufferedImage[4];
				sprites[0] = ImageIO.read(getClass().getResourceAsStream(ninjaStar1));
				for(int i = 1; i<4; i++){
					sprites[i] = ImageIO.read(getClass().getResourceAsStream
							("/Sprites/Orange-tabby-cat-icon"+ i +".png"));
				}
				animation.setFrames(sprites);
				animation.setDelay(50);
			}else{
				sprites = new BufferedImage[2];
				sprites[0] = ImageIO.read(getClass().getResourceAsStream(ninjaStar1));
				sprites[1] = ImageIO.read(getClass().getResourceAsStream(ninjaStar2));
			
				
				animation.setFrames(sprites);
				animation.setDelay(15);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	public void setHit(){
		if(hit)return;
		hit = true;
		dx = 0;
		dy = 0;
	}
	public boolean shouldRemove(){return remove;}
	public void update(){
		if((dx == 0||(dy == 0
				&&(items[Item.THREEWAY_BOTTOM]
						||items[Item.THREEWAY_TOP])))
				&& !hit){
			setHit();
		}
		checkTileMapCollision();
		
		
		if(items[Item.WIGGLE]){
			ytemp += (int)(-5*Math.sin(timer/2));
		}
		timer++;
		setPosition(xtemp, ytemp);
		animation.update();
		if(hit){
			remove = true;
		}
	}
	public void draw(Graphics2D g){
		setMapPosition();
		g.drawImage(animation.getImage(), 
				(int)(x+xmap-width/2)*2/GamePanel.SCALE, 
				(int)(y+ymap-height/2)*2/GamePanel.SCALE,
				width*2/GamePanel.SCALE,
				height*2/GamePanel.SCALE,
				null);
	}
	public static void setStar(String s) {
		if(s == "cat"){
			ninjaStar1 = "/Sprites/Orange-tabby-cat-icon.png";
			ninjaStar2 = "fuck your anus";
		}else{
			ninjaStar1 = "/Sprites/ThrowingStar1" + s + ".png";
			ninjaStar2 = "/Sprites/ThrowingStar2" + s + ".png";
		}
		System.out.println(ninjaStar1+ninjaStar2);
	}
}







