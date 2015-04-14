import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * Generic GameObject
 * Used for Objects like: Player, Evils, Boni
 * @author erwin
 *
 */
public interface GameObject {
	/*
	 * Calculate Object
	 */
	public boolean calc(float time);
	
	/*
	 * Paint Object
	 */
	public boolean paint(Graphics g);
	
	/*
	 * Reset Object
	 */
	public boolean reset();
	
	/*
	 * Check isColidate? 
	 */
	public boolean isColidate(Rectangle r);
	
	/*
	 * destroy Colidating Object
	 */
	public boolean destroyColidate(Rectangle r);
	
}
