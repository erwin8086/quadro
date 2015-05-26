package game;

/**
 * The GameListener
 * used for Mods and Default Game
 * @author erwin
 *
 */
public interface GameListener {

	/**
	 * Call on Level start
	 */
	public void onLevelStarts();
	/**
	 * Call on Game Exits to Menu
	 */
	public void onGameExit();
	/**
	 * Call on GameObjects Created
	 * add GameObjects here
	 */
	public void postCreateGameObjects();
	/**
	 * Call on GameObjects list Created
	 * and no GameObject added
	 */
	public void preCreateGameObjects();
	/**
	 * Call on Game start() and
	 * GameObjects list not Created
	 * do not Add GameObjects
	 */
	public void onGameStarts();
}
