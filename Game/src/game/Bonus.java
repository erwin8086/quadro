package game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Bonus Objects in Game
 * Used for Coins, Lives, Invincible
 * @author erwin
 *
 */
public class Bonus implements GameObject{
	// The Player
	private Player player;
	// The Level Instance
	private Level level;
	// Contains all Boni
	private ArrayList<BonusObject> boni;
	private Game game;
	/**
	 * Creates and resets Bonus
	 * @param player // The Player
	 * @param level  // The Level instance
	 */
	public Bonus(Game game) {
		this.player=game.getPlayer();
		this.level=game.getLevel();
		this.game=game;
		this.reset();
	}

	/**
	 * Calculate Bonus(Check if Player colidate)
	 * @time Time sience Last Frame in Secounds
	 */
	@Override
	public boolean calc(float time) {
		int i;
		for(i=0;i<boni.size();i++) {
			if(boni.get(i).calc()) {
				boni.remove(i);
			}
		}
		return false;
	}

	/**
	 * Paint Bonus Image
	 */
	@Override
	public boolean paint(Graphics g) {
		for(BonusObject b : boni) {
			b.paint(g);
		}
		return false;
	}

	/**
	 * Resets Bonus on Level Starts
	 */
	@Override
	public boolean reset() {
		boni = new ArrayList<BonusObject>();
		BufferedImage image=null;
		// Coin 50
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/coin50.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage image_50=image;
		// Coin 100
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/coin100.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage image_100=image;
		// Coin 500
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/coin500.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage image_500=image;
		// Live
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/live.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage image_live=image;
		// Invincible
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/invincible.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage image_invincible=image;
		// Coin 50
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				boni.add(new Coin().setValue(50).setImage(image_50).setPos(x, y));
			}
		}.load('5', level.getLevel(), game);
		// Coin 100
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				boni.add(new Coin().setValue(100).setImage(image_100).setPos(x, y));
			}
		}.load('1', level.getLevel(), game);
		// Coin 500
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				boni.add(new Coin().setValue(500).setImage(image_500).setPos(x, y));
			}
		}.load('%', level.getLevel(), game);
		// Invincible
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				boni.add(new Invincible().setImage(image_invincible).setPos(x, y));
			}
		}.load('I', level.getLevel(), game);
		// Live
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				boni.add(new Live().setImage(image_live).setPos(x, y));
			}
		}.load('L', level.getLevel(), game);
		
		return false;
	}

	/**
	 * Do Noting
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		return false;
	}

	/**
	 * Do Noting
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}
	
	/**
	 * General BonusObject
	 * @author erwin
	 *
	 */
	abstract class BonusObject {
		protected Image image;
		private int x,y;
		/**
		 * On Player colidate with Bonus
		 */
		abstract void onBonus();
		/**
		 * Calculate if Player Colidate with Bonus
		 * and Calls onBonus();
		 * @return if true Bonus are destroyed
		 */
		public boolean calc() {
			if(player.isColidate(getPOS())) {
				onBonus();
				return true;
			}
			return false;
		}
		/**
		 * Get Postion as Rectangle
		 * @return
		 */
		public Rectangle getPOS() {
			return new Rectangle(x,y,image.getWidth(null), image.getHeight(null));
		}
		/**
		 * Paint Bonus
		 * @param g
		 */
		public void paint(Graphics g) {
			
			g.drawImage(image, x, y, null);
		}
		/**
		 * Sets Bonus Position
		 * @param x
		 * @param y
		 * @return
		 */
		public BonusObject setPos(int x, int y) {
			this.x=x;
			this.y=y;
			return this;
		}
		/**
		 * Set Bonus Image
		 * @param image
		 * @return
		 */
		public BonusObject setImage(Image image) {
			Image im2 = image.getScaledInstance(game.getGUI().getWidth()/50, game.getGUI().getHeight()/37, 0);
			this.image=im2;
			return this;
		}
	}
	/**
	 * Coin Bonus
	 * @author erwin
	 *
	 */
	class Coin extends BonusObject {
		// The Coin Value
		private int value=10;
		/**
		 * Set Coin Value
		 * @param value
		 * @return
		 */
		public Coin setValue(int value) {
			this.value=value;
			return this;
		}
		/**
		 * Add Score to Player
		 */
		@Override
		void onBonus() {
			player.addScore(value);
		}
		
	}
	/**
	 * Invincible Bonus
	 * @author erwin
	 *
	 */
	class Invincible extends BonusObject {

		/**
		 * Set Player Invincible
		 */
		@Override
		void onBonus() {
			player.addInvincible(5, true);
		}
		
	}
	/**
	 * Live Bonus
	 * @author erwin
	 *
	 */
	class Live extends BonusObject {

		/**
		 * Add a Live to Player
		 */
		@Override
		void onBonus() {
			player.addLive();
		}
		
	}
	@Override
	public int getType() {
		return GameObject.BONUS;
	}

	@Override
	public void changeDest(Rectangle r) {}

	@Override
	public int getCons() {
		return GameObject.CONS_EVIL;
	}

	

}
