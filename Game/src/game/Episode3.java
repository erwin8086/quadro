package game;

import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;
/**
 * Episode III
 * The Sky
 * @author erwin
 *
 */
public class Episode3 implements LevelSet, GameListener{
	
	private String levels[] = {"level3_1.txt", "level3_2.txt", "level3_3.txt", "level3_4.txt", "level3_5.txt", "level3_6.txt", "level3_7.txt", "level3_8.txt", "level3_9.txt", "level3_10.txt"};
	private int level;
	private int width,height;
	private Color color = Color.white;
	private Game game;
	
	public Episode3(Game game, int level) {
		this.level=level;
		if(level>=levels.length)
			this.level=0;
		width=game.getGUI().getWidth();
		height=game.getGUI().getHeight();
		this.game=game;
	}

	/**
	 * getLevelAsStream
	 */
	@Override
	public InputStream getLevel() {
		if(level>=levels.length)
			level=0;
		return getClass().getResourceAsStream("/res/" + levels[level]);
	}

	/**
	 * Load next Level
	 */
	@Override
	public boolean nextLevel() {
		boolean ret=true;
		level++;
		if(level>=levels.length) {
			level=0;
			ret=false;
		}
		game.getLevel().saveLevel(false);
		return ret;
			
	}

	/**
	 * No next Episode
	 */
	@Override
	public LevelSet nextLevelSet() {
		return null;
	}

	@Override
	public void onLevelStarts() {
		// Show Screen
		if(level==0)
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story2.txt"));
		// Add EndEvil
		if(level==9) {
			game.getGameObjects().add(new EndEvil(game));
			game.getGameListeners().add(this);
		}
	}

	/**
	 * Get Number of Level in Epsiode
	 */
	@Override
	public int getLevelNum() {
		return level;
	}

	/**
	 * Draw green background
	 */
	@Override
	public void drawBackground(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * The defaultGame isScore
	 */
	@Override
	public boolean isScore() {
		return true;
	}

	/**
	 * Black font on white screen
	 */
	@Override
	public Color getFontColor() {
		return Color.black;
	}

	/**
	 * Show EndScreen (Story3.txt)
	 */
	@Override
	public void onGameExit() {
		game.getMenu().showScreen(getClass().getResourceAsStream("/res/story3.txt"));
		game.getGameListeners().remove(this);
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void postCreateGameObjects() {
		
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void preCreateGameObjects() {
		
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void onGameStarts() {
		
	}

}
