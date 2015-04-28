package levelEdit;



import game.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
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
	
	public LevelEditor(JFrame gui, Game game) {
		this.gui = gui;
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		buffer = gui.getBufferStrategy();
		tools=new JFrame("Tools");
		tools.setBounds(gui.getWidth()+gui.getBounds().x, gui.getBounds().y, 120, gui.getHeight());
		tools.setLayout(new FlowLayout());
		tools.setUndecorated(true);
		addComponents();
		addListeners();
		active=false;
		level = new Level(gui);
		
	}
	
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
		
		file.add(fnew);
		file.add(load);
		file.add(save);
		file.add(exit);
		
		objects.add(player);
		objects.add(evil);
		objects.add(evil2);
		objects.add(evil3);
		objects.add(vEvil);
		objects.add(sEvil);
		objects.add(dEvil);
		objects.add(mauer);
		objects.add(vMauer);
		objects.add(live);
		objects.add(invincible);
		objects.add(coin50);
		objects.add(coin100);
		objects.add(coin500);
		
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
	}
	
	
	public void show() {
		cSel=' ';
		sel.setText("Selecetd: ' '");
		tools.setVisible(true);
		active=true;
		while(active) {
			display();
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void display() {
		Graphics g = buffer.getDrawGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		level.display(g);
		buffer.show();
	}
	
	private void onBack() {
		level.back();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if(s==back) {
			onBack();
		} else if(s==exit || s==exit_button) {
			if(JOptionPane.showConfirmDialog(tools, "Exit?")==JOptionPane.YES_OPTION) {
				active=false;
				tools.setVisible(false);
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
		}
		sel.setText("Selected: '" + cSel + "'");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!active) return;
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

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		button = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		button=0;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		MouseEvent e = new MouseEvent((Component) arg0.getSource(), arg0.getID(), arg0.getWhen(), arg0.getModifiers(), arg0.getX(), arg0.getY(), arg0.getClickCount(), false , button);
		mouseClicked(e);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
	
	
}
