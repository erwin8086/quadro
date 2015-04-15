import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class Save {
	public static final int KEY_UP=0;
	public static final int KEY_LEFT=1;
	public static final int KEY_RIGHT=2;
	public static final int LEVEL=3;
	public static final int LIVES=4;
	public static final int SCORE=5;
	public static final int HIGHSCORE=6;
	
	
	public int getKeys(int key) {
		String val = getConf(key);
		if(val!=null) {
			try {
				return Integer.valueOf(val);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(key==KEY_UP) {
			return KeyEvent.VK_UP;
		} else if(key==KEY_LEFT) {
			return KeyEvent.VK_LEFT;
		} else if(key==KEY_RIGHT) {
			return KeyEvent.VK_RIGHT;
		}
		return 0;
	}
	
	public void saveScore(GUI gui, int score) {
		if(isHighScore(score)) {
			String name;
			name=JOptionPane.showInputDialog("Bitte Namen eingeben [A-Z, a-z, 0-9]:");
			if(name==null) return;
			if(name=="") name="a Player";
			for(int i=0;i<5;i++) {
				if(score>getHighScore(i).getScore()) {
					newHighScore(i, new Score(name, score));
				}
			}
		}
	}
	
	public boolean isHighScore(int score) {
		if(score>getHighScore(4).getScore()) return true;
		return false;
	}
	
	public void newHighScore(int num, Score score) {
		String scores = getConf(HIGHSCORE);
		if(scores==null) return;
		String[] split = scores.split(";");
		for(int i=num+1;i<5;i++) {
			split[i] = split[i-1];
		}
		split[num]=score.getName() + "-" + String.valueOf(score.getScore());
		for(String s : split) {
			scores+=s+";";
		}
		setConf(HIGHSCORE, scores);
	}
	
	public Score getHighScore(int num) {
		String scores = getConf(HIGHSCORE);
		if(scores==null) return new Score("null", 0);
		String[] split = scores.split(";");
		if(split.length<num) return new Score("null", 0);
		if(split[num]==null) return new Score("null", 0);
		try {
			String[] split2 = split[num].split("-");
			return new Score(split2[0], Integer.valueOf(split2[1]));
		} catch(NumberFormatException e) {
			return new Score("null", 0);
		}
	}
	
	
	public void setHighScore(int num, Score score) {
		String scores = getConf(HIGHSCORE);
		if(scores==null) return;
		String[] split = scores.split(";");
		split[num] = score.getName() + "-" + String.valueOf(score.getScore());
		scores = "";
		for(String s : split) {
			scores+=s+";";
		}
		setConf(HIGHSCORE, scores);
	}
	
	public void clear() {
		if(getConfFile().exists()) {
			getConfFile().delete();
		}
	}
	
	public File getConfFile() {
		return new File(System.getProperty("user.home") + System.getProperty("file.separator") + "." + Main.title + ".conf");
	}
	
	@SuppressWarnings("resource")
	public String getConf(int conf) {
		File f = getConfFile();
		InputStream in=null;
		if(f.exists()) {
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			in = getClass().getResourceAsStream("/res/conf.txt");
		}
		if(in!=null) {
			Scanner s = new Scanner(in);
			while(s.hasNextLine()) {
				String line = s.nextLine();
				String[] split = line.split(":");
				if(split[0].equals(((Integer)conf).toString())) {
					return split[1];
				}
			}
			s.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void setConf(int conf, String val) {
		boolean saved=false;
		File f = getConfFile();
		if(!f.exists()) {
			try {
				Scanner s = new Scanner(getClass().getResourceAsStream("/res/conf.txt"));
				PrintWriter pr = new PrintWriter(f);
				while(s.hasNextLine()) {
					pr.print(s.nextLine());
				}
				s.close();
				pr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			PrintWriter p = new PrintWriter(bout);
			Scanner in = new Scanner(f);
			while(in.hasNextLine()) {
				p.println(in.nextLine());
			}
			in.close();
			p.close();
			in = new Scanner(new ByteArrayInputStream(bout.toByteArray()));
			p = new PrintWriter(f);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] split = line.split(":");
				if(split[0].equals(String.valueOf(conf))) {
					p.println(conf + ":" + val);
					saved=true;
				} else {
					p.println(line);
				}
			}
			if(!saved) {
				p.println(conf + ":" + val);
			}
			in.close();
			p.close();
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	static class Score {
		public Score(String name, int score) {
			setName(name);
			this.score = score;
		}
		private String name;
		private int score;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			if(!checkName(name)) {
				this.name=name;
			} else {
				this.name="null";
			}
		}
		
		public int getScore() {
			return score;
		}
		
		public int setScore() {
			return score;
		}
		
		public boolean checkName(String name) {
			return name.matches("-") || name.matches(";") || name.matches(":");
		}
	}
}
