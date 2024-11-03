//Enemy.java
//Spencer Trepanier
//Enemy class creates enemy objects.
//Enemy moves right then down upon hitting a wall then left until next wall then repeats until it reaches the bottom.
//Enemies are vulnerable to Player bullets.
//Enemies shoot player at a random rate (chance of firing changes upon hitting a wall or level increasing)
//Enemies destroy shield pixels upon collision.
//Enemies make fancy noises when moving, and upon destruction makes a fancy death animation and noise.
import java.awt.*;
import java.awt.geom.*;
import java.applet.*;
import java.io.*;

public class Enemy{
	public boolean edeath;
	public int deathTimer;
	public int x,y,w,l,v,t;
	private Bullet eBullet;
	public Rectangle eRect;
	public static boolean hitsWall;
	File wavFile = new File("sfx_movement_footstepsloop4_fast.wav");
	AudioClip sound;
	Font fontLocal = null;

	public Enemy(int xx, int yy, int ww, int ll, int vv, int tt) {
		edeath = false;
		deathTimer = 0;
		x = xx;
		y = yy;
		w = ww;
		l = ll;
		v = vv;
		t = tt;
		hitsWall = false;
		eBullet = new Bullet(x,GamePanel.HEIGHT+l,4,12,10,GamePanel.DOWN);
		eRect = new Rectangle(x,y,w,l);
		try{
			sound = Applet.newAudioClip(wavFile.toURL());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		String fName = "invaders.ttf";
		InputStream is = GamePanel.class.getResourceAsStream(fName);
		try{
			fontLocal = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(36f);
		}
		catch(IOException ex){
			System.out.println(ex);	
		}
		catch(FontFormatException ex){
			System.out.println(ex);	
		}
	}

	public static void move(){	//Note: much credit to George for this method, the implementation was his success.
		if(!hitsWall){	//if not hitting wall, move the enemy in current direction
			for(Enemy e:GamePanel.enemies){
				e.moveX();
			}
		}
		if(hitsWall){	//if hitting wall each enemy moves down, the shooting chance is increased and the tick delay is decreased. If the bottom of any of the enemies is at the y of the player, the player dies.
			for(Enemy e:GamePanel.enemies){
				e.moveAtWall();
				if(GamePanel.shootChance>=300){
					GamePanel.shootChance-=25;
				}
				if(GamePanel.tickSpeed<=5){
					GamePanel.tickSpeed--;
				}
				if(e.y+e.l==500){
					GamePanel.lives=0;
				}
			}
			hitsWall = false;	//all the necessary movements have occured upon hitting wall, therefore it is not hitting wall anymore
		}
		else{
			for(Enemy e:GamePanel.enemies){	//checks if each enemy is hitting the wall, and sets the boolean accordingly
				if(e.x<=0 || e.x>=GamePanel.WIDTH-e.w){
					hitsWall = true;
				}
			}
		}
	}
	
	public void moveX(){	//moves the enemies in the x direction
		sound.play();
		eRect = new Rectangle(x,y,w,l);
		x+=v;
	}
	
	public void moveAtWall(){	//plays a fancy sound, switches the direction of the enemies, then moves them down
		sound.play();
		v*=-1;
		y+=30;
	}	
	
	public void shoot(){	//shoots the player at a random rate (changes upon hitting wall and level increase)
		if(Util.randint(0,GamePanel.shootChance)==GamePanel.shootChance && eBullet.y > GamePanel.HEIGHT){
			eBullet = new Bullet(x+w/2-2,y+12,4,12,10,GamePanel.DOWN);
			GamePanel.bullets.add(eBullet);
		}
	}
	
	public void draw(Graphics g){	//draws enemies
		g.setColor(Color.WHITE);
		g.setFont(fontLocal);
		if(edeath){	//tackles fancy death animation
			g.drawString("z",x,y+l);
			deathTimer++;
		}
		else if(t==GamePanel.SQUID){	//if enemy is a squid, draw a squid
			if(GamePanel.frameCounter%GamePanel.tickSpeed*2>=GamePanel.tickSpeed){	//makes the arms go up and down (super fancy)
				g.drawString("d",x,y+l);
			}
			else{
				g.drawString("e",x,y+l);
			}
		}
		else if(t==GamePanel.CRAB){
			if(GamePanel.frameCounter%GamePanel.tickSpeed*2>=GamePanel.tickSpeed){	//same as above for crab
				g.drawString("b",x,y+l);
			}
			else{
				g.drawString("c",x,y+l);
			}
		}
		else if(t==GamePanel.OCTOPUS){
			if(GamePanel.frameCounter%GamePanel.tickSpeed*2>=GamePanel.tickSpeed){	//same as above for octopus
				g.drawString("f",x,y+l);
			}
			else{
				g.drawString("g",x,y+l);
			}
		}
	}
}