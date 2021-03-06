package zip;

import java.awt.Color;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import game.Game;
import game.LevelSet;
import game.Player;
/**
 * A LevelSet from ZipFile
 * used for CustomLevelSets and Mods
 * @author erwin
 *
 */
public class ZipLevelSet implements LevelSet {
	
	private ZipFile zip;
	private Game game;
	private Scanner conf;
	private byte[] level;
	private int save_score;
	private int save_line;
	private int save_lives;
	private Color color;
	private Color font=Color.white;
	private int width,height;
	private int num_level=0;
	private boolean first_start=true;
	private ZipClassLoader modLoader;
	
	/**
	 * Reads the SaveGame
	 */
	public void readSave() {
		if(zip.getFile("save.txt")==null) {
			save_score=0;
			save_line=0;
			save_lives=3;
			return;
		}
		Scanner in = new Scanner(zip.getFileAsStream("save.txt"));
		save_score=Integer.valueOf(in.nextLine());
		save_line=Integer.valueOf(in.nextLine());
		save_lives=Integer.valueOf(in.nextLine());
		in.close();
	}
	
	/**
	 * Writes The SaveGame
	 */
	public void writeSave() {
		Player play = game.getPlayer();
		if(play==null) return;
		save_lives = play.getLives();
		save_score = play.getScore();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter pr = new PrintWriter(out);
		pr.println(save_score);
		pr.println(save_line);
		pr.println(save_lives);
		pr.close();
		zip.replace(out.toByteArray(), "save.txt");
	}
	
	/**
	 * Load first Level and Mods
	 * @param game the Game
	 * @param zip the ZipFile
	 */
	public ZipLevelSet(Game game, ZipFile zip) {
		this.zip=zip;
		this.game=game;
		modLoader = new ZipClassLoader(getClass().getClassLoader(), zip);
		conf=new Scanner(zip.getFileAsStream("main.cnf"));
		width=game.getGUI().getWidth();
		height=game.getGUI().getHeight();
		color=Color.black;
		readSave();
		int l=save_line;
		// Loads first Level if no SaveGame
		while(level==null && conf.hasNextLine() && l==0) {
			String line = conf.nextLine();
			save_line++;
			String[] split = line.split(":");
			if(split[0].equals("display")) {
				game.getMenu().showScreen(zip.getFileAsStream(split[1]));
			} else if(split[0].equals("color")) {
				color = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
			} else if(split[0].equals("level")) {
				level=zip.getFile(split[1]);
				num_level=Integer.valueOf(split[2]);
				break;
			} else if(split[0].equals("save")) {
				writeSave();
			} else if(split[0].equals("mod")) {
				try {
					Class<?> modc = modLoader.loadClass("mod." + split[1]);
					try {
						Mod mod = (Mod) modc.newInstance();
						mod.load(game,zip);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if(split[0].equals("font")) {
				font = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
			}
		}
		// Loads Saved State
		while(l>0) {
			if(!conf.hasNextLine()) {
				InputStream in=getClass().getResourceAsStream("/res/level.txt");
				Scanner s = new Scanner(in);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PrintWriter p = new PrintWriter(out);
				while(s.hasNextLine())
					p.println(s.nextLine());
				p.close();
				s.close();
				level=out.toByteArray();
				break;
			}
			String line = conf.nextLine();
			String[] split = line.split(":");
			if(split[0].equals("display")) {
				game.getMenu().showScreen(zip.getFileAsStream(split[1]));
			} else if(split[0].equals("color")) {
				color = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
			} else if(split[0].equals("mod")) {
				try {
					Class<?> modc = modLoader.loadClass("mod." + split[1]);
					try {
						Mod mod = (Mod) modc.newInstance();
						mod.load(game,zip);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if(split[0].equals("font")) {
				font = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
			}
			l--;
		}	
	}

	/**
	 * Gets the Level
	 */
	@Override
	public InputStream getLevel() {
		if(level==null) {
			while(level==null && conf.hasNextLine()) {
				String line = conf.nextLine();
				save_line++;
				String[] split = line.split(":");
				if(split[0].equals("display")) {
					game.getMenu().showScreen(zip.getFileAsStream(split[1]));
				} else if(split[0].equals("color")) {
					color = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
				} else if(split[0].equals("level")) {
					level=zip.getFile(split[1]);
					num_level=Integer.valueOf(split[2]);
					break;
				} else if(split[0].equals("save")) {
					writeSave();
				} else if(split[0].equals("mod")) {
					try {
						Class<?> modc = modLoader.loadClass("mod." + split[1]);
						try {
							Mod mod = (Mod) modc.newInstance();
							mod.load(game,zip);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if(split[0].equals("font")) {
					font = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
				}
			}
			if(level==null) return null;
		}
		return new ByteArrayInputStream(level);
	}

	/**
	 * loads nextLevel
	 */
	@Override
	public boolean nextLevel() {
		level=null;
		if(getLevel()==null) return false;
		return true;
	}

	/**
	 * LevelSet finish
	 */
	@Override
	public LevelSet nextLevelSet() {
		zip.delete("save.txt");
		return null;
	}

	/**
	 * on Level starts first
	 * set Player lives and Score
	 */
	@Override
	public void onLevelStarts() {
		if(first_start) {
			Player play = game.getPlayer();
			play.addScore(save_score);
			play.setLives(save_lives);
			first_start=false;
		}
	}

	// Return number of Level
	@Override
	public int getLevelNum() {
		return num_level;
	}

	/**
	 * Draw Background
	 */
	@Override
	public void drawBackground(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0,width, height);
	}

	/**
	 * gets font color
	 */
	@Override
	public Color getFontColor() {
		return font;
	}

	/**
	 * is not Default
	 */
	@Override
	public boolean isScore() {
		return false;
	}

}
