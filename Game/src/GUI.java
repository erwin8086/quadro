import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * Graphical Window for The Game
 * @author erwin
 *
 */
public class GUI extends JFrame implements KeyListener {
	private static final long serialVersionUID = 6411499808530678723L;
	// Size of Window
	private final int size_x=800, size_y=600;
	private BufferStrategy buffer;
	private Game game;
	/**
	 * inits Main GUI
	 */
	public GUI() {
		// Set Options
		this.setBounds(30,30,size_x, size_y);
		this.setResizable(false);
		this.setTitle("Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// NO Titlebar
		this.setUndecorated(true);
		// Add KeyListener for Exit on ESC
		this.addKeyListener(this);
		
		// Set Visible
		this.setVisible(true);
		
		
		// Create BufferStrategy
		this.createBufferStrategy(2);
		buffer = this.getBufferStrategy();
		
	}
	
	public void addGame(Game game) {
		this.game=game;
	}
	
	/**
	 * Exit on Escape
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			if(game!=null)
				game.pauseGame();
		}
	}
	/**
	 * Do Noting
	 */
	@Override
	public void keyReleased(KeyEvent e) {}
	/**
	 * Do Noting
	 */
	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Get Graphics Object for Buffer
	 * @return Graphics
	 */
	public Graphics getPaint() {
		return buffer.getDrawGraphics();
	}
	
	/**
	 * Switch Buffers
	 */
	public void finishPaint() {
		buffer.show();
	}

}
