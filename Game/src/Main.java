
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
		// Create GUI
		GUI gui = new GUI();
		Game game = new Game(gui, new Save());
		Menu menu = new Menu(gui,game);
		menu.showMainMenu();
		System.out.println("Game Exits OK");
		System.exit(0);
	}

}
