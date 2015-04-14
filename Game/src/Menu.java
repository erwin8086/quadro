import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Menu implements MouseListener, MouseMotionListener{
	private GUI gui;
	private boolean visible;
	private Game game;
	private boolean move;
	private boolean clicked;
	private int mouse_x=0, mouse_y=0;
	
	public Menu(GUI gui, Game game) {
		this.gui = gui;
		this.game = game;
		this.move=false;
		visible=false;
		clicked=false;
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		game.addMenu(this);
		
	}
	
	private void paintGeneric(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		g.setColor(Color.white);
		g.fillRect(0, 0, gui.getWidth(), 16);
		g.setColor(Color.black);
		g.drawString(Main.title, gui.getWidth()/2 - 24, 12);
	}
	
	public void showPauseMenu() {
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-100,100,20), "Resume Game", g) && clicked) {
				visible=false;
				return;
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-70,100,20), "Exit to Menu", g) && clicked) {
				game.getSave().setConf(Save.LEVEL, String.valueOf(game.getLevel().getLevelCount()));
				game.endGame();
				return;
			}
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void showMainMenu() {
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-100,120,20), "New Game", g) && clicked) {
				visible=false;
				System.out.println("Game Starts");
				game.getSave().setConf(Save.LEVEL, String.valueOf(0));
				game.start();
				clicked=false;
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-70,120,20), "Continue Game", g) && clicked) {
				visible=false;
				game.start();
				clicked=false;
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-10,120,20), "Exit Game", g) && clicked) {
				System.exit(0);
			}
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean drawButton(Rectangle r, String text, Graphics g) {
		Color color=Color.white;
		boolean mouseover=false;
		if(mouse_x>r.x && mouse_x<r.x+r.width && mouse_y>r.y && mouse_y<r.y+r.height) {
			mouseover=true;
			color=Color.pink;
		}
		g.setColor(color);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(Color.black);
		g.drawString(text, r.x + r.width/2 - text.length()*8/2, r.y + r.height/2 + 4);
		return mouseover;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(e.getY() < 16) {
				move=true;
			} else {
				clicked=true;
			}
		}
		mouse_x=e.getX();
		mouse_y=e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		move=false;
		clicked=false;
	}

	/**
	 * Move Window
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if(!visible) {
			return;
		}
		if(move) {
			System.out.println("Move");
			Rectangle bounds = gui.getBounds();
			if(mouse_x>e.getX()) {
				bounds.x -= mouse_x-e.getX();
			}
			if(mouse_x<e.getX()) {
				bounds.x += e.getX()-mouse_x;
			}
			if(mouse_y>e.getY()) {
				bounds.y -= mouse_y-e.getY();
			}
			if(mouse_y<e.getY()) {
				bounds.y += e.getY()-mouse_y;
			}
			gui.setBounds(bounds);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse_x=e.getX();
		mouse_y=e.getY();
	}

}
