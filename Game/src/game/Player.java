package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The Player
 * Symbol: 'P'
 * @author erwin
 *
 */
public class Player implements GameObject, KeyListener {
	// Coordinates and Size
	private float x, y;
	private int size_x, size_y;
	// Pressed Keys
	private boolean key_left, key_up, key_right;
	private int KEY_LEFT, KEY_UP, KEY_RIGHT;
	private GUI gui;
	// Jumping Count
	private float up;
	// Speed of Player
	private int speed;
	// Num of Evils to KILL
	private int num_evils;
	private Level level;
	// is force JUMPING
	private boolean jumping;
	// Lives of Player
	private Integer lives;
	// Invincible of Player
	private float invincible;
	// Destroy Evils if Invincible
	private boolean destroyEvils;
	private Integer score;
	private float no_jump;
	
	private Game game;
	
	/**
	 * Create the Player
	 * @param game the Game Instance
	 */
	public Player(Game game) {
		// Sets Variables
		this.game = game;
		this.gui = game.getGUI();
		this.level = game.getLevel();
		// Add KeyListener
		gui.addKeyListener(this);
		// Calc size and speed
		size_x = gui.getWidth()/50;
		size_y = gui.getHeight()/37;
		speed=size_x*8;
		
		// Sets Keys
		KEY_LEFT = KeyEvent.VK_LEFT;
		KEY_RIGHT = KeyEvent.VK_RIGHT;
		KEY_UP = KeyEvent.VK_UP;
		
		if(game.getLevel().isScore()) {
			// Sets Lives and Score
			lives=Integer.valueOf(game.getSave().getConf(Save.LIVES));
			score=Integer.valueOf(game.getSave().getConf(Save.SCORE));
		} else {
			lives=3;
			score=0;
		}
		// Reset Player
		this.reset();
	}
	/**
	 * Addes one Live
	 */
	public void addLive() {
		lives++;
	}
	
	/**
	 * Destroy Player
	 */
	public void destroy() {
		gui.removeKeyListener(this);
	}
	
	/**
	 * Set Keybinds
	 * @param left
	 * @param right
	 * @param up
	 */
	public void setKeys(int left, int right, int up) {
		KEY_LEFT = left;
		KEY_UP = up;
		KEY_RIGHT = right;
	}
	
	/**
	 * Adds given score to score
	 * @param score // Score to Add
	 */
	public void addScore(int score) {
		this.score += score;
	}
	
	/**
	 * Gets Position as Rectangle
	 * @return
	 */
	public Rectangle getPOS() {
		return new Rectangle((int)x, (int)y, size_x, size_y);
	}
	
	/**
	 * Jump Given Height
	 * @param up // height to jump
	 * @param time // time sience last frame
	 */
	public void jump(int up, float time) {
		if(no_jump>0) return;
		this.up=(up/16)*size_x;
		this.y -= speed*time*2;
		jumping=true;
	}
	
	/**
	 * Calculate Player movement
	 */
	@Override
	public boolean calc(float time) {
		if(no_jump>0) no_jump-=time;
		// Decrase Invincible
		invincible-=time;
		boolean is_mauer=false;
		for (GameObject g : game.getGameObjects()) {
			if(g.getCons()!=GameObject.CONS_MAUER) continue;
			if(g.isColidate(new Rectangle((int)x, (int)y+size_y+1, size_x, 1))) {
				is_mauer=true;
				break;
			}
			
		}
		// Check if Player shold fall down
		if(!is_mauer) {
			if(up<=0) {
				this.y += time*speed*2;
				GameObject m = null;
				for (GameObject g : game.getGameObjects()) {
					if(g.getCons()!=GameObject.CONS_MAUER) continue;
					if(g.isColidate(new Rectangle((int)x, (int)y+size_y+1, size_x, 1))) {
						m=g;
						if(g.getType()==GameObject.EVIL) {
							gameOver();
						}
						break;
					}
					
				}
				if(m!=null) {
					while(m.isColidate(new Rectangle((int)x, (int)y+size_y, size_x, 1))) {
						this.y--;
					}
				}
			}
		// Check if Player shold jump
		} else if(key_up) {
			this.jump(96, time);
			jumping=false;
		}
		// Jump Player
		if(up>0 && (key_up || jumping) ) {
			is_mauer=false;
			for(GameObject g : game.getGameObjects()) {
				if(g==this) continue;
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(getPOS())) {
					is_mauer=true;
					while(g.isColidate(getPOS())) {
						y++;
					}
					break;
				}
			}
			if(is_mauer) {
				up=0;
				jumping=false;
				no_jump=.5f;
			} else {
				if(up>36)
					this.y -= speed*time*2;
				else
					this.y -= speed*time;
				up-= speed*time;
			}
		// Check if Player release key_up
		} else if(up>0 && !key_up && !jumping) {
			up=0;
			jumping=false;
		}
		// Go Right
		if(key_right) {
			this.x += time*speed;
			is_mauer=false;
			for(GameObject g : game.getGameObjects()) {
				if(g==this) continue;
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(getPOS())) {
					is_mauer=true;
					break;
				}
			}
			if(is_mauer)
				this.x -= time*speed;
			if(this.x>gui.getWidth()-size_x) {
				this.x=gui.getWidth()-size_x;
			}
		}
		// Go Left
		if(key_left) {
			this.x -= time*speed;
			is_mauer=false;
			for(GameObject g : game.getGameObjects()) {
				if(g==this) continue;
				if(g.getCons()!=GameObject.CONS_MAUER) continue;
				if(g.isColidate(getPOS())) {
					is_mauer=true;
					break;
				}
			}
			if(is_mauer)
				this.x += time*speed;
			if(this.x<0) {
				this.x=0;
			}
		}
		
		// Set GameComplete if no evils
		if(num_evils<=0) {
			gameComplete();
		}
		
		for(GameObject g : game.getGameObjects()) {
			if(g==this) continue;
			if(g.isColidate(getPOS())) {
				if(g.getType()==GameObject.EVIL) {
					gameOver();
				}
				g.changeDest(getPOS());
			}
		}
		return false;
	}

	/**
	 * Paint Player to Screen
	 */
	@Override
	public boolean paint(Graphics g) {
		if(invincible>0) {
			g.setColor(Color.CYAN);
		} else {
			g.setColor(Color.blue);
		}
		g.fillRect((int)x, (int)y, size_x, size_y);
		return false;
	}

	/**
	 * Resets Player on Level starts
	 */
	@Override
	public boolean reset() {
		x=gui.getWidth()/2;
		y=gui.getHeight()/2;
		new Level.LevelLoader() {
			
			@Override
			void onFound(int x1, int y1) {
				x=x1;
				y=y1;
			}
		}.load('P', level.getLevel(), game);
		key_left=false;
		key_right=false;
		key_up=false;
		up=0;
		num_evils=0;
		jumping=false;
		invincible=2;
		destroyEvils=false;
		return false;
	}

	/**
	 * Check if Player Colidate
	 */
	@Override
	public boolean isColidate(Rectangle r) {
		if(getPOS().intersects(r)) return true;
		return false;
	}

	/**
	 * Destroy Player if Colidate
	 */
	@Override
	public boolean destroyColidate(Rectangle r) {
		if(isColidate(r)) {
			if(invincible>0) {
				if(destroyEvils) {
					return true;
				} else {
					return false;
				}
			} else {
				gameOver();
			}
		}
			
		return false;
	}
	
	/**
	 * Remove live from player if lives==0 destroy Player and exits game
	 */
	public void gameOver() {
		if(invincible>0) return;
		if(lives>0) {
			lives--;
			invincible=1;
			return;
		}
		game.getLevel().gameOver();
	}
	
	/**
	 * Set Player Level Complete
	 */
	public void gameComplete() {
		level.nextLevel(score);
	}

	/**
	 * Set Keys
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		if(c==KEY_UP)
			key_up=true;
		else if(c==KEY_LEFT)
			key_left=true;
		else if(c==KEY_RIGHT)
			key_right=true;
	}

	/**
	 * Unset Keys
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int c = e.getKeyCode();
		if(c==KEY_UP)
			key_up=false;
		else if(c==KEY_LEFT)
			key_left=false;
		else if(c==KEY_RIGHT)
			key_right=false;
	}

	/**
	 * Do Noting
	 */
	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Add a Evil
	 */
	public void addEvil() {
		num_evils++;
	}
	
	/**
	 * Del a Evil
	 */
	public void delEvil() {
		num_evils--;
	}
	
	/**
	 * Get Score
	 * @return in score
	 */
	public Integer getScore() {
		return score;
	}
	
	/**
	 * Get Num Lives
	 * @return int lives
	 */
	public Integer getLives() {
		return lives;
	}
	
	/**
	 * Set Player Invincible
	 * @param time int time in secounds to invincible
	 * @param destroy bool shold destroy Evils on contact
	 */
	public void addInvincible(int time, boolean destroy) {
		invincible=time;
		destroyEvils=destroy;
	}
	@Override
	public int getType() {
		return GameObject.PLAYER;
	}
	
	/**
	 * Do Noting
	 */
	@Override
	public void changeDest(Rectangle r) {
		
	}
	
	/**
	 * get Consistency of Player
	 * @return CONS_EVIL
	 */
	@Override
	public int getCons() {
		return GameObject.CONS_EVIL;
	}
	
	/**
	 * get x coordinate from Player
	 * @return the x coordinate
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * set x coordinate from Player
	 * @param x the x coordinate
	 */
	public void setX(float x) {
		this.x=x;
	}
	
	/**
	 * get y coordinate from Player
	 * @return the y coordinate
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * set y coordinate from Player
	 * @param y the y coordinate
	 */
	public void setY(float y) {
		this.y=y;
	}
	
	public void setLives(int lives) {
		this.lives=lives;
	}


}
