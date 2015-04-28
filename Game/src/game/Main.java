package game;

/**
 * Main Class
 * Creates GUI, Save, Game
 * @author erwin
 *
 */
public class Main {
	// The Title of the Game
	public static String title = "Quadro";
	
	/**
	 * Main Function
	 * @param args
	 */
	public static void main(String[] args) {
		// Show KeyMenu on Game starts
		boolean keys=false;
		while(true) {
			Save save = new Save();
			// Create GUI
			
			// Load Resulution from Save
			String res = save.getConf(Save.RES);
			if(res==null) res="0";
			if(res.length()<1) res="0";
			GUI gui = new GUI(Integer.valueOf(res));
			
			// Creates Game and Menu
			Game game = new Game(gui, save);
			Menu menu = new Menu(game);
			
			// Show The Menu
			if(menu.showMainMenu(keys)) {
				keys=true;
			} else {
				break;
			}
			gui.setVisible(false);
		}
		System.exit(0);
	}

}
