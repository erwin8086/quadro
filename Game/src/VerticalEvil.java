import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class VerticalEvil implements GameObject {

	private int size_x, size_y;
	private final int speed=128;
	private Game game;
	private ArrayList<VEvil> evils;
	private Color color = new Color(100,0,0);
	
	public VerticalEvil(Game game) {
		this.game=game;
		GUI gui = game.getGUI();
		size_x = gui.getWidth()/50;
		size_y = gui.getHeight()/37;
		reset();
	}
	@Override
	public boolean calc(float time) {
		for(VEvil e : evils) {
			e.calc(time);
		}
		return false;
	}

	@Override
	public boolean paint(Graphics g) {
		g.setColor(color);
		for( VEvil e : evils) {
			e.paint(g);
		}
		return false;
	}

	@Override
	public boolean reset() {
		evils = new ArrayList<VEvil>();
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				evils.add(new VEvil(x,y));
			}
		}.load('V', game.getLevel().getLevel(), game);
		
		
		return false;
	}

	@Override
	public boolean isColidate(Rectangle r) {
		for(VEvil e : evils) {
			if(e.getPOS().intersects(r))
				return true;
		}
		return false;
	}

	@Override
	public boolean destroyColidate(Rectangle r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getType() {
		return GameObject.EVIL;
	}

	@Override
	public void changeDest(Rectangle r) {
		
	}

	@Override
	public int getCons() {
		return GameObject.CONS_EVIL;
	}
	
	class VEvil {
		private float x,y;
		private int dest=1;
		
		public VEvil(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void paint(Graphics g) {
			g.fillRect((int)x, (int)y, size_x, size_y);
		}
		
		public Rectangle getPOS() {
			return new Rectangle((int)x, (int)y, size_x, size_y);
		}
		
		public void calc(float time) {
			y+=dest*speed*time;
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
	}

}
