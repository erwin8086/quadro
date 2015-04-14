import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Level Loader and Symbol Parser
 * @author erwin
 *
 */
public class Level {

	// Levels in Game
	private String[] levels = {"level.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt", "level7.txt", "level8.txt", "level9.txt", "level10.txt"};
	// Start Level
	private int level;
	private GUI gui;
	// GameObjects for Reset
	private ArrayList<GameObject> gos;
	
	private Game game;
	
	public Level(ArrayList<GameObject> gos, GUI gui, Game game) {
		this.gos = gos;
		this.gui = gui;
		this.game = game;
		level = Integer.valueOf(game.getSave().getConf(Save.LEVEL));
	}
	/**
	 * Get Level Stream
	 * @return InputStream for CurrentLevel
	 */
	public InputStream getLevel() {
		return getClass().getResourceAsStream("/res/" + levels[level]);
	}
	
	/**
	 * Switch to Next Level
	 * and Reset all Objects
	 * @param score
	 */
	public void nextLevel(Integer score) {
		level++;
		if(level>=levels.length) {
			JOptionPane.showMessageDialog(gui, "Finish - Score: " + score.toString());
			game.endGame();
		}
		// Reset all GameObjects
		for(GameObject g : gos) {
			g.reset();
		}
	}
	
	/**
	 * Get Number of CurrentLevel
	 * @return
	 */
	public int getLevelCount() {
		return level;
	}
	/**
	 * SymbolParser for Level files
	 * @author erwin
	 *
	 */
	abstract static class LevelLoader {
		/**
		 * Load Level file
		 * @param check Identifier
		 * @param in InputStream for Level
		 */
		public void load(char check, InputStream in) {
			Scanner read = new Scanner(in);
			int x=0,y=0;
			while(read.hasNextLine()) {
				String line = read.nextLine();
				char[] blocks = line.toCharArray();
				for(char c : blocks) {
					if(c==check) {
						// on Symbol Found
						onFound(x,y);
					}
					x+=16;
				}
				x=0;
				y+=16;
			}
			read.close();
		}
		/**
		 * Called on Symbol found to Add Object
		 * @param x
		 * @param y
		 */
		abstract void onFound(int x, int y);
	}

}
