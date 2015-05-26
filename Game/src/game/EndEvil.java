package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * EndEvil used for Level 10 of Episode III
 * @author erwin
 *
 */
public class EndEvil implements GameObject {
	private int width, height;
	private ArrayList<Canon> canon;
	private float x,y;
	private int size_x, size_y;
	private int dest;
	private int speed=70;
	private Image image;
	private Player play;
	private int lives;
	private int size_minus;
	private boolean destroyed=false;
	private float next_canon=3;
	private float canon_time=3;
	private Random rand;
	
	/**
	 * Create EndEvil and init Canon
	 * @param game
	 */
	public EndEvil(Game game) {
		
		// init variables
		width=game.getGUI().getWidth();
		height=game.getGUI().getHeight();
		size_x=width/15;
		size_y=size_x;
		x=0;
		y=size_y*2;
		dest=1;
		lives=10;
		size_minus=size_x/15;
		play=game.getPlayer();
		
		// Load Image
		BufferedImage tmp=null;
		try {
			tmp = ImageIO.read(getClass().getResourceAsStream("/res/smiley.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(tmp!=null)
			image=tmp.getScaledInstance(size_x, size_y, 0);
		
		// Init Canon
		canon=new ArrayList<Canon>();
		rand=new Random();
		reset();
	}

	/**
	 * Calculate movement and Canon
	 */
	@Override
	public boolean calc(float time) {
		// If Evil destroyed do not Calc
		if(destroyed) return false;
		
		// Timer for next Canon
		next_canon-=time;
		
		// Create Canon
		if(next_canon<0) {
			next_canon=canon_time;
			if(canon_time>0.5f)
				canon_time-=0.3f;
			Canon c = new Canon(x+size_x/2, y+size_y/2, size_x/5, size_y/5);
			// Set Destination
			int sel = rand.nextInt(8);
			if(sel==0) {
				c.dest_x=0;
				c.dest_y=1;
			} else if(sel==1) {
				c.dest_x=0;
				c.dest_y=-1;
			} else if(sel==2) {
				c.dest_x=1;
				c.dest_y=0;
			} else if(sel==3) {
				c.dest_x=-1;
				c.dest_y=0;
			} else if(sel==4){
				c.dest_x=1;
				c.dest_y=1;
			} else if(sel==5) {
				c.dest_x=1;
				c.dest_y=-1;
			} else if(sel==6) {
				c.dest_x=-1;
				c.dest_y=-1;
			} else if(sel==7) {
				c.dest_x=-1;
				c.dest_y=1;
			} else {
				c.dest_x=-1;
				c.dest_y=1;
			}
			canon.add(c);
		}
		
		// Calc Canon
		for(int i=0;i<canon.size();i++) {
			Canon c = canon.get(i);
			c.y+=time*speed*2*c.dest_y;
			c.x+=time*speed*2*c.dest_x;
			Rectangle pos=new Rectangle((int)c.x, (int)c.y, c.height, c.width);
			// Set Player GameOver if Colidate
			if(play.isColidate(pos)) {
				play.gameOver();
				canon.remove(c);
			}
			if(c.y>height)
				canon.remove(c);
		}
		// Move EndEvil
		x+=speed*time*dest;
		if((x+size_x) > width )
			dest*=-1;
		if(x<0)
			dest*=-1;
		x+=speed*time*dest;
		// Check for Player Colidate
		Rectangle pos = new Rectangle((int)x, (int)y, size_x, size_y);
		if(play.isColidate(pos)) {
			if(y-8>play.getY()) {
				size_x-=size_minus;
				size_y-=size_minus;
				lives--;
				play.addScore(50);
				image=image.getScaledInstance(size_x, size_y, 0);
				if(lives<=0) {
					play.delEvil();
					destroyed=true;
					play.addScore(500);
				}
			} else {
				play.gameOver();
			}
			play.jump(100, time);
		}
		return false;
	}

	/**
	 * Paint Evil and Canon
	 */
	@Override
	public boolean paint(Graphics g) {
		if(destroyed) return false;
		g.drawImage(image, (int)x, (int)y, null);
		g.setColor(Color.black);
		for(int i=0;i<canon.size();i++) {
			Canon c = canon.get(i);
			g.fillRect((int)c.x, (int)c.y, c.width, c.height);
		}
		return false;
	}

	/**
	 * reset Evil do not use on new Level.
	 * Create new EndEvil
	 */
	@Override
	public boolean reset() {
		play.addEvil();
		return false;
	}

	/**
	 * check if Colidate
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		if(r.intersects(new Rectangle((int) x, (int)y, size_x, size_y)))
			return true;
		return false;
	}

	/**
	 * Do Noting
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		return false;
	}

	/**
	 * get Type
	 */
	@Override
	public int getType() {
		return EVIL_SELF_CALC;
	}

	/**
	 * Do Nothing
	 */
	@Override
	public void changeDest(Rectangle r) {
		
	}

	/**
	 * get Consitence
	 */
	@Override
	public int getCons() {
		return CONS_EVIL;
	}
	
	/**
	 * Canon used for Position of canons
	 * @author erwin
	 *
	 */
	class Canon {
		float x,y;
		int width, height;
		private int dest_x;
		private int dest_y;
		public Canon(float x, float y, int width, int height) {
			this.x=x;
			this.y=y;
			this.width=width;
			this.height=height;
		}
	}

}
