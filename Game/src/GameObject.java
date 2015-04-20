import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * Generic GameObject
 * Used for Objects like: Player, Evils, Boni
 * @author erwin
 *
 */
public interface GameObject {
	public static final int EVIL=1;
	public static final int PLAYER=2;
	public static final int MAUER=3;
	public static final int BONUS=4;
	
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
	 * destroy Colidating Object
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
	
}
