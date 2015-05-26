package game;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import zip.ZipFile;
import zip.ZipLevelSet;

/**
 * The Menu
 * @author erwin
 *
 */
public class Menu implements MouseListener, MouseMotionListener, KeyListener{
	private GUI gui;
	private boolean visible;
	protected Game game;
	private boolean move;
	private boolean clicked;
	private int mouse_x=0, mouse_y=0;
	private int last_key;
	private char last_typed;
	
	/**
	 * Creates Object and add Listeners to GUI
	 * @param game
	 */
	public Menu(Game game) {
		this.gui = game.getGUI();
		this.game = game;
		this.move=false;
		visible=false;
		clicked=false;
		
		// Add Listeners
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.addKeyListener(this);
		
		// Attach Instance to Game
		game.addMenu(this);
		
	}
	
	public void hide() {
		visible=false;
		clicked=false;
	}
	
	public boolean continueGameMenu() {
		clicked=false;
		visible=true;
		while(true) {
			Graphics g = gui.getPaint();
			paintGeneric(g);
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-100,100,20), "Continue Game", g) && clicked) {
				clicked=false;
				return false;
				
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-70,100,20), "New Game", g) && clicked) {
				clicked=false;
				return true;
			}
			
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * paints Titlebar and Background
	 * @param g Graphics to Paint
	 */
	protected void paintGeneric(Graphics g) {
		// Draw Background
		g.setColor(Color.black);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		// Draw Titlebar
		g.setColor(Color.white);
		g.fillRect(0, 0, gui.getWidth(), 16);
		// Calc width of Text and display Title
		g.setColor(Color.black);
		int width=0;
		for(char c : Main.title.toCharArray()) {
			width+=g.getFontMetrics().charWidth(c);
		}
		g.drawString(Main.title, gui.getWidth()/2 - width/2, 12);
	}
	
	/**
	 * Show Menu when user press ESC to Pause Game
	 */
	public void showPauseMenu() {
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			// Continue
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-100,100,20), "Resume Game", g) && clicked) {
				visible=false;
				return;
			}
			// Exit
			if(drawButton(new Rectangle(gui.getWidth()/2-50,gui.getHeight()/2-70,100,20), "Exit to Menu", g) && clicked) {
				game.endGame();
				visible=false;
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
	
	public String EnterScore(boolean isHigh, Integer score) {
		String in = "";
		last_key = 0;
		last_typed='\0';
		char cur=' ';
		visible=true;
		while(last_key != KeyEvent.VK_ENTER) {
			Graphics g = gui.getPaint();
			paintGeneric(g);
			
			// Score
			drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-100,120,20), "SCORE: " + score.toString(), g);
			
			// Name
			if(isHigh) {
				if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-70,120,20), "Name: " + in + cur, g)) {
					if(cur==' ') {
						cur='_';
					} else {
						cur=' ';
					}
					if(last_typed!='\0' && last_typed!=';' && last_typed!=':' && last_typed!='-' && last_typed!='\n') {
						in += last_typed;
						last_typed='\0';
					}
					if(last_key!=0) {
						if(last_key==KeyEvent.VK_BACK_SPACE) {
							in = in.substring(0, in.length()-2);
							last_key=0;
						}
					}
				}
			}			
			// OK
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-40,120,20), "OK", g) && clicked) {
				break;
			}
			gui.finishPaint();
			clicked=false;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		clicked=false;
		visible=false;
		return in;
	}
	
	/**
	 * Show Message in Game
	 * Message are not automatic redrawn
	 * @param msg Message
	 * @param g Graphics to Paint
	 */
	protected void showMSG(String msg, Graphics g) {
		int x = gui.getWidth()/2 -100;
		int y = gui.getHeight()/2 -50;
		g.setColor(Color.white);
		g.fillRect(x, y, 200, 100);
		x += 20;
		y += 25;
		g.setColor(Color.black);
		g.drawString(msg, x, y);
	}
	
	/**
	 * Set Key
	 * @param Key key to set
	 * @param key_name name of key
	 */
	protected void setKey(int Key, String key_name) {
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
		// Save Key
		game.getSave().setConf(Key, String.valueOf(last_key));
	}
	
	/**
	 * Show OptionsMenu
	 * @return
	 */
	protected boolean showKeyMenu() {
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			// UP
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-100,120,20), "KEY: UP", g) && clicked) {
				setKey(Save.KEY_UP, "UP");
			}
			// Right
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-70,120,20), "KEY: RIGHT", g) && clicked) {
				setKey(Save.KEY_RIGHT, "RIGHT");
			}
			// Left
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-40,120,20), "KEY: LEFT", g) && clicked) {
				setKey(Save.KEY_LEFT, "LEFT");
			}
			String size="800x592";
			String res = game.getSave().getConf(Save.RES);
			if(res==null) res="0";
			if(res.length()<1) res="0";
			switch(Integer.valueOf(res)) {
			case 0:
				break;
			case 1:
				size="1000x740";
				break;
			case 2:
				size="600x444";
				break;
			}
			// Resultion
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-10,120,20), "SIZE: " + size, g) && clicked) {
				int save_res=Integer.valueOf(res)+1;
				if(save_res>2) save_res=0;
				game.getSave().setConf(Save.RES, String.valueOf(save_res));
				return true;
			}
			// Exit
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2+20,120,20), "Exit Options", g) && clicked) {
				clicked=false;
				return false;
			}
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Show Hiscore Menu
	 */
	protected void showHighScore() {
		String[] scores = new String[5];
		for(int i=0;i<5;i++) {
			Save.Score score = game.getSave().getHighScore(i);
			scores[i] = score.getName() + ": " + String.valueOf(score.getScore());
		}
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			// Hiscores
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2-70,160,20), scores[0], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2-40,160,20), scores[1], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2-10,160,20), scores[2], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2+20,160,20), scores[3], g);
			drawButton(new Rectangle(gui.getWidth()/2-80,gui.getHeight()/2+50,160,20), scores[4], g);
			// Exit Button
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
	
	protected void startLevelEditor() {
		visible=false;
		new levelEdit.LevelEditor(gui, game).show();
		visible=true;
	}
	
	protected void showExtrasMenu() {
		clicked=false;
		visible=true;
		while(true) {
			Graphics g = gui.getPaint();
			paintGeneric(g);
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-100,120,20), "Custom Level", g) && clicked) {
				clicked=false;
				visible=false;
				JFileChooser file=new JFileChooser();
				if(file.showOpenDialog(gui)==JFileChooser.APPROVE_OPTION) {
					File f = file.getSelectedFile();
					if(!f.exists()) {
						visible=true;
						continue;
					}
					Scanner in = null;
					try {
						in = new Scanner(f);
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						PrintWriter pr = new PrintWriter(out);
						while(in.hasNextLine()) {
							pr.println(in.nextLine());
						}
						pr.close();
						game.start(new CustomLevel(game, out.toByteArray()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						if(in!=null)
							in.close();
					}
				}
				
				
				visible=true;
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-70,120,20), "Level Editor", g) && clicked) {
				clicked=false;
				startLevelEditor();
			}
			// Custom LevelSet
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-40,120,20), "Custom LevelSet", g) && clicked) {
				clicked=false;
				JFileChooser file = new JFileChooser();
				if(file.showOpenDialog(game.getGUI())!=JFileChooser.APPROVE_OPTION) continue;
				try {
					ZipFile zip = new ZipFile(file.getSelectedFile());
					if(zip.getFile("save.txt")!=null) {
						if(continueGameMenu()) {
							zip.delete("save.txt");
						}
					}
					visible=false;
					game.start(new ZipLevelSet(game, zip));
					visible=true;
					zip.save(file.getSelectedFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-10,120,20), "Exit Extras", g) && clicked) {
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
	
	
	/**
	 * Show Main Menu
	 * @param show_options
	 * @return
	 */
	public boolean showMainMenu(boolean show_options) {
		while(true) {
			visible=true;
			Graphics g = gui.getPaint();
			paintGeneric(g);
			// New Game
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-100,120,20), "New Game", g) && clicked) {
				visible=false;
				game.getLevel().newGame(true);
				game.start();
				clicked=false;
			}
			// Continue Game
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-70,120,20), "Continue Game", g) && clicked) {
				visible=false;
				game.start();
				clicked=false;
			}
			// Extras
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-40,120,20), "Extras", g) && clicked) {
				showExtrasMenu();
			}
			// Hiscore
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2-10,120,20), "Highscore", g) && clicked) {
				clicked=false;
				showHighScore();
			}
			// Auto Open Options on Resultion change
			if(show_options) {
				if(showKeyMenu()) return true;
				show_options=false;
			}
			// Options Menu
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2+20,120,20), "Options", g) && clicked) {
				clicked=false;
				if(showKeyMenu()) return true;
			}
			
			// Clear SaveGame
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2+50,120,20), "Clear Save", g) && clicked) {
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
					if(score!=null)
						game.getSave().setConf(Save.HIGHSCORE, score);
				}
			}
			// Exits the Game
			if(drawButton(new Rectangle(gui.getWidth()/2-60,gui.getHeight()/2+80,120,20), "Exit Game", g) && clicked) {
				return false;
			}
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Show TextScreen for Story
	 * @param text
	 */
	public void showScreen(InputStream text) {
		ArrayList<String> lines = new ArrayList<String>();
		ArrayList<DisplayedImage> imgs = new ArrayList<DisplayedImage>();
		String line="";
		String display="";
		Scanner s = new Scanner(text);
		String size="22";
		
		// Set Size for Text
		int size_x = gui.getWidth()/50;
		if(size_x<16) {
			size="16";
		} else if(size_x>19) {
			size="28";
		}
		while(true) {
			visible=true;
			// Set Font Size
			Graphics g = gui.getPaint();
			paintGeneric(g);
			Font old = g.getFont();
			g.setFont(Font.decode(old.getFontName() + " " + size));
			
			// Set Font Color
			g.setColor(Color.white);
			int x=20, y=60;
			
			// Draw Loaded Lines
			if(lines.size()>0) {
				for(String str: lines) {
					g.drawString(str, x, y);
					y+=g.getFontMetrics().getHeight()+5;
				}
			}
			
			// Draw Loaded Images
			if(imgs.size()>0) {
				for(DisplayedImage i : imgs) {
					i.display(g);
				}
			}
			
			// Draw current Line
			if(display.length()>0) {
				g.drawString(display, x, y);
			}
			
			// Read Line
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
					// Load Image
					if(line.startsWith("img:")) {
						String[] split = line.split(":");
						try {
							BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/res/" + split[1]));
							imgs.add(new DisplayedImage(x, y, img));
						} catch (IOException e) {
							e.printStackTrace();
						}
						line="";
					}
				}
			}
			// Set Font for Button
			g.setFont(old);
			if(drawButton(new Rectangle(gui.getWidth()-100,gui.getHeight()-30,90,20), "Next ->", g) && clicked) {
				break;
			}
			gui.finishPaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		s.close();
		visible=false;
	}
	
	/**
	 * Draw Button for Menu
	 * @param r Coordinates and Size of Button
	 * @param text Text for Screen
	 * @param g Graphics to Paint
	 * @return
	 */
	protected boolean drawButton(Rectangle r, String text, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		LineMetrics lm = fm.getLineMetrics(text, g);
		int width=0;
		for(char c : text.toCharArray()) {
			width+=fm.charWidth(c);
		}



		Color color=Color.white;
		
		// Check Mouseover
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

	/**
	 * Do Noting
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}

	/**
	 * Do Noting
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}

	/**
	 * Do Noting
	 */
	@Override
	public void mouseExited(MouseEvent e) {}

	/**
	 * Set Move and clicked for Buttons and Titlebar
	 */
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

	/**
	 * Unset Move and clicked
	 */
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
		if(!visible) return;
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

	/**
	 * set mouse_x and mouse_y
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		mouse_x=e.getX();
		mouse_y=e.getY();
	}

	/**
	 * Saves last key_code
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		last_key=e.getKeyCode();
	}

	/**
	 * Do Noting
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {}

	/**
	 * Do Noting
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		last_typed=e.getKeyChar();
	}
	
	/**
	 * DisplayImage for showScreen
	 * @author erwin
	 *
	 */
	class DisplayedImage {
		private BufferedImage image;
		private int x, y;
		
		/**
		 * Create Instance
		 * @param x
		 * @param y
		 * @param image Image to Paint
		 */
		public DisplayedImage(int x, int y, BufferedImage image) {
			this.x =x;
			this.y=y;
			this.image=image;
		}
		/**
		 * Display the Image
		 * @param g Graphics to Display
		 */
		public void display(Graphics g) {
			g.drawImage(image, x, y, null);
		}
		
	}

}
