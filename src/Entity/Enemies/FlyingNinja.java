package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class FlyingNinja extends Enemy {
	
	private BufferedImage[] sprites;
	private long elapsed;
	private BufferedImage[] sprites_dmg;
	private int prevHealth;
	private int healthCounter;
	
	public FlyingNinja(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 2;
		maxSpeed = 2;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 15;
		height = 22;
		cwidth = 12;
		cheight = 20;
		
		health = prevHealth = 10;
		healthCounter = 0;
		damage = 5;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Ninja_clothes.png"
				)
			);
			BufferedImage spritesheet_dmg = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Ninja_clothes_dmg.png"
					)
				);
			sprites = new BufferedImage[2];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet;
			}
			sprites_dmg = new BufferedImage[2];
			for(int i = 0; i < sprites.length; i++) {
				sprites_dmg[i] = spritesheet_dmg;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = false;
		left = true;
		facingRight = false;
		elapsed = System.nanoTime();
	}
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(prevHealth != health){
			animation.setFrames(sprites_dmg);
			healthCounter = 1;
			prevHealth = health;
		}else if(healthCounter>0){
			healthCounter++;
			if(healthCounter == 11){
				healthCounter = 0;
				animation.setFrames(sprites);
			}
		}
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		// update animation
		animation.update();
		if((System.nanoTime()-(int)(elapsed/1000000))%3000 == 0){
			
		}
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
}







