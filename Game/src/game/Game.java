package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * The Running Game
 * Game is running if no Menu shown
 * @author erwin
 *
 */
public class Game implements Runnable, GameObject{
	// Is GameOver?
	private boolean gameover;
	// Is Paused
	private boolean pause, pause_menu;
	// The Menu Instance
	private Menu menu;
	// The SaveGame
	private Save save;
	private Level level;
	private Player player;
	// The GameObjects
	private ArrayList<GameObject> gos = new ArrayList<GameObject>();
	private ArrayList<Calculator> calcs;
	// The Wall
	private Mauer mauer;
	private Evil evil;
	
	private Color color=Color.white;
	
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
		level=new Level(this,null);
		calcs=new ArrayList<Calculator>();
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
	
	public void start() {
		start(null);
	}
	
	/**
	 * start Game Contains Gameloop
	 */
	public void start(LevelSet levels) {
		// Create Array for GameObjects
		gos = new ArrayList<GameObject>();
				
				
		// Create GameObjects 
		level = new Level(this, levels);
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
		gos.add(this);
		
		
		// Call LevelStarts
		level.onLevelStarts();
		gameover=false;
		pause_menu=false;
		pause=false;
		this.reset();
		new Thread(this).start();
		while(!gameover) {
			if(pause_menu) {
				pause=true;
				if(menu!=null)
					menu.showPauseMenu();
				pause=false;
				pause_menu=false;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		player.destroy();
		calcs=new ArrayList<Calculator>();
	}
	/**
	 * Pause Game and Show Menu
	 */
	public void pauseGame(boolean menu) {
		pause=true;
		pause_menu=menu;
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
	
	public void exitPause() {
		pause=false;
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
	@Override
	public void run() {
		while(!gameover) {
			// Get Graphics
			Graphics g = gui.getPaint();
			// Draw Background
			level.drawBackground(g);
			
			// Draw Objects
			for(int i=0;i<gos.size();i++) {
				gos.get(i).paint(g);
			}
			// Draw Status Text
			g.setColor(color);
			g.drawString("Lives: " + player.getLives().toString() + " Score: " + player.getScore().toString() + " Level:" + (level.getLevelCount()+1), 0, 24);
				
			// Finish Draw
			gui.finishPaint();
			
			while(pause) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Calculator implements Runnable {
		
		GameObject g;
		
		public Calculator(GameObject g) {
			this.g = g;
		}

		@Override
		public void run() {
			long last_frame, this_frame;
			// Set Time for LastFrame
			last_frame = System.currentTimeMillis();
			while(!gameover && gos.contains(g) && calcs.contains(this)) {
				// Set Time for ThisFrame
				this_frame = System.currentTimeMillis();
				// Calculate Time in Secounds sience Last Frame
				float time = ( (float) (this_frame-last_frame) )/1000;
				if(time>0.5f) time=0.1f;
				last_frame=this_frame;
				g.calc(time/2);
				g.calc(time/2);
				while(pause) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}

	@Override
	public boolean calc(float time) {
		return false;
	}
	@Override
	public boolean paint(Graphics g) {
		return false;
	}
	@Override
	public boolean reset() {
		pause=true;
		calcs=new ArrayList<Calculator>();
		color=level.getFontColor();
		if(color==null)
			color=Color.white;
		for(int i=0;i<gos.size();i++) {
			Calculator c = new Calculator(gos.get(i));
			calcs.add(c);
			new Thread(c).start();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pause=false;
		return false;
	}
	@Override
	public boolean isColidate(Rectangle r) {
		return false;
	}
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}
	@Override
	public int getType() {
		return 0;
	}
	@Override
	public void changeDest(Rectangle r) {}
	
	@Override
	public int getCons() {
		return 0;
	}

}
