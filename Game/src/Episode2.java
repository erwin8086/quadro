import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;


public class Episode2 implements LevelSet {
	private Game game;
	private Color color = new Color(0, 50,0);
	
	public Episode2(Game game) {
		this.game=game;
	}

	@Override
	public InputStream getLevel() {
		return getClass().getResourceAsStream("/res/level.txt");
	}

	@Override
	public boolean nextLevel() {
		return false;
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
		return 0;
	}

	@Override
	public void drawBackground(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, game.getGUI().getWidth(), game.getGUI().getHeight());
	}

	
}
