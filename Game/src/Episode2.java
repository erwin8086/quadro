import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;

/**
 * Episode 2
 * The Grasland
 * @author erwin
 *
 */
public class Episode2 implements LevelSet {
	private Game game;
	// The Background Color
	private Color color = new Color(0, 50,0);
	// The Levels as TextFiles in res
	private String[] levels = {"level2_1.txt", "level2_2.txt", "level2_3.txt", "level2_4.txt", "level2_5.txt"};
	// The Current Level
	private int level;
	
	/**
	 * Create Object
	 * @param game The Game
	 * @param level The Level to start
	 */
	public Episode2(Game game, int level) {
		this.game=game;
		this.level=level;
		if(level>levels.length) this.level=0;
	}

	/**
	 * Gets Current Level as InputStream
	 */
	@Override
	public InputStream getLevel() {
		return getClass().getResourceAsStream("/res/" + levels[level]);
	}

	/**
	 * Gets the Next Level
	 * @return false if Level cold not load otherwise true
	 */
	@Override
	public boolean nextLevel() {
		level++;
		if(level>=levels.length) {
			level=0;
			return false;
		}
		return true;
	}

	/**
	 * Returns next LevelSet
	 * @return null if LevelSet cold not load
	 */
	@Override
	public LevelSet nextLevelSet() {
		return null;
	}

	/**
	 * Called on Level Starts
	 * Show Story1.txt
	 */
	@Override
	public void onLevelStarts() {
		if(level==0) {
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story1.txt"));
		}
	}

	/**
	 * Get Number of Level
	 */
	@Override
	public int getLevelNum() {
		return level;
	}

	/**
	 * Draw Background in 'color' Color 
	 */
	@Override
	public void drawBackground(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, game.getGUI().getWidth(), game.getGUI().getHeight());
	}

	
}
