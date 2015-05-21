package game;

import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;

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

	@Override
	public InputStream getLevel() {
		if(level>=levels.length)
			level=0;
		return getClass().getResourceAsStream("/res/" + levels[level]);
	}

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

	@Override
	public LevelSet nextLevelSet() {
		return null;
	}

	@Override
	public void onLevelStarts() {
		if(level==0)
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story2.txt"));
		if(level==9) {
			game.getGameObjects().add(new EndEvil(game));
			game.getGameListeners().add(this);
		}
	}

	@Override
	public int getLevelNum() {
		return level;
	}

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
	public Color getFontColor() {
		return Color.black;
	}

	@Override
	public void onGameExit() {
		game.getMenu().showScreen(getClass().getResourceAsStream("/res/story3.txt"));
		game.getGameListeners().remove(this);
	}

	@Override
	public void postCreateGameObjects() {
		
	}

	@Override
	public void preCreateGameObjects() {
		
	}

	@Override
	public void onGameStarts() {
		
	}

}
