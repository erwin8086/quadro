import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;


public class Menu implements MouseListener, MouseMotionListener, KeyListener{
	private GUI gui;
	private boolean visible;
	private Game game;
	private boolean move;
	private boolean clicked;
	private int mouse_x=0, mouse_y=0;
	private int last_key;
	
	public Menu(GUI gui, Game game) {
		this.gui = gui;
		this.game = game;
		this.move=false;
		visible=false;
		clicked=false;
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.addKeyListener(this);
		game.addMenu(this);
		
	}
	
	/**
	 * paints Titlebar and Background
	 * @param g Grphics to Paint
	 */
	private void paintGeneric(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		g.setColor(Color.white);
		g.fillRect(0, 0, gui.getWidth(), 16);
		g.setColor(Color.black);
		int width=0;
		for(char c : Main.title.toCharArray()) {
			width+=g.getFontMetrics().charWidth(c);
		}
		g.drawString(Main.title, gui.getWidth()/2 - width/2, 12);
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
	
	private void showMSG(String msg, Graphics g) {
		int x = gui.getWidth()/2 -100;
		int y = gui.getHeight()/2 -50;
		g.setColor(Color.white);
		g.fillRect(x, y, 200, 100);
		x += 20;
		y += 25;
		g.setColor(Color.black);
		g.drawString(msg, x, y);
	}
	
	private void setKey(int Key, String key_name) {
		last_key=0;
		while(last_key==0) {
			Graphics g = gui.getPaint();
			paintGeneric(g);
			showMSG("Press Key to Set for " + key_name, g);
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		game.getSave().setConf(Key, String.valueOf(last_key));
	}
	
	private void showKeyMenu() {
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-100,120,20), "KEY: UP", g) && clicked) {
				setKey(Save.KEY_UP, "UP");
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-70,120,20), "KEY: RIGHT", g) && clicked) {
				setKey(Save.KEY_RIGHT, "RIGHT");
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-40,120,20), "KEY: LEFT", g) && clicked) {
				setKey(Save.KEY_LEFT, "LEFT");
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-10,120,20), "Exit Options", g) && clicked) {
				clicked=false;
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
	
	private void showHighScore() {
		String[] scores = new String[5];
		for(int i=0;i<5;i++) {
			Save.Score score = game.getSave().getHighScore(i);
			scores[i] = score.getName() + ": " + String.valueOf(score.getScore());
		}
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2-70,160,20), scores[0], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2-40,160,20), scores[1], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2-10,160,20), scores[2], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2+20,160,20), scores[3], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2+50,160,20), scores[4], g);
			if(drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2+80,160,20), "Exit Highscore", g) && clicked) {
				clicked=false;
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
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-100,120,20), "New Game", g) && clicked) {
				visible=false;
				System.out.println("Game Starts");
				game.getLevel().newGame();
				game.start();
				clicked=false;
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-70,120,20), "Continue Game", g) && clicked) {
				visible=false;
				game.start();
				clicked=false;
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-40,120,20), "Highscore", g) && clicked) {
				clicked=false;
				showHighScore();
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-10,120,20), "Options", g) && clicked) {
				clicked=false;
				showKeyMenu();
			}
			
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2+20,120,20), "Clear Save", g) && clicked) {
				last_key=0;
				while(last_key==0) {
					Graphics g2 = gui.getPaint();
					paintGeneric(g2);
					showMSG("Press Y to Delete Game", g2);
					gui.finishPaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(last_key==KeyEvent.VK_Y) {
					String score = game.getSave().getConf(Save.HIGHSCORE);
					game.getSave().clear();
					game.getSave().setConf(Save.HIGHSCORE, score);
				}
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2+50,120,20), "Exit Game", g) && clicked) {
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
	
	public void showScreen(InputStream text) {
		ArrayList<String> lines = new ArrayList<String>();
		String line="";
		String display="";
		Scanner s = new Scanner(text);
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			g.setColor(Color.white);
			int x=20, y=60;
			if(lines.size()>0) {
				for(String str: lines) {
					g.drawString(str, x, y);
					y+=g.getFontMetrics().getHeight()+5;
				}
			}
			
			if(display.length()>0) {
				g.drawString(display, x, y);
			}
			
			if(line.length()>0) {
				display += line.substring(0, 1);
				line = line.substring(1);
			} else {
				if(display.length()>0) {
					lines.add(display);
				}
				display="";
				if(s.hasNextLine()) {
					line = s.nextLine();
				}
			}
			
			if(drawButton(new Rectangle(gui.getWidth()-100,gui.getHeight()-30,90,20), "Next ->", g) && clicked) {
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
	
	private boolean drawButton(Rectangle r, String text, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		LineMetrics lm = fm.getLineMetrics(text, g);
		int width=0;
		for(char c : text.toCharArray()) {
			width+=fm.charWidth(c);
		}



		Color color=Color.white;
		boolean mouseover=false;
		if(mouse_x>r.x && mouse_x<r.x+r.width && mouse_y>r.y && mouse_y<r.y+r.height) {
			mouseover=true;
			color=Color.pink;
		}
		g.setColor(color);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(Color.black);
		g.drawString(text, r.x + r.width/2 - width/2, r.y + r.height/2 + (int)Math.round(lm.getHeight()/2.8f));
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

	@Override
	public void keyPressed(KeyEvent e) {
		last_key=e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
