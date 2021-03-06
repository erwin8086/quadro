package levelEdit;



import game.CustomLevel;
import game.Game;
import game.Main;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.ByteArrayOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
/**
 * Level Editor for Quadro
 * @author erwin
 * @param <Game>
 *
 */
public class LevelEditor implements ActionListener, MouseListener, MouseMotionListener {
	private JFrame gui;
	private JFrame tools;
	private TitleBar title;
	private BufferStrategy buffer;
	
	// Tool Components
	private JMenuBar menuBar;
	// File Menu
	private JMenu file;
	private JMenuItem exit;
	private JMenuItem save;
	private JMenuItem load;
	private JMenuItem fnew;
	private JMenuItem start;
	// Objects Menu
	private JMenu objects;
	private JMenuItem player;
	private JMenuItem evil; // Generic Evil
	private JMenuItem evil2; // Kröte
	private JMenuItem evil3; // Speedy
	private JMenuItem vEvil;
	private JMenuItem mauer; // Wall
	private JMenuItem vMauer;
	private JMenuItem sEvil; // Static Evil
	private JMenuItem dEvil; // Dublicating Evil
	private JMenuItem live;
	private JMenuItem invincible;
	private JMenuItem coin50;
	private JMenuItem coin500;
	private JMenuItem coin100;
	private JMenuItem custom;
	// Labels
	private JLabel sel;
	private JButton back;
	private JButton exit_button;
	private JButton mauer_button;
	private JButton evil_button;
	private JButton evil2_button;
	private JButton vEvil_button;
	private JButton evil3_button;
	private JButton sEvil_button;
	
	private boolean active;
	
	private Level level;
	
	private char cSel;
	
	private int button=0;
	
	private Game game;
	
	private boolean start_level;
	
	/**
	 * Create LevelEditor
	 * @param gui the Main Window of Game
	 * @param game
	 */
	public LevelEditor(JFrame gui, Game game) {
		this.gui = gui;
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		buffer = gui.getBufferStrategy();
		tools=new JFrame("Tools");
		tools.setBounds(gui.getWidth()+gui.getBounds().x, gui.getBounds().y, 120, gui.getHeight());
		tools.setLayout(new FlowLayout());
		tools.setUndecorated(true);
		title = new TitleBar(Main.title + ": Level Editor");
		Rectangle r = title.getBounds();
		r.x = gui.getX();
		r.y = gui.getY()-12;
		r.width=gui.getWidth()+tools.getWidth();
		r.height=12;
		title.setBounds(r);
		title.setUndecorated(true);
		addComponents();
		addListeners();
		active=false;
		level = new Level(gui);
		this.game=game;
		
	}
	
	/**
	 * Starts Level from Editor
	 */
	private void startLevel() {
		active=false;
		tools.setVisible(false);
		title.setVisible(false);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		level.save(out);
		game.start(new CustomLevel(game, out.toByteArray()));
		tools.setVisible(true);
		title.setVisible(true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		active=true;
		start_level=false;
	}
	
	/**
	 * Add Components to Tools
	 */
	private void addComponents() {
		menuBar = new JMenuBar();
		file = new JMenu("File");
		objects = new JMenu("Objects");
		
		fnew = new JMenuItem("New");
		save = new JMenuItem("Save");
		exit = new JMenuItem("Exit");
		load = new JMenuItem("Load");
		start = new JMenuItem("Start");
		
		player = new JMenuItem("Player");
		evil = new JMenuItem("Simple Evil");
		evil2 = new JMenuItem("Kröte");
		evil3 = new JMenuItem("Speedy");
		vEvil = new JMenuItem("Vertical Evil");
		mauer = new JMenuItem("Mauer");
		vMauer = new JMenuItem("Vertical mauer");
		sEvil = new JMenuItem("Static Evil");
		dEvil = new JMenuItem("Dublicating Evil");
		
		live = new JMenuItem("Live");
		invincible=new JMenuItem("Invincibility");
		coin50 = new JMenuItem("Coin50");
		coin100 = new JMenuItem("Coin100");
		coin500 = new JMenuItem("Coin500");
		
		sel = new JLabel("Selected: ' '");
		back = new JButton("Back");
		exit_button=new JButton("Exit");
		mauer_button=new JButton("Mauer");
		evil_button=new JButton("Simple Evil");
		evil2_button=new JButton("Kröte");
		evil3_button=new JButton("Speedy");
		vEvil_button=new JButton("Vertical Evil");
		sEvil_button=new JButton("Static Evil");
		custom=new JMenuItem("Custom");
		
		file.add(fnew);
		file.addSeparator();
		file.add(start);
		file.add(load);
		file.add(save);
		file.addSeparator();
		file.add(exit);
		
		objects.add(player);
		objects.addSeparator();
		objects.add(evil);
		objects.add(evil2);
		objects.add(evil3);
		objects.add(vEvil);
		objects.add(sEvil);
		objects.add(dEvil);
		objects.addSeparator();
		objects.add(mauer);
		objects.add(vMauer);
		objects.addSeparator();
		objects.add(live);
		objects.add(invincible);
		objects.add(coin50);
		objects.add(coin100);
		objects.add(coin500);
		objects.add(custom);
		
		menuBar.add(file);
		menuBar.add(objects);
		
		tools.setJMenuBar(menuBar);
		
		tools.add(sel);
		tools.add(back);
		tools.add(mauer_button);
		tools.add(evil_button);
		tools.add(evil2_button);
		tools.add(evil3_button);
		tools.add(vEvil_button);
		tools.add(sEvil_button);
		tools.add(exit_button);
	}
	
	/**
	 * Add Listeners to Components
	 */
	private void addListeners() {
		exit.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
		evil.addActionListener(this);
		evil2.addActionListener(this);
		evil3.addActionListener(this);
		vEvil.addActionListener(this);
		dEvil.addActionListener(this);
		sEvil.addActionListener(this);
		player.addActionListener(this);
		mauer.addActionListener(this);
		vMauer.addActionListener(this);
		back.addActionListener(this);
		coin50.addActionListener(this);
		coin500.addActionListener(this);
		coin100.addActionListener(this);
		live.addActionListener(this);
		invincible.addActionListener(this);
		fnew.addActionListener(this);
		exit_button.addActionListener(this);
		mauer_button.addActionListener(this);
		evil_button.addActionListener(this);
		evil2_button.addActionListener(this);
		evil3_button.addActionListener(this);
		vEvil_button.addActionListener(this);
		sEvil_button.addActionListener(this);
		start.addActionListener(this);
		custom.addActionListener(this);
	}
	
	/**
	 * Show LevelEditor
	 */
	public void show() {
		cSel=' ';
		sel.setText("Selecetd: ' '");
		tools.setVisible(true);
		title.setVisible(true);
		active=true;
		start_level=false;
		while(active) {
			display();
			if(start_level) startLevel();
			title.updatePOS();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Display the Level
	 */
	private void display() {
		Graphics g = buffer.getDrawGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		level.display(g);
		buffer.show();
	}
	
	/**
	 * Undo last Step
	 */
	private void onBack() {
		level.back();
	}

	/**
	 * The ActionListener for Components
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get Source
		Object s = e.getSource();
		
		// Check for Source and do Action
		if(s==back) {
			onBack();
		} else if(s==exit || s==exit_button) {
			if(JOptionPane.showConfirmDialog(tools, "Exit?")==JOptionPane.YES_OPTION) {
				active=false;
				tools.setVisible(false);
				title.setVisible(false);
			}
		} else if(s==load) {
			JFileChooser f = new JFileChooser();
			int r = f.showOpenDialog(tools);
			if(r==JFileChooser.APPROVE_OPTION) {
				level.load(f.getSelectedFile());
			}
			
		} else if(s==save) {
			JFileChooser f = new JFileChooser();
			int r = f.showSaveDialog(tools);
			if(r==JFileChooser.APPROVE_OPTION) {
				level.save(f.getSelectedFile());
			}
			
		} else if(s==evil || s==evil_button) {
			cSel = 'G';
		} else if(s==evil2 || s==evil2_button) {
			cSel = 'K';
		} else if(s==evil3 || s==evil3_button) {
			cSel = 'S';
		} else if(s==vEvil || s==vEvil_button) {
			cSel = 'V';
		} else if(s==dEvil) {
			cSel = 'D';
		} else if(s==sEvil || s==sEvil_button) {
			cSel = 's';
		} else if(s==mauer || s==mauer_button) {
			cSel = '#';
		} else if(s==vMauer) {
			cSel = 'm';
		} else if(s==player) {
			cSel = 'P';
		} else if(s==coin50) {
			cSel = '5';
		} else if(s==coin100) {
			cSel = '1';
		} else if(s==coin500) {
			cSel = '%';
		} else if(s==live) {
			cSel = 'L';
		} else if(s==invincible) {
			cSel = 'I';
		} else if(s==fnew) {
			if(JOptionPane.showConfirmDialog(tools, "Clear the Level?")==JOptionPane.YES_OPTION) {
				level.clear();
			}
		} else if(s==start) {
			start_level=true;
		} else if(s==custom) {
			String c = JOptionPane.showInputDialog("Input the char");
			if(c.length()>0)
				cSel=c.toCharArray()[0];
		}
		sel.setText("Selected: '" + cSel + "'");
	}

	/**
	 * On Mouse Clicked sets Char in Level
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!active) return;
		if(e.getX()>gui.getWidth()-1) return;
		if(e.getY()>gui.getHeight()-1) return;
		if(e.getX()<0) return;
		if(e.getY()<0) return;
		if(e.getButton()==MouseEvent.BUTTON1) {
			if(level.get(e.getX(), e.getY())==cSel) return;
			boolean setPlay=false;
			if(cSel=='P') {
				Rectangle r = level.find('P');
				if(r!=null) {
					level.set(r.x, r.y, ' ', true);
					setPlay=true;
				}
			}
			level.set(e.getX(), e.getY(), cSel, !setPlay);
		} else if(e.getButton()==MouseEvent.BUTTON3) {
			level.set(e.getX(), e.getY(), ' ', true);
		}
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	/**
	 * Do Nothing
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {}

	/**
	 * Save Button
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		button = e.getButton();
	}

	/**
	 * Resets Button
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		button=0;
	}

	/**
	 * Sets Level on Mouse Dragged
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		MouseEvent e = new MouseEvent((Component) arg0.getSource(), arg0.getID(), arg0.getWhen(), arg0.getModifiers(), arg0.getX(), arg0.getY(), arg0.getClickCount(), false , button);
		mouseClicked(e);
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	
	/**
	 * The Titlebar for LevelEditor
	 * @author erwin
	 *
	 */
	private class TitleBar extends JFrame implements MouseMotionListener {
		private static final long serialVersionUID = -2592627871847839810L;
		private String title;
		private int oldx=0, oldy=0;
		
		public TitleBar(String title) {
			this.title = title;
			addMouseMotionListener(this);
		}

		/**
		 * Paints the TitleBar
		 */
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.white);
			FontMetrics fm = g.getFontMetrics();
			int width=0;
			for(char c : title.toCharArray()) {
				width+=fm.charWidth(c);
			}
			int height=Math.round(fm.getHeight()/1.75f);
			g.drawString(title, getWidth()/2-width/2, height);
		}
		
		/**
		 * Update Position of Window
		 */
		public void updatePOS() {
			Rectangle[] bounds = new Rectangle[3];
			bounds[0] = getBounds();
			bounds[1] = tools.getBounds();
			bounds[2] = gui.getBounds();
			bounds[2].x = bounds[0].x;
			bounds[2].y = bounds[0].y+getHeight();
			bounds[1].x = bounds[0].x + gui.getWidth();
			bounds[1].y = bounds[0].y+getHeight();
			tools.setBounds(bounds[1]);
			gui.setBounds(bounds[2]);

		}

		/**
		 * Move Window
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if( (oldx!=e.getX()) || (oldy!=e.getY()) ) {
				Rectangle r = getBounds();
				if(oldx>e.getX()) {
					r.x -= oldx-e.getX();
				} else {
					r.x += e.getX()-oldx;
				}
				if(oldy>e.getY()) {
					r.y -= oldy-e.getY();
				} else {
					r.y += e.getY()-oldy;
				}
				setBounds(r);
			}
		}

		/**
		 * Sets oldx and oldy
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			oldx=e.getX();
			oldy=e.getY();
		}
		
		
	}
	
	
}
