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

	// Start Level
	private LevelSet level;
	
	private Game game;
	
	public Level(Game game) {
		this.game = game;

		level = new Episode1(game, Integer.valueOf(game.getSave().getConf(Save.LEVEL)));
		int levelset = Integer.valueOf(game.getSave().getConf(Save.LEVELSET));
		while(levelset > 0) {
			level=level.nextLevelSet();
			if(level==null) {
				level=new Episode1(game, 0);
				break;
			}
			levelset--;
		}
	}
	/**
	 * Get Level Stream
	 * @return InputStream for CurrentLevel
	 */
	public InputStream getLevel() {
		return level.getLevel();
	}
	
	public void gameComplete() {
		JOptionPane.showMessageDialog(game.getGUI(), "Finish - Score: " + game.getPlayer().getScore().toString());
		game.endGame();
		level = new Episode1(game, 0);
		newGame();
	}
	
	public void saveLevel() {
		game.getSave().setConf(Save.LEVEL, String.valueOf(getLevelCount()));
		game.getSave().setConf(Save.SCORE, String.valueOf(game.getPlayer().getScore()));
		game.getSave().setConf(Save.LIVES, String.valueOf(game.getPlayer().getLives()));
	}
	
	public void gameOver() {
		JOptionPane.showMessageDialog(game.getGUI(), "GameOver - Score: " + game.getPlayer().getScore().toString());
		game.getSave().setConf(Save.LEVEL, "0");
		game.getSave().setConf(Save.SCORE, "0");
		game.getSave().setConf(Save.LIVES, "3");
		game.getSave().saveScore(game.getGUI(), game.getPlayer().getScore());
		game.endGame();
	}
	
	public void newGame() {
		game.getSave().setConf(Save.LEVEL, String.valueOf(0));
		game.getSave().setConf(Save.SCORE, "0");
		game.getSave().setConf(Save.LIVES, "3");
		game.getSave().setConf(Save.LEVELSET, "0");
	}
	
	/**
	 * Switch to Next Level
	 * and Reset all Objects
	 * @param score
	 */
	public void nextLevel(Integer score) {
		if(!level.nextLevel()) {
			level=level.nextLevelSet();
			if(level==null) {
				gameComplete();
			} else {
				game.getSave().setConf(Save.LEVELSET, String.valueOf(Integer.valueOf(game.getSave().getConf(Save.LEVELSET))+1));
			}
		}
		
		
		// Reset all GameObjects
		for(GameObject g : game.getGameObjects()) {
			g.reset();
		}
	}
	
	public void onLevelStarts() {
		level.onLevelStarts();
	}
	
	/**
	 * Get Number of CurrentLevel
	 * @return
	 */
	public int getLevelCount() {
		return level.getLevelNum();
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
		public void load(char check, InputStream in, Game g) {
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
					x+=g.getGUI().getWidth()/50;
				}
				x=0;
				y+=g.getGUI().getHeight()/37;
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
