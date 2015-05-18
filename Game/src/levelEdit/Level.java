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

public class Level {
	private char[][] level = new char[37][50];
	private int size_x, size_y;
	private LastAction last=null;
	
	public Level(JFrame frame) {
		clear();
		size_x=frame.getWidth()/50;
		size_y=frame.getHeight()/37;
	}
	
	public void clear() {
		last=null;
		for(int y=0;y<37;y++) {
			for(int x=0;x<50;x++) {
				level[y][x] = ' ';
			}
		}
	}
	
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
	
	public void back() {
		if(last==null) return;
		boolean user = last.user;
		level[last.y][last.x]=last.old;
		last=last.pre;
		if(!user) back();
	}
	
	public char get(int x, int y) {
		x /= size_x;
		y /= size_y;
		return level[y][x];
	}
	
	private void showRect(Graphics g, Color c, int x, int y) {
		Color old = g.getColor();
		g.setColor(c);
		g.fillRect(x*size_x, y*size_y, size_x, size_y);
		g.setColor(old);
	}
	
	public void display(Graphics g) {
		g.setColor(Color.black);
		for(int y=0;y<37;y++) {
			for(int x=0;x<50;x++) {
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
				g.drawRect(x*size_x, y*size_y, size_x, size_y);
				g.drawChars(level[y], x, 1, x*size_x+size_x/2, (y+1)*size_y);
			}
		}
	}
	
	public void load(File file) {
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
	
	public void save(OutputStream os) {
		PrintWriter out=null;
		out=new PrintWriter(os);
		for(int y=0;y<37;y++) {
			out.println(String.valueOf(level[y]));
		}
		out.close();
	}
	
	public void save(File file) {
		try {
			save(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	class LastAction {
		public LastAction pre=null;
		public int x,y;
		public char old;
		public boolean user;
	}
}
