package TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import Entity.Item;
import GameState.Level1State;
import Main.GamePanel;
public class TileMap {
	private double x;
	private double y;
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	private double tween;
	//map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	///tiles
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	//drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize){
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.WIDTH / tileSize + 2;
		numColsToDraw = GamePanel.HEIGHT / tileSize + 2;
		setTween(0.09);
	}
	public void loadTiles(String s){
		try{
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth()/tileSize;
			tiles = new Tile[3][numTilesAcross];
			BufferedImage subimage;
			for(int col=0; col<numTilesAcross; col++){
				subimage = tileset.getSubimage(col*tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(col*tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
				subimage = tileset.getSubimage(col*tileSize, 2*tileSize, tileSize, tileSize);
				tiles[2][col] = new Tile(subimage, Tile.STAIR_DOWN);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void loadMap(String s){
		try{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols*tileSize;
			height = numRows*tileSize;

			
			xmin = 320 - width;
			xmax = 0;
			ymin = 240 - height;
			ymax = 0;
			Pattern re = Pattern.compile("i(\\d?) ");
			String delims = "\\s+";
			Item q;
			for(int row = 0; row<numRows; row++){
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col<numCols; col++){
					Matcher x = re.matcher(tokens[col]);
					if(x.matches()){
						map[row][col] = 0;
						q = new Item(this, Integer.parseInt(x.group(1)));
						q.setPosition(row*tileSize, col*tileSize);
						Level1State.items.add(q);
					}else{
						map[row][col] = Integer.parseInt(tokens[col]);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public int getTileSize(){return tileSize;}
	public int getx(){return (int)x;}
	public int gety(){return (int)y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public int getType(int row, int col){
		int rc = map[row][col];
		int r = rc/numTilesAcross;
		int c = rc%numTilesAcross;
		return tiles[r][c].getType();
	}
	public void setPosition(double x, double y) {
		
		
		
		this.x = (-x + 160);
		this.y = (-y + 120);
		
				
		fixBounds();
		colOffset = ((int)-this.x / tileSize);
		rowOffset = ((int)-this.y / tileSize);
		
	}
	private void fixBounds(){
		if(x<xmin)x=xmin;
		if(y<ymin)y=ymin;
		if(x>xmax)x=xmax;
		if(y>ymax)y=ymax;
		
	}
	public void draw(Graphics2D g){
		for(int row = rowOffset; row<rowOffset + numRowsToDraw; row++){
			for(int col=colOffset; col<colOffset+numColsToDraw; col++){
				if(row>=numRows){
					break;
				}
				if(col>=numCols){
					break;
				}
				
				int rc = map[row][col];
				int r = rc/numTilesAcross;
				int c = rc%numTilesAcross;
				g.drawImage(tiles[r][c].getImage(), ((int)x + col*tileSize)*2/GamePanel.SCALE, ((int)y + row*tileSize)*2/GamePanel.SCALE, 
						tileSize*2/GamePanel.SCALE, tileSize*2/GamePanel.SCALE, null);
			}
		}
	}
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public double getTween() {
		return tween;
	}
	public void setTween(double tween) {
		this.tween = tween;
	}
}
