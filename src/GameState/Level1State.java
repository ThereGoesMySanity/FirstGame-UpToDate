package GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.FontMetrics;

import Audio.JukeBox;
import Entity.Bullet;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Item;
import Entity.Player;
import Entity.Enemies.FlyingNinja;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
public class Level1State extends GameState{
	private TileMap tileMap;
	private Player player;
	private ArrayList<Enemy> enemies;
	public static ArrayList<Item> items;
	private ArrayList<Explosion> explosions;
	public static BufferedImage[] explosionSprites;
	private HUD hud;
	public FlyingNinja bob;
	private Background background;
	private boolean s=false;
	private boolean a=false;
	private boolean n=false;
	private boolean i=false;
	private boolean c=false;
	private boolean pause;
	private Font pauseFont;
	private int currentChoice;
	private String[] options = new String[]{
			"Resume",
			"Restart",
			"Menu",
			"Quit"
	};
	public static BufferedImage[] flyingNinjaSprites_dmg;
	public static BufferedImage[] flyingNinjaSprites;
	public static BufferedImage[] bulletSprites;
	public Level1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	public void init(){
		background = new Background("/Background/menubgmod.png", .5);
		tileMap = new TileMap(16);
		player = new Player(tileMap);
		hud = new HUD(player);
		items = new ArrayList<Item>();
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		loadSprites();
		populateEnemies();
		populateItems();
		background.setVector(-5, 0);
		tileMap.loadTiles("/Tilesets/lvl1tilesout.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		player.setPosition(100, 0);
		gsm.setBTTime(30);
		if(GamePanel.ninjaSlayer){
			JukeBox.load("/Music/Ninja Slayer.mp3", "bgMusic");
		}else{
			JukeBox.load("/Music/song.wav", "bgMusic");
		}
		JukeBox.loop("bgMusic");
		JukeBox.setVolume("bgMusic", -10);
		JukeBox.load("/Music/Green_Hill_Zone1.mp3", "sanic");
		JukeBox.setVolume("sanic", -10);
		pauseFont = new Font("Fixedsys", Font.TRUETYPE_FONT, 56/GamePanel.SCALE);
	}
	private void pause() {
		pause = true;
	}
	public void loadSprites(){
		try {
			if(GamePanel.ninjaSlayer){
				explosionSprites = new BufferedImage[18];
				for(int i = 0; i < 18; i++){
					explosionSprites[i] = ImageIO.read(getClass().getResourceAsStream("/Sprites/frame_" + String.format("%06d", i+1) + ".png"));
				}
			}else{
				BufferedImage spritesheet = ImageIO.read(
						getClass().getResourceAsStream(
								"/Sprites/explosion.gif"));

				explosionSprites = new BufferedImage[6];
				for(int i = 0; i < explosionSprites.length; i++) {
					explosionSprites[i] = spritesheet.getSubimage(
							i * Explosion.width,
							0,
							Explosion.width,
							Explosion.height
							);
				}
			}
			if(Bullet.ninjaStar1 == "/Sprites/Orange-tabby-cat-icon.png"){
				bulletSprites = new BufferedImage[4];
				bulletSprites[0] = ImageIO.read(getClass().getResourceAsStream(Bullet.ninjaStar1));
				for(int i = 1; i<4; i++){
					bulletSprites[i] = ImageIO.read(getClass().getResourceAsStream
							("/Sprites/Orange-tabby-cat-icon"+ i +".png"));
				}
				Bullet.cat = true;
			}else{
				bulletSprites = new BufferedImage[2];
				bulletSprites[0] = ImageIO.read(getClass().getResourceAsStream(Bullet.ninjaStar1));
				bulletSprites[1] = ImageIO.read(getClass().getResourceAsStream(Bullet.ninjaStar2));
			}
			flyingNinjaSprites = new BufferedImage[2];
			for(int i = 0; i < flyingNinjaSprites.length; i++) {
				flyingNinjaSprites[i] = ImageIO.read(
						getClass().getResourceAsStream(
								"/Sprites/Ninja_clothes.png")
						);
			}
			flyingNinjaSprites_dmg = new BufferedImage[2];
			for(int i = 0; i < flyingNinjaSprites_dmg.length; i++) {
				flyingNinjaSprites_dmg[i] = ImageIO.read(
						getClass().getResourceAsStream(
								"/Sprites/Ninja_clothes_dmg.png"
								)
						);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	private int randEnemy(int xupper, int xlower){
		return (int)(Math.random()*(xupper-xlower))+xlower;
	}
	private void populateEnemies() {
		FlyingNinja bob;
		Point[] points = new Point[] {
				new Point(400, randEnemy(120, 170)),
				new Point(860, randEnemy(120, 180)),
				new Point(1000, randEnemy(120, 180)),
				new Point(1060, randEnemy(120, 180)),
				new Point(1260, randEnemy(120, 180)),
				new Point(1360, randEnemy(90, 180)),
				new Point(1460, randEnemy(90, 180)),
				new Point(1490, randEnemy(90, 180)),
				new Point(1525, randEnemy(90, 180)),
				new Point(1680, randEnemy(90, 180)),
				new Point(1900, randEnemy(90, 180)),
				new Point(2000, randEnemy(120, 180)),
				new Point(2050, randEnemy(120, 180)),
				new Point(2120, randEnemy(120, 180)),
				new Point(2440, randEnemy(120, 180)),
				new Point(2800, randEnemy(120, 180))
		};
		for(int i = 0; i < points.length; i++) {
			bob = new FlyingNinja(tileMap);
			bob.setPosition(points[i].x, points[i].y);
			enemies.add(bob);
		}

	}
	private void populateItems(){
		items = new ArrayList<Item>();
		Item james;
		Point[] points = new Point[]{
				new Point(300, 180),
				new Point(600, 180),
				new Point(930, 50),
				new Point(1800, 180)
		};
		int [] itemses = new int[]{
				Item.THREEWAY, Item.TWENTY_TWENTY, Item.WIGGLE, Item.DOUBLE
		};
		for(int i = 0; i < points.length; i++){
			james = new Item(tileMap, itemses[i]);
			james.setPosition(points[i].x, points[i].y);
			items.add(james);
		}
	}
	private void gottaGoFast(){
		tileMap.loadTiles("/Tilesets/lvl1tilesout_sanic.png");
		player.maxSpeed = 10;
		player.moveSpeed = 1;
		player.stopSpeed = 3;
		JukeBox.stop("bgMusic");
		JukeBox.play("sanic");
	}
	public void update(){
		if(pause)return;
		if(s&&a&&n&&i&&c&&!GamePanel.ninjaSlayer){
			gottaGoFast();
			player.setSanic();
		}
		player.update();
		if(player.isDead()){
			JukeBox.stop("bgMusic");
			JukeBox.stop("sanic");
			gsm.setState(GameStateManager.GAMEOVER);
		}
		tileMap.setPosition(

				player.getx(),
				player.gety());
		background.update();
		player.checkAttack(enemies);
		player.checkItem(items);
		for(int i = 0; i < items.size(); i++){
			Item it =  items.get(i);
			it.update();
		}
		for(int i=0; i<enemies.size();i++){
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){
				enemies.remove(i);
				explosions.add(new Explosion(
						e.getx(), 
						e.gety()
						));
				i--;
			}
		}
		for(int i = 0; i<explosions.size(); i++){
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()){
				explosions.remove(i);
				i--;
			}
		}
		if(player.getStairs()){
			toMenu();
		}
	}
	public void draw(Graphics2D g) {
		background.draw(g);
		tileMap.draw(g);
		player.draw(g);
		for(Item i : items){
			i.draw(g);
		}
		for(Enemy i : enemies){
			i.draw(g);
		}
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).setMapPosition(tileMap.getx(), tileMap.gety());
			explosions.get(i).draw(g);
		}
		hud.draw(g);
		if(pause){
			FontMetrics fm = g.getFontMetrics();
			g.setFont(pauseFont);
			g.setColor(Color.RED);
			g.drawString("Paused", (GamePanel.WIDTH-fm.stringWidth("Paused"))/3, 64);
			for(int i = 0; i < options.length; i++){
				g.setColor(Color.LIGHT_GRAY);
				if(currentChoice == i){
					g.setColor(Color.GRAY);
				}
				g.drawString(options[i], 300, 200 + 60*i);
			}
		}
	}
	public void keyPressed(int k) {
		if(pause){
			if(k == KeyEvent.VK_UP){
				currentChoice--;
				if (currentChoice == -1){
					currentChoice = options.length - 1;
				}
			}
			if(k == KeyEvent.VK_DOWN){
				currentChoice++;
				if (currentChoice == options.length){
					currentChoice = 0;
				}
			}
			if(k == KeyEvent.VK_ENTER){
				switch(currentChoice){
				case 0:
					pause = false;
					break;
				case 1:
					gsm.setState(GameStateManager.LEVEL1STATE);
					break;
				case 2:
					toMenu();
					break;
				case 3:
					System.exit(0);
				}
			}
		}else{
			if(k == KeyEvent.VK_UP)player.setUp(true);
			if(k == KeyEvent.VK_DOWN)player.setDown(true);
			if(k == KeyEvent.VK_LEFT)player.setLeft(true);
			if(k == KeyEvent.VK_RIGHT)player.setRight(true);
			if(k == KeyEvent.VK_Z)player.setJumping(true);
			if(k == KeyEvent.VK_X)player.setFiring(true);
			if(k == KeyEvent.VK_SPACE)gsm.setBulletTime(true);
			if(k == KeyEvent.VK_ESCAPE)pause();
			if(k == KeyEvent.VK_S)s=!s;
			if(k == KeyEvent.VK_A&&s)a=!a;
			if(k == KeyEvent.VK_N&&s&&a)n=!n;
			if(k == KeyEvent.VK_I&&s&&a&&n)i=!i;
			if(k == KeyEvent.VK_C&&s&&a&&n&&i)c=!c;
			if(!(k == KeyEvent.VK_S
					||k == KeyEvent.VK_A
					||k == KeyEvent.VK_N
					||k == KeyEvent.VK_I
					||k == KeyEvent.VK_C))s=a=n=i=c=false;
		}
	}
	public void keyReleased(int k) {
		if(!pause){
			if(k == KeyEvent.VK_UP)player.setUp(false);
			if(k == KeyEvent.VK_DOWN)player.setDown(false);
			if(k == KeyEvent.VK_LEFT)player.setLeft(false);
			if(k == KeyEvent.VK_RIGHT)player.setRight(false);
			if(k == KeyEvent.VK_Z)player.setDy(player.getDy()/2);
			if(k == KeyEvent.VK_X)player.setFiring(false);
			if(k == KeyEvent.VK_SPACE)gsm.setBulletTime(false);
		}
	}
	public void toMenu(){
		gsm.setState(GameStateManager.MENUSTATE);
	}
}
