package game;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Evil that are Static like StaticEvil and
 * Generates a Evil all 15 secounds
 * @author erwin
 *
 */
public class DublicatingEvil extends StandingEvil{
	
	// Last Dublicating
	private float last_dub=0;

	/**
	 * Generates Dublicating Evil
	 * @param game The Game Instance
	 */
	public DublicatingEvil(Game game) {
		super(game);
	}

	/**
	 * Calculate Dublicating and
	 * creates the Evil
	 */
	@Override
	public boolean calc(float time) {
		for(Rectangle r : evils) {
			last_dub+=time;
			if(last_dub>15) {
				last_dub=0;
				game.getEvil().addEvil(r.x+r.width, r.y);
			}
		}
		return super.calc(time);
	}
	
	/**
	 * Resets the Evil on Level Load
	 */
	@Override
	public boolean reset() {
		evils = new ArrayList<Rectangle>();
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x, int y) {
				evils.add(new Rectangle(x,y, size_x, size_y));
			}
		}.load('D', game.getLevel().getLevel(), game);
		
		return false;
	}

}
