package zip;

import game.Game;
/**
 * The BaseMod used for Base for Other Mods
 * Adds a GameListener and dels the GameListener on Exit
 * @author erwin
 *
 */
public abstract class BaseMod implements game.GameListener, Mod{

	protected Game game;
	protected ZipFile zip;
	abstract protected void onLoad();
	abstract protected void onUnload();
	
	protected Game getGame() {
		return game;
	}
	
	protected ZipFile getZip() {
		return zip;
	}
	
	protected void addGameObject(game.GameObject g) {
		game.getGameObjects().add(g);
	}
	
	@Override
	public void load(Game game, ZipFile zip) {
		this.game=game;
		this.zip=zip;
		game.getGameListeners().add(this);
		onLoad();
	}

	@Override
	public void onGameExit() {
		game.getGameListeners().remove(this);
		onUnload();
	}

}
