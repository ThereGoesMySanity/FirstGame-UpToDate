package Entity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import GameState.GameStateManager;
import Main.GamePanel;

import javax.imageio.ImageIO;
public class HUD {
	private Player player;
	private BufferedImage image;
	private BufferedImage health;
	private BufferedImage ammo;
	private BufferedImage bt;
	private Font font;
	private int btCooldownTimer;
	public HUD(Player p){
		player = p;
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/GUI/hud.png"));
			health = ImageIO.read(getClass().getResourceAsStream("/GUI/health.png"));
			ammo = ImageIO.read(getClass().getResourceAsStream("/GUI/ammo.png"));
			bt = ImageIO.read(getClass().getResourceAsStream("/GUI/bt.png"));
			font = new Font("Fixedsys", Font.TRUETYPE_FONT, 28/GamePanel.SCALE);
		}catch(Exception e){e.printStackTrace();}
	}
	public void draw(Graphics2D g){
		g.drawImage(image, 0, GamePanel.HEIGHT-36/GamePanel.SCALE, 
				GamePanel.WIDTH/GamePanel.SCALE, image.getHeight()*2/GamePanel.SCALE, null);
		g.setFont(font);
		int bullets = player.getBullet();
		int healths = player.getHealth();
		int maxBullets = player.getMaxBullets();
		int maxHealth = player.getMaxHealth();
		long btTimer = GameStateManager.getBTTimer();
		for(int i = 0; i<(
				(healths*76/maxHealth)
				); i++){
			g.drawImage(health, 8/GamePanel.SCALE + (4/GamePanel.SCALE*i), GamePanel.HEIGHT-28/GamePanel.SCALE,
					2/GamePanel.SCALE, 8/GamePanel.SCALE, null);
		}
		for(int i = 0; i<(
				(bullets*76/maxBullets)
				); i++){
			g.drawImage(ammo, GamePanel.WIDTH-10/GamePanel.SCALE - (4/GamePanel.SCALE*i), GamePanel.HEIGHT-28/GamePanel.SCALE,
					2/GamePanel.SCALE, 8/GamePanel.SCALE, null);
		}
		if(GameStateManager.getCooldown()){
			btCooldownTimer++;
		}
		if(!GameStateManager.getCooldown()||btCooldownTimer < 2){
			g.drawImage(bt, 0, GamePanel.HEIGHT-8/GamePanel.SCALE, (int)btTimer*2/GamePanel.SCALE, 16/GamePanel.SCALE,  null);
		}else if(btCooldownTimer == 6){
			btCooldownTimer = 0;
		}
	}
}
