import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * Generic GameObject
 * Used for Objects like: Player, Evils, Boni
 * @author erwin
 *
 */
public interface GameObject {
	/**
	 * The Generic EVIL
	 * Player checks for Collision
	 */
	public static final int EVIL=1;
	/**
	 * The Player
	 */
	public static final int PLAYER=2;
	/**
	 * Generic Wall Objects check for Collision
	 */
	public static final int MAUER=3;
	/**
	 * a Bonus the Bonus checks for Collision
	 */
	public static final int BONUS=4;
	/**
	 * A Evil the Evil checks for Collision
	 */
	public static final int EVIL_SELF_CALC=5;
	/**
	 * Consistency of a Mauer
	 */
	public static final int CONS_MAUER=1;
	/**
	 * Consistency of a Evil
	 */
	public static final int CONS_EVIL=2;
	
	/**
	 * Calculate Object
	 */
	public boolean calc(float time);
	
	/**
	 * Paint Object
	 */
	public boolean paint(Graphics g);
	
	/**
	 * Reset Object
	 */
	public boolean reset();
	
	/**
	 * Check isColidate? 
	 */
	public boolean isColidate(Rectangle r);
	
	/**
	 * destroy Collidating Object
	 */
	public boolean destroyColidate(Rectangle r);
	
	
	/**
	 * Get Type of Object
	 */
	public int getType();
	
	/**
	 * Changing the destination of Colidating
	 */
	public void changeDest(Rectangle r);
	
	/**
	 * Get Consistancy of the Object
	 */
	public int getCons();
	
}
