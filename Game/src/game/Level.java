package game;
import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Level Loader and Symbol Parser
 * @author erwin
 *
 */
public class Level {

	// Start Level
	private LevelSet level;
	private LevelSet old_level;
	private boolean endGame=false;
	
	private Game game;
	
	/**
	 * Create Level Instance
	 * and Reads level from Save
	 * @param game
	 */
	public Level(Game game, LevelSet levelSet) {
		this.game = game;

		if(levelSet==null) {
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
		} else {
			level=levelSet;
		}
	}
	/**
	 * Get Level Stream
	 * @return InputStream for CurrentLevel
	 */
	public InputStream getLevel() {
		if(level==null) return null;
		return level.getLevel();
	}
	
	/**
	 * Game Finish
	 * ends Game on Game complete
	 */
	public void gameComplete() {
		if(endGame) return;
		endGame=true;
		game.pauseGame(false);
		if(old_level.isScore())
			game.getSave().saveScore(game.getGUI(), game.getPlayer().getScore()+5000);
		else
			game.getMenu().EnterScore(false, game.getPlayer().getScore());
		level = new Episode1(game, 0);
		if(old_level.isScore())
			newGame(true);
		game.endGame();
		game.exitPause();
	}
	
	/**
	 * save the Level
	 */
	public void saveLevel(boolean is_over) {
		if(is_over)
			if(!level.isScore()) return;
		else
			if(!old_level.isScore()) return;
		game.getSave().setConf(Save.LEVEL, String.valueOf(getLevelCount()));
		game.getSave().setConf(Save.SCORE, String.valueOf(game.getPlayer().getScore()));
		game.getSave().setConf(Save.LIVES, String.valueOf(game.getPlayer().getLives()));
	}
	
	/**
	 * Ends the Game on Player dies
	 */
	public void gameOver() {
		if(endGame) return;
		endGame=true;
		game.pauseGame(false);
		if(level.isScore())
			game.getSave().saveScore(game.getGUI(), game.getPlayer().getScore());
		else
			game.getMenu().EnterScore(false, game.getPlayer().getScore());
		if(isScore())
			newGame(false);
		game.endGame();
		game.exitPause();
	}
	
	/**
	 * Resets the SaveGame
	 * @param user clear the LevelSet Save. True for Finish or new Game
	 */
	public void newGame(boolean user) {
		game.getSave().setConf(Save.LEVEL, String.valueOf(0));
		game.getSave().setConf(Save.SCORE, "0");
		game.getSave().setConf(Save.LIVES, "3");
		if(user)
			game.getSave().setConf(Save.LEVELSET, "0");
	}
	
	/**
	 * Draw Background
	 */
	public void drawBackground(Graphics g) {
		if(level!=null)
			level.drawBackground(g);
	}
	
	/**
	 * Switch to Next Level
	 * and Reset all Objects
	 * @param score
	 */
	public void nextLevel(Integer score) {
		game.pauseGame(false);
		old_level=level;
		if(!level.nextLevel()) {
			level=level.nextLevelSet();
			if(level==null) {
				gameComplete();
				return;
			} else {
				if(level.isScore())
					game.getSave().setConf(Save.LEVELSET, String.valueOf(Integer.valueOf(game.getSave().getConf(Save.LEVELSET))+1));
			}
		}
		onLevelStarts();
		
		
		// Reset all GameObjects
		for(GameObject g : game.getGameObjects()) {
			g.reset();
		}
		game.exitPause();
	}
	
	/**
	 * Called on Levelstart
	 * Calls LevelSets on LevelStarts
	 */
	public void onLevelStarts() {
		ArrayList<GameListener> gameListeners = game.getGameListeners();
		for(int i=0;i<gameListeners.size();i++) {
			gameListeners.get(i).onLevelStarts();
		}
		endGame=false;
		level.onLevelStarts();
	}
	
	/**
	 * Get Number of CurrentLevel
	 * @return
	 */
	public int getLevelCount() {
		if(level!=null)
			return level.getLevelNum();
		return 0;
	}
	
	public Color getFontColor() {
		if(level==null)
			return null;
		return level.getFontColor();
	}
	
	public boolean isScore() {
		if(level==null) return false;
		return level.isScore();
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
