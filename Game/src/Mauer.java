import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * The Walls
 * @author erwin
 *
 */
public class Mauer implements GameObject {

	private GUI gui;
	// The Coordinates of Walls
	private ArrayList<Rectangle> mauern;
	private Level level;
	private Game game;
	
	public Mauer(Game game) {
		this.gui=game.getGUI();
		this.level = game.getLevel();
		this.game=game;
		this.reset();
	}
	
	/**
	 * Do Noting
	 */
	@Override
	public boolean calc(float time) {
		return false;
	}

	/**
	 * Paint the Walls
	 */
	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		for(Rectangle r : mauern) {
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		return false;
	}

	
	/**
	 * Reset Walls
	 */
	@Override
	public boolean reset() {
		// Clear Walls
		mauern = new ArrayList<Rectangle>();
		// Define the food wall
		mauern.add(new Rectangle(0, gui.getHeight()-gui.getHeight()/37, gui.getWidth(), gui.getHeight()/37));
		
		/**
		 * Load Wall Coordinates from Level File
		 */
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				mauern.add(new Rectangle(x,y,gui.getWidth()/50,gui.getHeight()/37));
			}
		}.load('#', level.getLevel(), game);
		return false;
	}

	/**
	 * Check if Object Colidates Wall
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		for(Rectangle rp : mauern) {
			if(rp.intersects(r))
				return true;
		}
		return false;
	}

	/**
	 * Do Noting
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}

	@Override
	public int getType() {
		return GameObject.MAUER;
	}

	@Override
	public void changeDest(Rectangle r) {}

	@Override
	public int getCons() {
		return GameObject.CONS_MAUER;
	}
	
}
