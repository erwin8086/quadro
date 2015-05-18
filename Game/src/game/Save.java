package game;
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

/**
 * The SaveGame
 * used for Store all
 * Hiscore, Player state, Resulution and others
 * @author erwin
 *
 */
public class Save {
	
	// Save Values for Entries in config file
	public static final int KEY_UP=0;
	public static final int KEY_LEFT=1;
	public static final int KEY_RIGHT=2;
	public static final int LEVEL=3;
	public static final int LIVES=4;
	public static final int SCORE=5;
	public static final int HIGHSCORE=6;
	public static final int LEVELSET=7;
	public static final int RES=8;
	private Game game;
	
	public void attachGame(Game game) {
		this.game=game;
	}
	
	/**
	 * Gets a Keybind
	 * @param key the Keybind to get
	 * @return the Key
	 */
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
	
	/**
	 * Saves Highscore to SaveGame
	 * Inputs for Name if score are High
	 * @param gui the MainWindow
	 * @param score the score to Save
	 */
	public void saveScore(GUI gui, int score) {
		if(isHighScore(score)) {
			String name;
			name=game.getMenu().EnterScore(true, score);
			if(name==null) return;
			if(name=="" || name.length()<1) name="a Player";
			for(int i=0;i<5;i++) {
				if(score>getHighScore(i).getScore()) {
					newHighScore(i, new Score(name, score));
					return;
				}
			}
		} else {
			game.getMenu().EnterScore(false, score);
		}
	}
	
	/**
	 * Check if score are High
	 * @param score the Score to check
	 * @return true if score are High
	 */
	public boolean isHighScore(int score) {
		if(score>getHighScore(4).getScore()) return true;
		return false;
	}
	
	/**
	 * Saves new Score
	 * @param num the Slot to Save
	 * @param score the Score to Save
	 */
	public void newHighScore(int num, Score score) {
		String scores = getConf(HIGHSCORE);
		if(scores==null) return;
		String[] split = scores.split(";");
		for(int i=4;i>num;i--) {
			split[i] = split[i-1];
		}
		split[num]=score.getName() + "-" + String.valueOf(score.getScore());
		scores="";
		for(String s : split) {
			scores+=s+";";
		}
		setConf(HIGHSCORE, scores);
	}
	
	/**
	 * get Score from HighScore
	 * @param num the Slot to get
	 * @return the Score
	 */
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
	
	/**
	 * Set Highscore in Slot
	 * @param num the Slot
	 * @param score the Score
	 */
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
	
	/**
	 * clear SaveGame
	 */
	public void clear() {
		if(getConfFile().exists()) {
			getConfFile().delete();
		}
	}
	
	/**
	 * get ConfigFile must not be exists
	 * @return
	 */
	public File getConfFile() {
		return new File(System.getProperty("user.home") + System.getProperty("file.separator") + "." + Main.title + ".conf");
	}
	
	/**
	 * get Config value
	 * @param conf the Slot to get
	 * @return the Value
	 */
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
		String val = null;
		val = getConf(conf,in);
		if(val==null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in=getClass().getResourceAsStream("/res/conf.txt");
			val = getConf(conf,in);
		}
		try {
			in.close();
		} catch( Exception e) {
			e.printStackTrace();
		}
		
		return val;
		
	}
	
	/**
	 * get Config Value from InputStream in
	 * @param conf the Slot to get
	 * @param in the InputStream
	 * @return the Value
	 */
	public String getConf(int conf, InputStream in) {
		
		if(in!=null) {
			Scanner s = new Scanner(in);
			while(s.hasNextLine()) {
				String line = s.nextLine();
				String[] split = line.split(":");
				if(split[0].equals(((Integer)conf).toString())) {
					s.close();
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
	
	/**
	 * Sets Config Value
	 * @param conf the Slot to Save
	 * @param val the Value to Save
	 */
	public void setConf(int conf, String val) {
		boolean saved=false;
		File f = getConfFile();
		if(!f.exists()) {
			try {
				Scanner s = new Scanner(getClass().getResourceAsStream("/res/conf.txt"));
				PrintWriter pr = new PrintWriter(f);
				while(s.hasNextLine()) {
					pr.println(s.nextLine());
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
	
	/**
	 * A Score with Name
	 * @author erwin
	 *
	 */
	static class Score {
		/**
		 * Create Score check name
		 * @param name the Name
		 * @param score the Score
		 */
		public Score(String name, int score) {
			setName(name);
			this.score = score;
		}
		private String name;
		private int score;
		
		/**
		 * gets the Name
		 * @return the Name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the Name
		 * Check for Correct Name
		 * @param name the Name
		 */
		public void setName(String name) {
			if(name.length()>10) {
				name = name.substring(0, 10);
			}
			if(!checkName(name)) {
				this.name=name;
			} else {
				this.name="a Player";
			}
		}
		
		/**
		 * gets The Score
		 * @return the Score
		 */
		public int getScore() {
			return score;
		}
		
		/**
		 * sets the Score
		 * @param score the Score to Set
		 */
		public void setScore(int score) {
			this.score=score;
		}
		
		/**
		 * Check name
		 * @param name the name to check
		 * @return true if Name are invalid
		 */
		public boolean checkName(String name) {
			return name.contains(new StringBuffer(";")) || name.contains(new StringBuffer("-")) || name.contains(new StringBuffer(":"));
		}
	}
}
