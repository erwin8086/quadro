import java.awt.Rectangle;
import java.util.ArrayList;


public class DublicatingEvil extends StandingEvil{
	
	private float last_dub=0;

	public DublicatingEvil(Game game) {
		super(game);
	}

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
