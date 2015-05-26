package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
/**
 * Vertical moving Evil
 * Symbol: 'V'
 * @author erwin
 *
 */
public class VerticalEvil implements GameObject {

	private int size_x, size_y;
	private final int speed=128;
	protected Game game;
	protected ArrayList<VEvil> evils;
	// The Color
	private Color color = new Color(100,0,0);
	
	/**
	 * creates Instance
	 * @param game The Game
	 */
	public VerticalEvil(Game game) {
		this.game=game;
		GUI gui = game.getGUI();
		size_x = gui.getWidth()/50;
		size_y = gui.getHeight()/37;
		reset();
	}
	
	/**
	 * Calculate movement
	 */
	@Override
	public boolean calc(float time) {
		for(VEvil e : evils) {
			e.calc(time);
		}
		return false;
	}

	/**
	 * Paint to Screen
	 */
	@Override
	public boolean paint(Graphics g) {
		g.setColor(color);
		for( VEvil e : evils) {
			e.paint(g);
		}
		return false;
	}

	/**
	 * Resets on Level Starts
	 */
	@Override
	public boolean reset() {
		evils = new ArrayList<VEvil>();
		new Level.LevelLoader() {
			
			@Override
			public void onFound(int x, int y) {
				evils.add(new VEvil(x,y));
			}
		}.load('V', game.getLevel().getLevel(), game);
		
		
		return false;
	}

	/**
	 * check if is Collidating with r
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		for(VEvil e : evils) {
			if(e.getPOS().intersects(r))
				return true;
		}
		return false;
	}

	/**
	 * Do Nothing
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}

	/**
	 * @return EVIL
	 */
	@Override
	public int getType() {
		return GameObject.EVIL;
	}

	/**
	 * Change vertical destination
	 */
	@Override
	public void changeDest(Rectangle r) {
		for(VEvil e : evils) {
			if(e.getPOS().intersects(r))
				e.setDest(e.getDest()*-1);
		}
	}

	/**
	 * @return CONS_EVIL
	 */
	@Override
	public int getCons() {
		return GameObject.CONS_EVIL;
	}
	
	/**
	 * The Single Evil
	 * @author erwin
	 *
	 */
	class VEvil {
		private float x,y;
		private int dest=1;
		
		/**
		 * Create Evil at Position
		 * @param x the x coordinate
		 * @param y the y coordinate
		 */
		public VEvil(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * Paint to Screen
		 * @param g Graphics to Paint
		 */
		public void paint(Graphics g) {
			g.fillRect((int)x, (int)y, size_x, size_y);
		}
		
		/**
		 * get Position as Rectangle
		 * @return the Position
		 */
		public Rectangle getPOS() {
			return new Rectangle((int)x, (int)y, size_x, size_y);
		}
		
		/**
		 * Calculate Movement
		 * @param time the Time since last frame
		 */
		public void calc(float time) {
			y+=dest*speed*time;
			if(y<0) dest*=-1;
			GameObject m=null;
			for(GameObject g : game.getGameObjects()) {
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(getPOS())) {
					m=g;
					break;
				}
			}
			if(m!=null) {
				dest*=-1;
				while(m.isColidate(getPOS())) {
					y+=dest*1;
				}
			}
		}
		
		/**
		 * get Destination
		 * -1 or 1
		 * @return the destination
		 */
		public int getDest() {
			return dest;
		}
		
		/**
		 * set Destination
		 * -1 or 1
		 * @param dest the Destination
		 */
		public void setDest(int dest) {
			this.dest=dest;
		}
		
		/**
		 * gets Y coordinate
		 * @return the y coordinate
		 */
		public float getY() {
			return y;
		}
		
		/**
		 * sets Y coordinate
		 * @param y the y coordinate
		 */
		public void setY(float y) {
			this.y=y;
		}
	}

}
