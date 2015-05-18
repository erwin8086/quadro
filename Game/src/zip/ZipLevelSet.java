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

public class ZipLevelSet implements LevelSet {
	
	private ZipFile zip;
	private Game game;
	private Scanner conf;
	private byte[] level;
	private int save_score;
	private int save_level;
	private int save_lives;
	private Color color;
	private int width,height;
	
	public void readSave() {
		if(zip.getFile("save.txt")==null) {
			save_score=0;
			save_level=0;
			save_lives=3;
			return;
		}
		Scanner in = new Scanner(zip.getFileAsStream("save.txt"));
		save_score=Integer.valueOf(in.nextLine());
		save_level=Integer.valueOf(in.nextLine());
		save_lives=Integer.valueOf(in.nextLine());
		in.close();
	}
	
	public void writeSave() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter pr = new PrintWriter(out);
		pr.println(save_score);
		pr.println(save_level);
		pr.println(save_lives);
		pr.close();
		zip.replace(out.toByteArray(), "save.txt");
	}
	
	
	public ZipLevelSet(Game game, ZipFile zip) {
		this.zip=zip;
		this.game=game;
		conf=new Scanner(zip.getFileAsStream("main.cnf"));
		width=game.getGUI().getWidth();
		height=game.getGUI().getHeight();
		color=Color.black;
		readSave();
		int l=save_level;
		getLevel();
		level=null;
		while(l>0) {
			if(getLevel()==null) break;
			level=null;
			l--;
		}
		if(level==null) {
			save_level=0;
			conf=new Scanner(zip.getFileAsStream("main.cnf"));
		}
	}

	@Override
	public InputStream getLevel() {
		if(level==null) {
			while(level==null && conf.hasNextLine()) {
				String line = conf.nextLine();
				String[] split = line.split(":");
				if(split[0].equals("display")) {
					game.getMenu().showScreen(zip.getFileAsStream(split[1]));
				} else if(split[0].equals("color")) {
					color = new Color(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
				} else if(split[0].equals("level")) {
					level=zip.getFile(split[1]);
					break;
				}
			}
			if(level==null) return null;
		}
		return new ByteArrayInputStream(level);
	}

	@Override
	public boolean nextLevel() {
		level=null;
		save_level++;
		writeSave();
		if(getLevel()==null) return false;
		return true;
	}

	@Override
	public LevelSet nextLevelSet() {
		return null;
	}

	@Override
	public void onLevelStarts() {
		
	}

	@Override
	public int getLevelNum() {
		return save_level;
	}

	@Override
	public void drawBackground(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0,width, height);
	}

	@Override
	public Color getFontColor() {
		return Color.white;
	}

	@Override
	public boolean isScore() {
		return false;
	}

}