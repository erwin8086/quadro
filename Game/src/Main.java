
/**
 * Main Class Contains Game LOOP
 * @author erwin
 *
 */
public class Main {
	public static String title = "Game";
	
	/**
	 * Main Function
	 * @param args
	 */
	public static void main(String[] args) {
		boolean keys=false;
		while(true) {
			Save save = new Save();
			// Create GUI
			String res = save.getConf(Save.RES);
			if(res==null) res="0";
			if(res.length()<1) res="0";
			GUI gui = new GUI(Integer.valueOf(res));
			Game game = new Game(gui, save);
			Menu menu = new Menu(gui,game);
			if(menu.showMainMenu(keys)) {
				keys=true;
			} else {
				break;
			}
			gui.setVisible(false);
		}
		System.out.println("Game Exits OK");
		System.exit(0);
	}

}
