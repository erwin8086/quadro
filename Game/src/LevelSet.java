import java.io.InputStream;


public interface LevelSet {
	public InputStream getLevel();
	public boolean nextLevel();
	public LevelSet nextLevelSet();
	public void onLevelStarts();
	public int getLevelNum();
	
}
