import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Evil2: Kröte
 * Symbol: 'K'
 * Fall not down from Platform
 * @author erwin
 *
 */
public class Evil2 extends Evil{

	public Evil2(Game g) {
		super(g);
	}

	/**
	 * Symbol: 'K'
	 */
	@Override
	public boolean reset() {
		return reset('K');
	}


	/**
	 * Color: YELLOW
	 */
	@Override
	public boolean paint(Graphics g) {
		g.setColor(Color.YELLOW);
		paintEvils(g);
		return false;
	}


	/**
	 * Fall not down form Platform
	 */
	@Override
	public boolean calc(float time) {
		// Calculate movement
		super.calc(time);
		// Dont fall down
		for(Evils e : evils) {
			Rectangle r = (Rectangle) e.getPOS().clone();
			Rectangle r2 = (Rectangle) e.getPOS().clone();
			r2.width=1;
			r2.y++;
			r.x+=e.size_x-1;
			r.width=1;
			r.y++;
		
			// Calculate if Evil exits Platform
			boolean r_col=false, r2_col=false;
			for(GameObject g : game.getGameObjects()) {
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(r)) r_col=true;
				if(g.isColidate(r2)) r2_col=true;
			}
			// Check if exit Platform
			if(!r_col || !r2_col) {
				e.dest *= -1;
				e.x += time*speed*e.dest;
			}
		}
		return false;
	}


	/**
	 * Add more Score
	 */
	@Override
	public void destroy(Evils e, float time) {
		super.destroy(e, time);
		player.addScore(25);
	}
	

	

}
