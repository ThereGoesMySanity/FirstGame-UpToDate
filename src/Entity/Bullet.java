package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.*;

public class Bullet extends MapObject{
	public Animation animation;
	private boolean hit;
	public static String ninjaStar1 = "/Sprites/ThrowingStar1grey.png";
	public static String ninjaStar2 = "/Sprites/ThrowingStar2grey.png";
	private boolean remove;
	private int timer = 0;
	private BufferedImage[] sprites;
	private int item;
	public Bullet(TileMap tm, boolean right, int item) {
		super(tm);
		this.item = item;
		moveSpeed = 6;
		if(right)dx = moveSpeed;
		else dx = -moveSpeed;
		if(item == Item.THREEWAY_BOTTOM){
			dy = 2;
		}
		if(item == Item.THREEWAY_TOP){
			dy = -2;
		}
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;
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
		if((dx == 0||(dy == 0&&(item == Item.THREEWAY_BOTTOM||item == Item.THREEWAY_TOP))) && !hit){
			setHit();
		}
		checkTileMapCollision();
		
		
		switch (item){
			case Item.WIGGLE:
				ytemp += (int)(-5*Math.sin(timer/2));
				break;
		}
		timer++;
		setPosition(xtemp, ytemp);
		animation.update();
		if(hit){
			remove = true;
		}
		System.out.println(y);
	}
	public void draw(Graphics2D g){
		setMapPosition();
		g.drawImage(animation.getImage(), (int)(x+xmap-width/2), (int)(y+ymap-height/2), null);
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







