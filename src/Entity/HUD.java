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
			font = new Font("Fixedsys", Font.TRUETYPE_FONT, 14);
		}catch(Exception e){e.printStackTrace();}
	}
	public void draw(Graphics2D g){
		g.drawImage(image, 0, GamePanel.HEIGHT-18 , null);
		g.setFont(font);
		int bullets = player.getBullet();
		int healths = player.getHealth();
		int maxBullets = player.getMaxBullets();
		int maxHealth = player.getMaxHealth();
		long btTimer = GameStateManager.getBTTimer();
		for(int i = 0; i<(
				(healths*76/maxHealth)
				); i++){
			g.drawImage(health, 4 + (2*i), GamePanel.HEIGHT-14, null);
		}
		for(int i = 0; i<(
				(bullets*76/maxBullets)
				); i++){
			g.drawImage(ammo, GamePanel.WIDTH-5 - (2*i), GamePanel.HEIGHT-14, null);
		}
		if(GameStateManager.getCooldown()){
			btCooldownTimer++;
		}
		if(!GameStateManager.getCooldown()||btCooldownTimer < 2){
			g.drawImage(bt, (int) (btTimer*GamePanel.WIDTH/320-640), GamePanel.HEIGHT-4, null);
		}else if(btCooldownTimer == 6){
			btCooldownTimer = 0;
		}
		g.drawString(player.getx() + ", " + player.gety(), 160, 50);
	}
}
