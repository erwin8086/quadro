package game;
import java.awt.Graphics;
import java.io.InputStream;

/**
 * A Set of Levels
 * used for Episodes
 * @author erwin
 *
 */
public interface LevelSet {
	/**
	 * get Current Level as Stream
	 * @return the InputStream
	 */
	public InputStream getLevel();
	
	/**
	 * Switch to next Level
	 * @return true if Level exist; false if Level not exists
	 */
	public boolean nextLevel();
	
	/**
	 * Create Instance of next LevelSet
	 * @return Instance of next LevelSet
	 */
	public LevelSet nextLevelSet();
	
	/**
	 * Called on Level starts
	 * used for Story Texts and spezials
	 */
	public void onLevelStarts();
	
	/**
	 * get Nummer of Level for display and save
	 * @return Nummer of current Level in Set
	 */
	public int getLevelNum();
	
	/**
	 * draw Background for LevelSet
	 * @param g the Graphics to Paint
	 */
	public void drawBackground(Graphics g);
	
}
