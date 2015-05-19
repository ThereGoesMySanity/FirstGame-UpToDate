package Entity;
import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
public class Player extends MapObject {
	public static boolean glitch;
	public boolean sanic;
	private boolean sanicd;
	private HashMap<String, AudioPlayer> sfx;

	public void setSanic(){
		sanic = true;
		if(!sanicd){
			try{
				BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Sanic.png"));
				sprites = new ArrayList<BufferedImage[]>();
				for(int i=0; i<4; i++){
					BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for(int j = 0; j<numFrames[i]; j++){
						if(i!=3){
							bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
						}else{
							bi[j] = spritesheet.getSubimage(j*2*width, i*height, width, height);
						}			
					}
					sprites.add(bi);
				}
			}catch(Exception e){e.printStackTrace();}
			sanicd = true;
		}
	}
	public boolean isSanic(){
		return sanic;
	}
	public static void setGlitch(boolean g) {
		glitch = g;
	}
	public static boolean isGlitch(){
		return glitch;
	}
	private int maxHealth;
	private int bullet;
	private int maxBullets;
	private boolean dead;
	private boolean flinching;
	private long flinchTime;
	private ArrayList<Bullet> bullets;
	private boolean firing;
	private int bulletCost;
	private int bulletDamage;
	public boolean played;
	private int currentItem;
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {1, 3, 4, 4};
	private double sanicCharge;
	private boolean sanicDischarge;
	private long modTime;
	private boolean prevFiring;
	private boolean sanicJumpAnim;
	private static final int IDLE = 0;
	private static final int RUNNING = 1;
	private static final int JUMPING = 2;
	private static final int SHOOTING = 3;
	public Player(TileMap tm){
		super(tm);
		width = 22;
		height = 32;
		cwidth = 14;
		cheight = 26;
		moveSpeed = 0.6;
		maxSpeed = 2;
		stopSpeed = 1;
		jumpStart = -4.8;
		fallSpeed = 0.15;
		maxFallSpeed = 999;
		stopJumpSpeed = 3;
		health = maxHealth = 25;
		bullet = maxBullets = 1997;
		bulletCost = 50;
		bulletDamage = 1;
		currentItem = Item.DOUBLE;
		bullets = new ArrayList<Bullet>();
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("sanicHit", new AudioPlayer("/SFX/sonic017.wav"));
		sfx.put("sanicSlow", new AudioPlayer("/SFX/sonic005.wav"));
		sfx.put("sanicStep", new AudioPlayer("/SFX/sonic006.wav"));
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player.png"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i=0; i<4; i++){
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j<numFrames[i]; j++){
					if(i!=3){
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}else{
						bi[j] = spritesheet.getSubimage(j*2*width, i*height, width, height);
					}

				}
				sprites.add(bi);
			}

		}catch(Exception e){e.printStackTrace();}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(-1);
	}
	public int getMaxBullets() {
		return maxBullets;
	}
	public int getHealth() {
		return health;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public int getBullet() {
		return bullet;
	}
	public void setFiring(boolean b){
		firing = b;
	}
	public void update(){
		getNextPosition();
		sanicJumpAnim = false;
		modTime++;
		if(y > 300){
			dead = true;
		}
		if(currentAction == SHOOTING && animation.hasPlayedOnce())firing = false;
		bullet += 7;
		if(bullet>maxBullets)bullet = maxBullets;

		if (firing){
			if(sanic&&!sanicDischarge){
				if(bullet<bulletCost){
					sanicDischarge = true;
					stopSpeed = 0.2;
					firing = false;
					sanicJumpAnim = true;
					if(!facingRight){
						dx -= sanicCharge;
					}else{
						dx += sanicCharge;
					}
					sanicCharge = -0.2;
				}
				if(!prevFiring){
					sanicJumpAnim = true;
				}
				sanicCharge += 0.2;
			}
			if(!prevFiring){
				prevFiring = true;
			}
			if(bullet>bulletCost&&modTime%2==0){
				bullet -= bulletCost;
				if(currentItem == Item.THREEWAY){
					bullet -= bulletCost;
				}
				if(!sanic){
					Bullet b = new Bullet(tileMap, facingRight, currentItem);
					b.setPosition(x, y);
					bullets.add(b);
					if(currentItem == Item.THREEWAY){
						Bullet c = new Bullet(tileMap, facingRight, Item.THREEWAY_TOP);
						c.setPosition(x, y);
						bullets.add(c);
						Bullet d = new Bullet(tileMap, facingRight, Item.THREEWAY_BOTTOM);
						d.setPosition(x, y);
						bullets.add(d);
					}
					if(currentItem == Item.DOUBLE){
						System.out.println("yes" + x + " " +(y + 3));
						Bullet e = new Bullet(tileMap, facingRight, 10);
						e.setPosition(x, y+3);
					}
				}
			}
		}else if(prevFiring){
			prevFiring=false;
			if(sanic){
				sanicDischarge = true;
				stopSpeed = 0.2;
				firing = false;
				sanicJumpAnim = true;
				if(!facingRight){
					dx -= sanicCharge;
				}else{
					dx += sanicCharge;
				}
				sanicCharge = 0;
			}
		}
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		if(dy!=0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(60);
				width = 22;
			}
		}else if((left||right)&&sanicCharge == 0){
			if(currentAction != RUNNING){
				if(sanic){
					int sanicRand = (int) (Math.random()*8);
					if(sanicRand == 3)sfx.get("sanicSlow").play();
					if(sanicRand == 7)sfx.get("sanicStep").play();
				}
				currentAction = RUNNING;
				animation.setFrames(sprites.get(RUNNING));
				animation.setDelay(40);
				width = 22;
			}
		}else if(currentAction !=IDLE&&sanicCharge == 0){
			currentAction = IDLE;
			animation.setFrames(sprites.get(IDLE));
			animation.setDelay(-1);
			width = 22;
		}
		animation.update();

		if(currentAction != SHOOTING){
			if(right)facingRight = true;
			if(left)facingRight = false;
		}
		if(flinching) {
			flinchTime += 1;
			if(flinchTime>60){
				flinching = false;
				System.out.println("done");
				flinchTime = 0;
			}
		}
		if(sanicDischarge){
			if(dx==0){
				sanicDischarge = false;
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(-1);
				width = 22;
				stopSpeed = 1;
			}
		}
		if(sanicJumpAnim){
			animation.setFrames(sprites.get(JUMPING));
			animation.setDelay(30);
		}
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}


		// jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if(falling) {
			
			dy += fallSpeed;

			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;

			if(dy > maxFallSpeed) dy = maxFallSpeed;

		}
		if(sanicCharge > 0 && firing){
			dx = 0;
			dy /= 1.1;
		}
	}
	public void draw(Graphics2D g){
		setMapPosition();
		for(int i=0; i<bullets.size(); i++){
			bullets.get(i).draw(g);
		}
		if(flinchTime%4 == 0||flinchTime%4==1){
			super.draw(g);
		}
		if(sanicCharge>0){
			g.drawString(String.valueOf((int)(sanicCharge)), 10, 10);
		}
	}
	public void checkAttack(ArrayList<Enemy> enemies){
		for(int i = 0; i<enemies.size(); i++){
			Enemy e = enemies.get(i);
			for(int j = 0; j<bullets.size(); j++){
				if(bullets.get(j).intersects(e)){
					e.hit(bulletDamage);
					bullets.get(j).setHit();
					System.out.println("hit, " +e.getHealth());
				}
			}
			if(intersects(e)){
				if(!((jumping||falling)&&sanic)){
					hit(e.getDamage());
					if(sanic){
						sfx.get("sanicHit").play();
					}
				}else if(sanic&&(jumping||falling||sanicDischarge)){
					e.hit(100);
				}
			}
		}
	}
	public void hit(int damage){
		if(flinching)return;
		health-=damage;
		if(health<=0){
			dead = true;
			health = 0;
		}
		flinching = true;
	}
	public boolean getStairs(){
		return blStair||brStair;
	}
	public boolean isDead(){
		return dead;
	}
	public int getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}
}
