package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;

/**
 * The First Episode of The Game
 * The Underground
 * @author erwin
 *
 */
public class Episode1 implements LevelSet, GameObject {
	
	private Game game;
	// The Levelfiles in res
	private String[] levels = {"level.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt", "level7.txt", "level8.txt", "level9.txt", "level10.txt", "level11.txt"};
	// The Current Level
	private int level;
	private int size_y;
	private int width, height;
	
	/**
	 * Creates a Episode1 Object
	 * @param game The Game Instance
	 * @param level The Level to start
	 */
	public Episode1(Game game, int level) {
		this.game=game;
		this.level=level;
		size_y = game.getGUI().getHeight()/37;
		if(level>levels.length) this.level=0;
		width=game.getGUI().getWidth();
		height=game.getGUI().getHeight();
	}

	/**
	 * Gets a Level as Inputstream
	 */
	@Override
	public InputStream getLevel() {
		if(level>=levels.length)
			level=0;
		return getClass().getResourceAsStream("/res/" + levels[level]);
	}

	/**
	 * Go to next Level
	 * @return true if Level loadable
	 * 			false if Level not exist
	 */
	@Override
	public boolean nextLevel() {
		level++;
		if(level>=levels.length) {
			level=0;
			return false;
		}
		game.getLevel().saveLevel();
		return true;
	}

	/**
	 * Returns the Next Level Set
	 * @return the LevelSet
	 * 			null if not Exist
	 */
	@Override
	public LevelSet nextLevelSet() {
		game.getGameObjects().remove(this);
		return new Episode2(game,level);
	}

	/**
	 * Called on a Level starts
	 */
	@Override
	public void onLevelStarts() {
		// Show Story0.txt
		if(level==0) {
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story0.txt"));
		}
		// Show green bar
		if(level==10) {
			game.getGameObjects().add(this);
			game.getPlayer().addEvil();
		}
	}

	/**
	 * Get Number of Current Level
	 */
	@Override
	public int getLevelNum() {
		return level;
	}

	/**
	 * Calculate green bar
	 * finish if Player colidate
	 */
	@Override
	public boolean calc(float time) {
		if(level==10) {
			Player player = game.getPlayer();
			if(player.isColidate(new Rectangle(0, 0, width, size_y))) {
				player.delEvil();
			}
		}
		return false;
	}

	/**
	 * Paint green bar
	 */
	@Override
	public boolean paint(Graphics g) {
		if(level==10) {
			g.setColor(Color.green);
			g.fillRect(0, 0, width, size_y);
		}
		return false;
	}

	/**
	 * on Level 10 add a Evil to exit after green bar toucht
	 */
	@Override
	public boolean reset() {
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
	 * Draw Background for Episode 1
	 */
	@Override
	public void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Returns 0
	 */
	@Override
	public int getType() {
		return 0;
	}

	/**
	 * Do Noting
	 */
	@Override
	public void changeDest(Rectangle r) {}

	/**
	 * Returns CONS_EVIL
	 */
	@Override
	public int getCons() {
		return GameObject.CONS_EVIL;
	}

	@Override
	public boolean isScore() {
		return true;
	}

	@Override
	public Color getFontColor() {
		return Color.white;
	}

}
