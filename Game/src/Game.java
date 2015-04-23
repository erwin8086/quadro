import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * The Running Game
 * Game is running if no Menu shown
 * @author erwin
 *
 */
public class Game {
	// Is GameOver?
	private boolean gameover;
	// Is Paused
	private boolean pause;
	// The Menu Instance
	private Menu menu;
	// The SaveGame
	private Save save;
	private Level level;
	private Player player;
	// The GameObjects
	private ArrayList<GameObject> gos = new ArrayList<GameObject>();
	// The Wall
	private Mauer mauer;
	private Evil evil;
	
	private GUI gui;
	
	/**
	 * Creates Game
	 * @param gui the MainWindow
	 * @param save the SaveGame
	 */
	public Game(GUI gui, Save save) {
		this.gui=gui;
		this.save=save;
		gui.addGame(this);
		level=new Level(this);
	}
	/**
	 * gets the Save Instance
	 * @return the Save Instance
	 */
	public Save getSave() {
		return save;
	}
	
	/**
	 * Attach the Menu to the Game
	 * @param menu
	 */
	public void addMenu(Menu menu) {
		this.menu=menu;
	}
	
	/**
	 * start Game Contains Gameloop
	 */
	public void start() {
		// Create Array for GameObjects
		gos = new ArrayList<GameObject>();
				
				
		// Create GameObjects 
		level = new Level(this);
		mauer = new Mauer(this);
		gos.add(mauer);
		player = new Player(this);
		player.setKeys(save.getKeys(Save.KEY_LEFT), save.getKeys(Save.KEY_RIGHT), save.getKeys(Save.KEY_UP));
		gos.add(player);
		evil=new Evil(this);
		gos.add(evil);
		gos.add(new Evil2(this));
		gos.add(new Evil3(this));
		gos.add(new Bonus(this));
		gos.add(new StandingEvil(this));
		gos.add(new DublicatingEvil(this));
		gos.add(new VerticalEvil(this));
		gos.add(new VerticalMauer(this));
		
		// Call LevelStarts
		level.onLevelStarts();
		
		// Create Variable for Time Calculate
		long last_frame, this_frame;
		// Set Time for LastFrame
		last_frame = System.currentTimeMillis();
		gameover=false;
		while(!gameover) {
			// Set Time for ThisFrame
			this_frame = System.currentTimeMillis();
			// Calculate Time in Secounds sience Last Frame
			float time = ( (float) (this_frame-last_frame) )/1000;
			last_frame=this_frame;
			// Set Max Time
			if(time>0.5) time=0.1f;
			// calculate all Objects
			for(int i=0;i<gos.size();i++) {
				gos.get(i).calc(time);
			}
			// Get Graphics
			Graphics g = gui.getPaint();
			// Draw Background
			level.drawBackground(g);
			
			// Draw Objects
			for(GameObject go: gos) {
				go.paint(g);
			}
			// Draw Status Text
			g.setColor(Color.WHITE);
			g.drawString("Lives: " + player.getLives().toString() + " Score: " + player.getScore().toString() + " Level:" + (level.getLevelCount()+1), 0, 24);
				
			// Finish Draw
			gui.finishPaint();
			
			if(pause) {
				if(menu!=null)
					menu.showPauseMenu();
				pause=false;
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		player.destroy();
	}
	/**
	 * Pause Game and Show Menu
	 */
	public void pauseGame() {
		pause=true;
	}
	
	/**
	 * Exits The Game
	 */
	public void endGame() {
		gameover=true;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public GUI getGUI() {
		return gui;
	}
	
	/**
	 * Get The ArrayList of GameObjects
	 * @return ArrayList of GameObjects
	 */
	public ArrayList<GameObject> getGameObjects() {
		return gos;
	}
	
	public Mauer getMauer() {
		return mauer;
	}
	
	public Evil getEvil() {
		return evil;
	}

}
