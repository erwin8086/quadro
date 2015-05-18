package game;

import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;

public class Episode3 implements LevelSet{
	
	private String levels[] = {"level3_1.txt", "level3_2.txt", "level3_3.txt", "level3_4.txt"};
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
		level++;
		if(level>=levels.length) {
			level=0;
			return false;
		}
		game.getLevel().saveLevel(false);
		return true;
			
	}

	@Override
	public LevelSet nextLevelSet() {
		return null;
	}

	@Override
	public void onLevelStarts() {
		
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

}
