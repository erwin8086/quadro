package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;

/**
 * Episode 2
 * The Grasland
 * @author erwin
 *
 */
public class Episode2 implements LevelSet, GameObject {
	private Game game;
	// The Background Color
	private Color color = new Color(0, 50,0);
	// The Levels as TextFiles in res
	private String[] levels = {"level2_1.txt", "level2_2.txt", "level2_3.txt", "level2_4.txt", "level2_5.txt", "level2_6.txt", "level2_7.txt", "level2_8.txt", "level2_9.txt", "level2_10.txt"};
	// The Current Level
	private int level;
	private int width, height;
	
	/**
	 * Create Object
	 * @param game The Game
	 * @param level The Level to start
	 */
	public Episode2(Game game, int level) {
		this.game=game;
		this.level=level;
		if(level>=levels.length) this.level=0;
		width=game.getGUI().getWidth();
		height=game.getGUI().getHeight();
	}

	/**
	 * Gets Current Level as InputStream
	 */
	@Override
	public InputStream getLevel() {
		if(level>=levels.length)
			level=0;
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
		game.getGameObjects().remove(this);
		return new Episode3(game,level);
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
		if(level==9) {
			game.getGameObjects().add(this);
			game.getPlayer().addEvil();
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
		g.fillRect(0, 0, width, height);
	}

	@Override
	public boolean isScore() {
		return true;
	}

	@Override
	public boolean calc(float time) {
		if(game.getPlayer().isColidate(new Rectangle(0,0,width,16))) {
			game.getPlayer().delEvil();
		}
		return false;
	}

	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, width, 16);
		return false;
	}

	@Override
	public boolean reset() {
		return false;
	}

	@Override
	public boolean isColidate(Rectangle r) {
		return false;
	}

	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public void changeDest(Rectangle r) {}

	@Override
	public int getCons() {

		return GameObject.CONS_EVIL;
	}

	@Override
	public Color getFontColor() {
		return Color.white;
	}

	
}
