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
	
	public Mauer(GUI gui, Level level) {
		this.gui=gui;
		this.level = level;
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
		mauern.add(new Rectangle(0, gui.getHeight()-16, gui.getWidth(), 16));
		
		/**
		 * Load Wall Coordinates from Level File
		 */
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				mauern.add(new Rectangle(x,y,16,16));
			}
		}.load('#', level.getLevel());
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
	
}
