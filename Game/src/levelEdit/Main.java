package levelEdit;

import javax.swing.JFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setBounds(0, 0, 800, 592);
		f.setUndecorated(true);
		f.setVisible(true);
		f.createBufferStrategy(2);
		new LevelEditor(f).show();
		System.exit(0);
	}

}
