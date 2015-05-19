package game;

public interface GameListener {

	public void onLevelStarts();
	public void onGameExit();
	public void postCreateGameObjects();
	public void preCreateGameObjects();
	public void onGameStarts();
}
