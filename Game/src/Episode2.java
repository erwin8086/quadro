import java.io.InputStream;


public class Episode2 implements LevelSet {

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

	
}
