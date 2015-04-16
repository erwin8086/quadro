import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;


public class Episode1 implements LevelSet, GameObject {
	
	private Game game;
	private String[] levels = {"level.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt", "level7.txt", "level8.txt", "level9.txt", "level10.txt"};
	private int level;
	
	public Episode1(Game game, int level) {
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
		if(level>=levels.length) return false;
		game.getLevel().saveLevel();
		return true;
	}

	@Override
	public LevelSet nextLevelSet() {
		return new Episode2();
	}

	@Override
	public void onLevelStarts() {
		if(level==0) {
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story0.txt"));
		}
	}

	@Override
	public boolean calc(float time) {
		return false;
	}

	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(0, 0,0 , 0);
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
	public int getLevelNum() {
		return level;
	}	

}
