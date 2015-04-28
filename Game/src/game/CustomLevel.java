package game;
import java.awt.Color;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class CustomLevel implements LevelSet {
	private byte[] level;
	private Game game;

	public CustomLevel(Game game, byte[] level) {
		this.game=game;
		this.level=level;
	}
	
	@Override
	public InputStream getLevel() {
		return new ByteArrayInputStream(level);
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
	public void onLevelStarts() {}

	@Override
	public int getLevelNum() {
		return 0;
	}

	@Override
	public void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, game.getGUI().getWidth(), game.getGUI().getHeight());
	}

	@Override
	public boolean isScore() {
		return false;
	}

}
