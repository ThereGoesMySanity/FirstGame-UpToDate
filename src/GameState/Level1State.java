package GameState;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;
import TileMap.*;
public class Level1State extends GameState{
	private TileMap tileMap;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Item> items;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	public FlyingNinja bob;
	private Background background;
	private AudioPlayer bgMusic;
	private AudioPlayer sanic;
	private boolean s=false;
	private boolean a=false;
	private boolean n=false;
	private boolean i=false;
	private boolean c=false;
	public Level1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	public void init(){
		gsm.setBTTime(30);
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/lvl1tilesout.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		background = new Background("/Background/menubgmod.png", .5);
		background.setVector(-5, 0);
		player = new Player(tileMap);
		player.setPosition(100, -100);
		populateEnemies();
		populateItems();
		hud = new HUD(player);
		explosions = new ArrayList<Explosion>();
		if(GamePanel.ninjaSlayer){
			bgMusic = new AudioPlayer("/Music/Ninja Slayer.wav");
		}else{
			bgMusic = new AudioPlayer("/Music/song.wav");
		}
		sanic = new AudioPlayer("/Music/Green_Hill_Zone1.wav");
	}
	private int randEnemy(int xupper, int xlower){
		return (int)(Math.random()*(xupper-xlower))+xlower;
	}
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
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
		bgMusic.stop();
		sanic.play();
	}
	public void update(){
		if(s&&a&&n&&i&&c&&!GamePanel.ninjaSlayer){
			gottaGoFast();
			player.setSanic();
		}
		if(!bgMusic.isRunning()&&!player.sanic){
			bgMusic.play();
		}
		player.update();
		if(player.isDead()){
			bgMusic.stop();
			if(player.sanic){sanic.stop();}
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
	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_UP)player.setUp(true);
		if(k == KeyEvent.VK_DOWN)player.setDown(true);
		if(k == KeyEvent.VK_LEFT)player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT)player.setRight(true);
		if(k == KeyEvent.VK_Z)player.setJumping(true);
		if(k == KeyEvent.VK_X)player.setFiring(true);
		if(k == KeyEvent.VK_SPACE)gsm.setBulletTime(true);
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
	public void keyReleased(int k) {

		if(k == KeyEvent.VK_UP)player.setUp(false);
		if(k == KeyEvent.VK_DOWN)player.setDown(false);
		if(k == KeyEvent.VK_LEFT)player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT)player.setRight(false);
		if(k == KeyEvent.VK_Z)player.setDy(player.getDy()/2);
		if(k == KeyEvent.VK_X)player.setFiring(false);
		if(k == KeyEvent.VK_SPACE)gsm.setBulletTime(false);
	}
	public void toMenu(){
		gsm.setState(GameStateManager.MENUSTATE);
	}
}
