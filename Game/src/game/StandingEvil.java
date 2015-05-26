package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * A Static Evil
 * Symbol: 's'
 * @author erwin
 *
 */
public class StandingEvil implements GameObject {
	
	// The Evils
	protected ArrayList<Rectangle> evils;
	protected Game game;
	// The Size
	protected int size_x, size_y;
	// The Color
	private Color color= new Color(100,0,0);
	
	/**
	 * Creates The StandingEvil
	 * @param game the Game
	 */
	public StandingEvil(Game game) {
		this.game=game;
		GUI gui = game.getGUI();
		size_x = gui.getWidth()/50;
		size_y = gui.getHeight()/37;
		
		// Resets the Evil
		reset();
	}

	/**
	 * Calculate Collisions
	 * Don't move the Evil
	 */
	@Override
	public boolean calc(float time) {
		for(Rectangle r : evils) {
			for(GameObject g : game.getGameObjects()) {
				if(g.isColidate(r)) {
					g.changeDest(r);
				}
			}
		}
		return false;
	}

	/**
	 * Paint to Screen
	 */
	@Override
	public boolean paint(Graphics g) {
		g.setColor(color);
		for(Rectangle r : evils) {
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		return false;
	}

	/**
	 * Resets Evil on Level Starts
	 */
	@Override
	public boolean reset() {
		evils = new ArrayList<Rectangle>();
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				evils.add(new Rectangle(x,y, size_x, size_y));
			}
		}.load('s', game.getLevel().getLevel(), game);
		
		return false;
	}

	/**
	 * check if Object collidates with Evil
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		for(Rectangle r2 : evils) {
			if(r2.intersects(r)) return true;
		}
		return false;
	}

	/**
	 * Do Nothing
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}

	/**
	 * @return EVIL
	 */
	@Override
	public int getType() {
		return GameObject.EVIL;
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void changeDest(Rectangle r) {}

	/**
	 * get Consistency of EVIL
	 * @return CONS_MAUER
	 */
	@Override
	public int getCons() {
		return GameObject.CONS_MAUER;
	}

}
