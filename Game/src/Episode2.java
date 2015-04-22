import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;


public class Episode2 implements LevelSet {
	private Game game;
	private Color color = new Color(0, 50,0);
	private String[] levels = {"level2_1.txt", "level2_2.txt", "level2_3.txt"};
	private int level;
	
	public Episode2(Game game, int level) {
		this.game=game;
		this.level=level;
	}

	@Override
	public InputStream getLevel() {
		return getClass().getResourceAsStream("/res/" + levels[level]);
	}

	@Override
	public boolean nextLevel() {
		level++;
		if(level>=levels.length) {
			level=0;
			return false;
		}
		return true;
	}

	@Override
	public LevelSet nextLevelSet() {
		return null;
	}

	@Override
	public void onLevelStarts() {
		if(level==0) {
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story1.txt"));
		}
	}

	@Override
	public int getLevelNum() {
		return 0;
	}

	@Override
	public void drawBackground(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, game.getGUI().getWidth(), game.getGUI().getHeight());
	}

	
}
