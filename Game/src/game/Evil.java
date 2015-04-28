package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Generic Evil Type:G
 * Run an Fall
 * @author erwin
 *
 */
public class Evil implements GameObject {
	// The Player
	Player player;
	// The Evils
	protected ArrayList<Evils> evils;
	protected int speed;
	// The Main Window
	private GUI gui;
	// The Wall
	protected Mauer mauer;
	// The Level
	private Level level;
	protected Game game;
	/**
	 * Generate Generic Evil
	 * @param game The Game
	 */
	public Evil(Game game) {
		player=game.getPlayer();
		this.mauer = game.getMauer();
		this.gui = game.getGUI();
		this.level = game.getLevel();
		this.game=game;
		speed=(gui.getWidth()/50)*4;
		// Reset Evil
		reset();
	}

	/**
	 * Calculate move set Player Gameover if Colidate
	 * Destroy Evil if jump
	 */
	@Override
	public boolean calc(float time) {
		int i;
		// Calculate all Evils
		for(i=0;i<evils.size();i++) {
			Evils e = evils.get(i);
			e.x += speed*time*e.dest;
			// TODO: Use GameObjects to Calculate
			if(mauer.isColidate(e.getPOS())) e.dest*=-1;
			while(mauer.isColidate(e.getPOS())) {
				e.x += 1*e.dest;
			}
			
			// Evil cannot exit Screen
			if(e.x>gui.getWidth()-e.size_x) {
				e.x=gui.getWidth()-e.size_x;
				e.dest *= -1;
			}	
			if(e.x<0) {
				e.x=0;
				e.dest *= -1;
			}
			// Check if Evil colidate with Mauer
			boolean is_mauer=false;
			for(GameObject g: game.getGameObjects()) {
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(e.getPOS())) {
					is_mauer=true;
					break;
				}
			}
			
			// Evil Fall if no Wall is under it
			if(!is_mauer) {
				e.y += time*speed;
				GameObject m=null;
				for(GameObject g: game.getGameObjects()) {
					if(g.getCons()!=GameObject.CONS_MAUER) continue;
					if(g.isColidate(e.getPOS())) {
						m=g;
						break;
					}
				}
				if(m!=null) {
					while(m.isColidate(e.getPOS())) {
						e.y--;
					}
				}
			}
			// Set Player Gameover of destroy Evil
			if(player.isColidate(e.getPOS())) {
				if(e.y-e.size_y/1.5 > player.getPOS().y) {
					this.destroy(e,time);
					player.jump(72, time);
				} else {
					if (player.destroyColidate(e.getPOS())) {
						this.destroy(e, time);
					}
				}
			}
			
		}
		
		// Calculate Changedest if Colidate
		for(GameObject g : game.getGameObjects()) {
			if(g==this) continue;
			for(Evils e : evils) {
				if(g.isColidate(e.getPOS())) {
					g.changeDest(e.getPOS());
				}
			}
		}
		// Calculate Evil and Evil Colision 
		for(Evils e1 : evils) {
			for(Evils e2 : evils) {
				if(e1==e2) continue;
				if(e1.getPOS().intersects(e2.getPOS())) {
					e1.dest*=-1;
					e2.dest*=-1;
				}
			}
		}
		
		return false;
	}
	/**
	 * Destroy This Evil
	 * Override if Evil has Multiple lives
	 * @param e
	 * @param time
	 */
	public void destroy(Evils e, float time) {
		player.addScore(25);
		evils.remove(e);
		player.delEvil();
	}

	/**
	 * Paint to the Screen
	 * Override if Color sold changed
	 * Call paintEvils(g) to Paint Evils
	 * in the Seted Color
	 */
	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.red);
		paintEvils(g);
		return false;
		
	}
	
	/**
	 * Paint Evil in Color
	 * @param g
	 */
	public void paintEvils(Graphics g) {
		for(int i=0;i<evils.size();i++) {
			Evils e = evils.get(i);
			g.fillRect((int)e.x, (int)e.y, e.size_x, e.size_y);
		}

	}

	/**
	 * reset Evil
	 * Override to change Placeholder in Level
	 */
	@Override
	public boolean reset() {
		return reset('G');
	}
	
	/**
	 * Reset Evil
	 * @param check // char to identify in Level
	 * @return
	 */
	public boolean reset(char check) {
		// Reset Evils List
		evils = new ArrayList<Evils>();
		// Read Level file
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				addEvil(x, y);
			}
		}.load(check, level.getLevel(), game);
					

		return false;
	}
	
	public void addEvil(int x, int y) {
		evils.add(new Evils(x,y,1));
		player.addEvil();
	}

	/**
	 * Check if Object Colidate
	 * @param Rectangle to check for Colision
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		for(Evils e : evils) {
			if(e.getPOS().intersects(r)) 
				return true;
		}
		return false;
	}

	/**
	 * Do Noting
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}
	/**
	 * The Evils on The Screen
	 * @author erwin
	 *
	 */
	class Evils {
		//Position
		float x,y;
		// Richtung 1 = right, -1 = left
		int dest;
		// Size
		int size_x,size_y;
		
		/**
		 * Init Evil
		 * @param x // initial Position
		 * @param y // ^
		 * @param dest
		 */
		public Evils(int x, int y, int dest) {
			size_x=gui.getWidth()/50;
			size_y=gui.getHeight()/37;
			this.x = x;
			this.y = y;
			this.dest = dest;
		}
		/**
		 * Get Position for Evil as Rectangle
		 * @return
		 */
		public Rectangle getPOS() {
			return new Rectangle((int)x, (int)y, size_x, size_y);
		}
	}
	/**
	 * Return EVIL_SELF_CALC
	 */
	@Override
	public int getType() {
		return GameObject.EVIL_SELF_CALC;
	}

	/**
	 * Change destination
	 */
	@Override
	public void changeDest(Rectangle r) {
		for(Evils e : evils) {
			if(r.intersects(e.getPOS())) {
				e.dest*=-1;
			}
		}
	}

	/**
	 * Get Consistency
	 */
	@Override
	public int getCons() {
		return GameObject.CONS_EVIL;
	}
	
	
}
