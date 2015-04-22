import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;



/**
 * Vertical Moving Wall
 * Symbol: 'm'
 * @author erwin
 *
 */
public class VerticalMauer implements GameObject{
	
	private Game game;
	
	private ArrayList<Rectangle> mauern;
	private ArrayList<Moving> mov;
	private int size_x, size_y;
	private float last;
	private int speed;
	
	/**
	 * Creates the Instance
	 * @param game the Game
	 */
	public VerticalMauer(Game game) {
		this.game=game;
		GUI gui = game.getGUI();
		size_x = gui.getWidth()/50;
		size_y = gui.getHeight()/37;
		speed=(gui.getWidth()/50)*4;
		reset();
	}

	/**
	 * Calculate Movement
	 */
	@Override
	public boolean calc(float time) {
		last+=time;
		// Creates new moving
		if(last>5) {
			last=0;
			for(Rectangle r : mauern) {
				Moving m = new Moving();
				m.x = r.x;
				m.y = r.y - r.height;
				mov.add(m);
			}
		}
		// Calculate Moving
		for(int i=0;i<mov.size();i++) {
			Moving m = mov.get(i);
			m.y-=time*speed;
			if(m.y+size_y<0) {
				mov.remove(m);
				return false;
			}
			for(GameObject g : game.getGameObjects()) {
				if(g==this) continue;
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(new Rectangle((int) m.x, (int) m.y, size_x, size_y))) {
					mov.remove(m);
					return false;
				}
			}
			Player play = game.getPlayer();
			while(play.isColidate(new Rectangle((int)m.x, (int)m.y, size_x, size_y))) {
				if(play.getY()>m.y) break;
				play.setY(play.getY()-1);
				for(GameObject g: game.getGameObjects()) {
					if(g==this) continue;
					if(g.getCons()!=GameObject.CONS_MAUER) continue;
					if(g.isColidate(play.getPOS())) {
						mov.remove(m);
						play.setY(play.getY()+1);
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Paint Wall and Moving to Screen
	 */
	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		for(Rectangle r : mauern) {
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		g.setColor(new Color(0 , 0, 200));
		for(Moving m : mov) {
			g.fillRect((int)m.x, (int)m.y, size_x, size_y);
		}
		return false;
	}

	/**
	 * Resets on Level Starts
	 */
	@Override
	public boolean reset() {
		last=0;
		mauern=new ArrayList<Rectangle>();
		mov=new ArrayList<Moving>();
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				mauern.add(new Rectangle(x,y, size_x, size_y));
			}
		}.load('m', game.getLevel().getLevel(), game);
		return false;
	}

	/**
	 * check if it is Colidating
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		for(Rectangle r2 : mauern) {
			if(r2.intersects(r)) return true;
		}
		for(Moving m : mov) {
			if(new Rectangle((int)m.x, (int)m.y, size_x, size_y).intersects(r)) {
				return true;
			}
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
	 * @return MAUER
	 */
	@Override
	public int getType() {
		return GameObject.MAUER;
	}

	/**
	 * Do Noting
	 */
	@Override
	public void changeDest(Rectangle r) {
		
	}

	/**
	 * @return CONS_MAUER
	 */
	@Override
	public int getCons() {
		return GameObject.CONS_MAUER;
	}
	
	/**
	 * The Moving Object
	 * @author erwin
	 *
	 */
	class Moving {
		public float x, y;
	}

}
