package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject {
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	protected double x;
	protected double y;
	protected double dx;
	public double getDy() {
		return dy;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}
	public void setDy(double dy) {
		this.dy = dy;
	}

	protected double dy;
	protected int width;
	protected int height;
	protected int cwidth;
	protected int cheight;
	protected int health;
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected boolean middleLeft;
	protected boolean middleRight;
	protected boolean blStair;
	protected boolean brStair;
	
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	public double moveSpeed;
	public double maxSpeed;
	public double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	public MapObject(TileMap tm){
		tileMap = tm;
		tileSize = tm.getTileSize();
		
	}
	public boolean intersects(MapObject o){
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	public Rectangle getRectangle(){
		return new Rectangle((int)x-cwidth, (int)y-cheight, cwidth, cheight);
	}
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow+1) * tileSize - cheight/2;
			}
			else {
				
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	public int getx(){return (int)this.x;}
	public int gety(){return (int)this.y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getCWidth(){return cwidth;}
	public int getCHeight(){return cheight;}
	public int getHealth(){return health;}
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	public void setMapPosition(){
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	public void calculateCorners(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topLeft = topRight = bottomLeft = bottomRight = false;
                return;
        }
        int tl = tileMap.getType(topTile, leftTile);
        int ml = tileMap.getType(topTile+1, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int mr = tileMap.getType(topTile+1, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
        middleLeft = ml == Tile.BLOCKED;
        middleRight = mr == Tile.BLOCKED;
        blStair = bl == Tile.STAIR_DOWN;
        brStair = bl == Tile.STAIR_DOWN;
	}
	public void setLeft(boolean b){left = b;}
	public void setRight(boolean b){right = b;}
	public void setUp(boolean b){up = b;}
	public void setDown(boolean b){down = b;}
	public void setJumping(boolean b){jumping = b;}
	
	public void draw(Graphics2D g){
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x+xmap-width/2)*2/GamePanel.SCALE, 
											  (int)(y+ymap-height/2)*2/GamePanel.SCALE, 
											  width*2/GamePanel.SCALE, 
											  height*2/GamePanel.SCALE, null);
		}else{
			g.drawImage(animation.getImage(), (int)(x+xmap-width/2+width)*2/GamePanel.SCALE, 
											  (int)(y+ymap-height/2)*2/GamePanel.SCALE, 
											  -width*2/GamePanel.SCALE, 
											  height*2/GamePanel.SCALE, null);
		}
	}
}
