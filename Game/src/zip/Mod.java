package zip;

import game.Game;

/**
 * The Interface for Mods
 * @author erwin
 *
 */
public interface Mod {

	/**
	 * Load The Mod
	 * @param game The Game
	 * @param zip The ZipFile from Load
	 */
	public void load(Game game, ZipFile zip);
}
