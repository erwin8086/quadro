import java.awt.Color;
import java.awt.Graphics;

/**
 * Evil3: Speedy
 * Symbol: 'S'
 * @author erwin
 *
 */
public class Evil3 extends Evil{

	public Evil3(Game g) {
		super(g);
	}

	/**
	 * Has 2 Lives
	 */
	@Override
	public void destroy(Evils e, float time) {
		if(e.dest==1 || e.dest==-1) {
			e.dest*=2;
		} else {
			super.destroy(e,time);
			player.addScore(25);
		}
		player.jump(100, time);
		player.addScore(25);
	}

	/**
	 * Paint Red or PINK(if SPEED)
	 */
	@Override
	public boolean paint(Graphics g) {
		for( Evils e : evils) {
			if(e.dest==1 || e.dest==-1) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.PINK);
			}
			g.fillRect((int)e.x, (int)e.y, e.size_x, e.size_y);
		}
		return false;
	}

	/**
	 * Symbol: 'S'
	 */
	@Override
	public boolean reset() {
		return reset('S');
	}

	

}
