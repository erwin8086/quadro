import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;


public class StandingEvil implements GameObject {
	
	protected ArrayList<Rectangle> evils;
	protected Game game;
	protected int size_x, size_y;
	
	public StandingEvil(Game game) {
		this.game=game;
		GUI gui = game.getGUI();
		size_x = gui.getWidth()/50;
		size_y = gui.getHeight()/37;
		
		reset();
	}

	@Override
	public boolean calc(float time) {
		for(Rectangle r : evils) {
			for(GameObject g : game.getGameObjects()) {
				if(g.isColidate(r)) {
					g.changeDest(r);
				}
			}
		}
		return false;
	}

	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.red);
		for(Rectangle r : evils) {
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		return false;
	}

	@Override
	public boolean reset() {
		evils = new ArrayList<Rectangle>();
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				evils.add(new Rectangle(x,y, size_x, size_y));
			}
		}.load('s', game.getLevel().getLevel(), game);
		
		return false;
	}

	@Override
	public boolean isColidate(Rectangle r) {
		for(Rectangle r2 : evils) {
			if(r2.intersects(r)) return true;
		}
		return false;
	}

	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}

	@Override
	public int getType() {
		return GameObject.EVIL;
	}

	@Override
	public void changeDest(Rectangle r) {}

	@Override
	public int getCons() {
		return GameObject.CONS_MAUER;
	}

}
