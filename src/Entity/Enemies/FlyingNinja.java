package Entity.Enemies;

import java.awt.Graphics2D;

import Entity.Animation;
import Entity.Enemy;
import GameState.Level1State;
import TileMap.TileMap;

public class FlyingNinja extends Enemy {
	
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
		
		
		animation = new Animation();
		animation.setFrames(Level1State.flyingNinjaSprites);
		animation.setDelay(300);
		
		right = false;
		left = true;
		facingRight = false;
		System.nanoTime();
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
			animation.setFrames(Level1State.flyingNinjaSprites_dmg);
			healthCounter = 1;
			prevHealth = health;
		}else if(healthCounter>0){
			healthCounter++;
			if(healthCounter == 11){
				healthCounter = 0;
				animation.setFrames(Level1State.flyingNinjaSprites);
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
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
}







