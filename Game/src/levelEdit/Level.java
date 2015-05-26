package levelEdit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
/**
 * The Level loaded in Editor
 * @author erwin
 *
 */
public class Level {
	// The Level
	private char[][] level = new char[37][50];
	private int size_x, size_y;
	private LastAction last=null;
	
	/**
	 * Creates Instance and clear()
	 * @param frame
	 */
	public Level(JFrame frame) {
		clear();
		size_x=frame.getWidth()/50;
		size_y=frame.getHeight()/37;
	}
	
	/**
	 * Clear the Level loaded
	 */
	public void clear() {
		last=null;
		for(int y=0;y<37;y++) {
			for(int x=0;x<50;x++) {
				level[y][x] = ' ';
			}
		}
	}
	
	/**
	 * Find char in Level and return Position
	 * @param f the char
	 * @return null if cold not found
	 */
	public Rectangle find(char f) {
		for(int y=0;y<37;y++) {
			for(int x=0;x<50;x++) {
				if(level[y][x]==f) {
					return new Rectangle(x*size_x, y*size_y, size_x, size_y);
				}
			}
		}
		return null;
	}
	
	/**
	 * set Char in Level
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param sel the char to set
	 * @param user is User?
	 */
	public void set(int x, int y, char sel, boolean user) {
		x /= size_x;
		y /= size_y;
		LastAction last_new = new LastAction();
		if(last!=null) {
			last_new.pre = last;
		}
		last_new.x=x;
		last_new.y=y;
		last_new.old = level[y][x];
		last_new.user=user;
		last=last_new;
		level[y][x] = sel;
	}
	
	/**
	 * Undo last Step
	 */
	public void back() {
		if(last==null) return;
		boolean user = last.user;
		level[last.y][last.x]=last.old;
		last=last.pre;
		if(!user) back();
	}
	
	/**
	 * get Char for Position
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the char
	 */
	public char get(int x, int y) {
		x /= size_x;
		y /= size_y;
		return level[y][x];
	}
	
	/**
	 * showRectangle
	 * @param g the Graphics to Show
	 * @param c the color
	 * @param x the x coordinate in array
	 * @param y the y coordinate in array
	 */
	private void showRect(Graphics g, Color c, int x, int y) {
		Color old = g.getColor();
		g.setColor(c);
		g.fillRect(x*size_x, y*size_y, size_x, size_y);
		g.setColor(old);
	}
	
	/**
	 * Display the level
	 * @param g the Graphics to draw
	 */
	public void display(Graphics g) {
		g.setColor(Color.black);
		for(int y=0;y<37;y++) {
			for(int x=0;x<50;x++) {
				// Show Color
				switch(level[y][x]) {
				case 'm':
				case '#':
					showRect(g, Color.DARK_GRAY, x, y);
					break;
				case 'G':
				case 'S':
					showRect(g, Color.RED, x, y);
					break;
				case 'D':
				case 's':
				case 'V':
					showRect(g, new Color(100,0,0), x, y);
					break;
				case 'K':
					showRect(g, Color.YELLOW, x, y);
					break;
				}
				// Show Char
				g.drawRect(x*size_x, y*size_y, size_x, size_y);
				g.drawChars(level[y], x, 1, x*size_x+size_x/2, (y+1)*size_y);
			}
		}
	}
	
	/**
	 * Load Level
	 * @param file the File to Load
	 */
	public void load(File file) {
		clear();
		if(!file.exists()) return;
		Scanner s = null;
		try {
			s=new Scanner(file);
			int y=0;
			while(s.hasNextLine() && y<37) {
				String line = s.nextLine();
				int x=0;
				for(char c : line.toCharArray()) {
					level[y][x] = c;
					x++;
					if(x>=50) break;
				}
				
				y++;
				if(y>=37) break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
	}
	
	/**
	 * Save Level
	 * @param os the OutputStream to Save
	 */
	public void save(OutputStream os) {
		PrintWriter out=null;
		out=new PrintWriter(os);
		for(int y=0;y<37;y++) {
			out.println(String.valueOf(level[y]));
		}
		out.close();
	}
	
	/**
	 * Save Level
	 * @param file the File to Save
	 */
	public void save(File file) {
		try {
			save(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save Last Action for Undo
	 * @author erwin
	 *
	 */
	private class LastAction {
		public LastAction pre=null;
		public int x,y;
		public char old;
		public boolean user;
	}
}
