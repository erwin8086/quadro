package mod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
/**
 * A Class used by TestMod
 * @author erwin
 *
 */
public class Test {

	/**
	 * Show Message
	 * @param menu
	 */
	public void showMSG(game.Menu menu) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter pr = new PrintWriter(out);
		pr.println("Hello from Mod");
		pr.close();
		menu.showScreen(new ByteArrayInputStream(out.toByteArray()));
	}
}
