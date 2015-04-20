import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;


public class Episode1 implements LevelSet, GameObject {
	
	private Game game;
	private String[] levels = {"level.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt", "level7.txt", "level8.txt", "level9.txt", "level10.txt", "level11.txt"};
	private int level;
	private int size_y;
	
	public Episode1(Game game, int level) {
		this.game=game;
		this.level=level;
		size_y = game.getGUI().getHeight()/37;
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
		game.getGameObjects().remove(this);
		return new Episode2(game);
	}

	@Override
	public void onLevelStarts() {
		if(level==0) {
			game.getMenu().showScreen(getClass().getResourceAsStream("/res/story0.txt"));
		}
		if(level==10)
			game.getGameObjects().add(this);
	}

	@Override
	public int getLevelNum() {
		return level;
	}

	@Override
	public boolean calc(float time) {
		if(level==10) {
			Player player = game.getPlayer();
			if(player.isColidate(new Rectangle(0, 0, game.getGUI().getWidth(), size_y))) {
				player.delEvil();
			}
		}
		return false;
	}

	@Override
	public boolean paint(Graphics g) {
		if(level==10) {
			g.setColor(Color.green);
			g.fillRect(0, 0, game.getGUI().getWidth(), size_y);
		}
		return false;
	}

	@Override
	public boolean reset() {
		if(level==10)
			game.getPlayer().addEvil();
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
	public void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, game.getGUI().getWidth(), game.getGUI().getHeight());
	}

}
