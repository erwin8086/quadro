import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Game {
	
	private boolean gameover;
	private boolean pause;
	private Menu menu;
	private Save save;
	private Level level;
	
	private GUI gui;
	public Game(GUI gui, Save save) {
		this.gui=gui;
		this.save=save;
		gui.addGame(this);
	}
	
	public Save getSave() {
		return save;
	}
	
	public void addMenu(Menu menu) {
		this.menu=menu;
	}
	/**
	 * start Game Contains Gameloop
	 */
	public void start() {
		// Create Array for GameObjects
		ArrayList<GameObject> gos = new ArrayList<GameObject>();
				
				
		// Create GameObjects 
		level = new Level(gos,gui, this);
		Mauer mauer = new Mauer(gui,level);
		gos.add(mauer);
		Player player = new Player(gui,mauer, level, this);
		player.setKeys(save.getKeys(Save.KEY_LEFT), save.getKeys(Save.KEY_RIGHT), save.getKeys(Save.KEY_UP));
		gos.add(player);
		gos.add(new Evil(player,gui,mauer,level));
		gos.add(new Evil2(player,gui,mauer,level));
		gos.add(new Evil3(player,gui,mauer,level));
		gos.add(new Bonus(player,level));
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
			for(GameObject go: gos) {
				go.calc(time);
			}
			// Get Graphics
			Graphics g = gui.getPaint();
			// Draw Background
			g.setColor(Color.black);
			g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
			
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
	}
	
	public void pauseGame() {
		pause=true;
	}
	
	public void endGame() {
		gameover=true;
	}
	
	public Level getLevel() {
		return level;
	}

}
