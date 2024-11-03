//Game.java
//Spencer Trepanier
//Game class creates a JFrame with a space invaders game.
//GamePanel class makes the game itself, involving a player, enemies, a ufo, and some bullets.
//Each type of enemy has a different score, ufo has a random score from 50-300 (inclusive) in increments of 50.
//The game is endless, after each enemy is destroyed, a new level is created with faster enemies that shoot more often, and get closer (until level 10).
//The shields slowly wear away as bullets hit them.

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Game extends BaseFrame{
	GamePanel game = new GamePanel();
	public Game(){
		super("Space Invaders");
	}
	
	public static void main(String[] arguments) {
		Game frame = new Game();
	}
}

class GamePanel extends JPanel implements KeyListener, ActionListener{
	public static final int INTRO = 0, GAME = 1, DEATH = 2;	//screens
	public static final int UP = 0, DOWN = 1;	//bullet directions
	private static final int LEFT = 37, RIGHT = 39, SHOOT = 32;	//key codes
	public static final int WIDTH = 800, HEIGHT = 600;	//width & height
	private static final int ENEMYV = 5, PLAYERV = 10;	//player and enemy speeds
	private static final int PLAYERW = 50, SQUIDW = 30, CRABW = 40, OCTOPUSW = 44, UFOW = 46;							//entity dimensions
	private static final int PLAYERH = 36, SQUIDH = 28, CRABH = 28, OCTOPUSH = 28, UFOH = 22, UFOY = 50, PLAYERX = 105;	//
	
	
	public static int screen = INTRO;
	
	private boolean []keys;
	
	javax.swing.Timer timer;
	public static int frameCounter;
	public static int deadCounter;	
	public static int shootChance;
	
	private static int ufoChance;
	
	Image back;
	Image over;
	
	public static Player player;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Shield> shields;
	public static ArrayList<Bullet> bullets;
	public static UFO ufo;
	private static int ufoCounter=0;
	
	private static int level;
	public static int tickSpeed;
	public static int score;
	public static int lives;
	public static int UFO = Util.randint(1,6)*50, SQUID = 40, CRAB = 20, OCTOPUS = 10;
	Font fontLocal1 = null, fontLocal2 = null;
	
	public GamePanel(){ //sets all the elements of the game before it starts
		back = new ImageIcon("intro.png").getImage();
		over = new ImageIcon("over.png").getImage();
		keys = new boolean[KeyEvent.KEY_LAST+1];
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		score=0;
		lives=3;
		level=1;
		
		frameCounter=0;
		tickSpeed=30;
		deadCounter=0;
		shootChance=10000;
		ufoChance=250;
		
		player = new Player(PLAYERX,PLAYERW,PLAYERH,PLAYERV,LEFT,RIGHT,SHOOT);
		
		enemies = new ArrayList<Enemy>();
		for(int i=0; i<11; i++){
			for(int j=0; j<5; j++){
				if(j==0){
					enemies.add(new Enemy(84+i*60,100+j*48,SQUIDW,SQUIDH,ENEMYV,SQUID));
				}
				if(j==1 || j==2){
					enemies.add(new Enemy(80+i*60,100+j*48,CRABW,CRABH,ENEMYV,CRAB));
				}
				if(j==3 || j==4){
					enemies.add(new Enemy(76+i*60,100+j*48,OCTOPUSW,OCTOPUSH,ENEMYV,OCTOPUS));
				}
			}
		}
		
		ufo = new UFO(HEIGHT+UFOH,UFOW,UFOH,ENEMYV);
		
		shields = new ArrayList<Shield>();
		for(int i=0; i<4; i++){
			shields.add(new Shield(50+i*200,50,50));
		}
		
		bullets = new ArrayList<Bullet>();
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		timer = new javax.swing.Timer(20, this);
		timer.start();
		
		String fName1 = "siFont.ttf";
		String fName2 = "invaders.ttf";
    	InputStream is1 = GamePanel.class.getResourceAsStream(fName1);
    	InputStream is2 = GamePanel.class.getResourceAsStream(fName2);
    	try{
    		fontLocal1 = Font.createFont(Font.TRUETYPE_FONT, is1).deriveFont(26f);
    		fontLocal2 = Font.createFont(Font.TRUETYPE_FONT, is2).deriveFont(26f);
    	}
    	catch(IOException ex){
    		System.out.println(ex);	
    	}
    	catch(FontFormatException ex){
    		System.out.println(ex);	
    	}
	}

	public void move(){
		if(screen == INTRO){
			
		}
		else if(screen == DEATH){
			
		}
		else if(screen == GAME){
			frameCounter++;
			player.move(keys);	//moves player
			if(frameCounter%tickSpeed==0){
				Enemy.move();	//moves enemy every certain amount of ticks (changes every time enemy hits wall & level changes)
			}
			if(Util.randint(0,ufoChance)==ufoChance && ufoCounter>=250){ //250 was determined via testing to be more than enough time for the ufo to cross the screen.
				ufo = new UFO(UFOY,UFOW,UFOH,ENEMYV);	//brings ufo to the screen
				ufo.setSide();	//sets the ufo's x randomly
				ufoCounter = 0;	//resets counter
			}
			ufo.move();	//moves ufo
			ufoCounter++;	//when ufo is in motion it counts frames
			for(Enemy e:enemies){
				e.shoot();	//each enemy shoots randomly
			}
			for(Bullet b:bullets){
				b.move();	//moves each bullet
				b.collisions();	//checks if each bullet is colliding
			}
			if(lives==0){	//if player dies, resets everything and sets screen to the death screen
				screen = DEATH;
				lives = 3;
				tickSpeed = 30;
				deadCounter = 0;
				enemies.clear();
				shields.clear();
				for(int i=0; i<11; i++){
					for(int j=0; j<5; j++){
						if(j==0){
							enemies.add(new Enemy(84+i*60,100+j*48,SQUIDW,SQUIDH,ENEMYV,SQUID));
						}
						if(j==1 || j==2){
							enemies.add(new Enemy(80+i*60,100+j*48,CRABW,CRABH,ENEMYV,CRAB));
						}
						if(j==3 || j==4){
							enemies.add(new Enemy(76+i*60,100+j*48,OCTOPUSW,OCTOPUSH,ENEMYV,OCTOPUS));
						}
					}
				}
				for(int i=0; i<4; i++){
					shields.add(new Shield(50+i*200,50,50));
				}
			}
			if(deadCounter==55){	//if all enemies are gone, reset everything except the shields, with an added level, enemies have an increased tickSpeed, increased shootChance, and spawn closer.
				level++;
				lives++;
				tickSpeed--;
				shootChance-=50;
				deadCounter=0;
				enemies.clear();
				if(level<=10){
					for(int i=0; i<11; i++){
						for(int j=0; j<5; j++){
							if(j==0){
								enemies.add(new Enemy(84+i*60,100+j*48+level*10,SQUIDW,SQUIDH,ENEMYV,SQUID));
							}
							if(j==1 || j==2){
								enemies.add(new Enemy(80+i*60,100+j*48+level*10,CRABW,CRABH,ENEMYV,CRAB));
							}
							if(j==3 || j==4){
								enemies.add(new Enemy(76+i*60,100+j*48+level*10,OCTOPUSW,OCTOPUSH,ENEMYV,OCTOPUS));
							}
						}
					}
				}
				else{
					for(int i=0; i<11; i++){
						for(int j=0; j<5; j++){
							if(j==0){
								enemies.add(new Enemy(84+i*60,200+j*48,SQUIDW,SQUIDH,ENEMYV,SQUID));
							}
							if(j==1 || j==2){
								enemies.add(new Enemy(80+i*60,200+j*48,CRABW,CRABH,ENEMYV,CRAB));
							}
							if(j==3 || j==4){
								enemies.add(new Enemy(76+i*60,200+j*48,OCTOPUSW,OCTOPUSH,ENEMYV,OCTOPUS));
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		move(); // never draw in move
		repaint(); // only draw
	}
	
	@Override
	public void keyReleased(KeyEvent ke){
		int key = ke.getKeyCode();
		keys[key] = false;
	}	
	
	@Override
	public void keyPressed(KeyEvent ke){
		int key = ke.getKeyCode();
		keys[key] = true;
	}
	
	@Override
	public void keyTyped(KeyEvent ke){
		if(keys[SHOOT] && screen == INTRO){	//starts game
			screen = GAME;
		}
	}

	@Override
	public void paint(Graphics g){
		if(screen == INTRO){	//draws intro screen
			g.drawImage(back,0,0,null);					
		}
		else if(screen == DEATH){	//draws death screen
			g.drawImage(over,-50,0,null);		
		}
		else if(screen == GAME){
			g.setColor(Color.BLACK);	//sets background colour
			g.fillRect(0,0,getWidth(),getWidth());	//fills background with background colour
			g.setColor(Color.WHITE);	//sets text colour
			g.setFont(fontLocal1);	//sets font
			g.drawString("SCORE: " + score,WIDTH/16,HEIGHT/12); //displays score
			g.drawString("LIVES: ",11*WIDTH/16,HEIGHT/12); 	//displays "lives: ""
			g.drawString("LEVEL: " + level,WIDTH/3,HEIGHT/12);	//displays level
			g.setFont(fontLocal2);	//sets font to the game icons
			g.setColor(Color.GREEN);
			if(lives>4 && lives<8){	//displays player icons for each life.
				for(int i=0; i<4; i++){
					g.drawString("w",13*WIDTH/16+i*30,HEIGHT/16); 
				}
				for(int j=0; j<lives%5+1; j++){
					g.drawString("w",13*WIDTH/16+j*30,HEIGHT/16+30);
				}
			}
			else if(lives>=8){
				for(int i=0; i<4; i++){
					for(int j=0; j<2; j++){
						g.drawString("w",13*WIDTH/16+i*30,HEIGHT/16+j*30);
					}
				}
			}
			else if(lives<=4){
				for(int i=0; i<lives; i++){
					g.drawString("w",13*WIDTH/16+i*30,HEIGHT/16);
				}
			}
			player.draw(g);	//draws player
			for(Shield s:shields){
				s.draw(g);	//draws shields
			}
			for(Enemy e:enemies){
				e.draw(g);	//draws enemies
			}
			for(Bullet b:bullets){
				b.draw(g);	//draws bullets
			}
			ufo.draw(g);	//draws ufo
		}
	}
}