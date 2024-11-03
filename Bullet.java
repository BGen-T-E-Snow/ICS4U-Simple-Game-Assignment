//Bullet.java
//Spencer Trepanier
//Bullet class creates a bullet object.
//Bullets are spawned by players, enemies and ufos.
//They have the ability to destroy and remove lives depending on their direction.
//They make fancy noises when they hit things.
import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import java.applet.*;
import java.io.*;

public class Bullet{
	public int x,y,w,l,v,d;
	public Rectangle bRect;
	File eDeath = new File("sfx_weapon_singleshot17.wav");
	File pDeath = new File("sfx_deathscream_robot2.wav");
	AudioClip eDeathSound;
	AudioClip pDeathSound;
	
	public Bullet(int xx, int yy, int ww, int ll, int vv, int dd){
		x = xx;
		y = yy;
		w = ww;
		l = ll;
		v = vv;
		d = dd;
		bRect = new Rectangle(x,y,w,l);
		try{
			eDeathSound = Applet.newAudioClip(eDeath.toURL());
			pDeathSound = Applet.newAudioClip(pDeath.toURL());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void move(){	//moves the bullet in the specified direction
		bRect = new Rectangle(x,y,w,l);
		if(d==GamePanel.UP){
			y -= v;
		}
		if(d==GamePanel.DOWN){
			y += v;
		}
	}
	
	public void collisions(){	//checks if the bullet is colliding with something
		Iterator<Enemy> enIter = GamePanel.enemies.iterator();	//iterator due to problems with .remove() from an arraylist that is in a for loop. I believe I got the syntax from a thread on https://stackoverflow.com/questions/223918/iterating-through-a-collection-avoiding-concurrentmodificationexception-when-re, and modified it for my own purposes.
		while(enIter.hasNext()){
			Enemy e = enIter.next();
			if(d==GamePanel.UP && bRect.intersects(e.eRect)){
				eDeathSound.play();	//plays fancy death sound
				GamePanel.deadCounter++;
				y = -l;
				GamePanel.score+=e.t;
				e.edeath = true;	//this triggers the fancy death animation for the enemy
			}
			if(e.deathTimer>=10){	//this ends the fancy death animation after 10 frames
				enIter.remove();
			}
		}
		for(Shield s:GamePanel.shields){	//same as above but forgets about direction
			Iterator<Rectangle> sS = s.shieldSquares.iterator();
			while(sS.hasNext()){
				Rectangle sRect = sS.next();
				if(bRect.intersects(sRect)){
					if(d==GamePanel.UP){
						y = -l;
					}
					else{
						y = GamePanel.HEIGHT+l;
					}
					sS.remove();
				}
			}
		}
		if(d==GamePanel.DOWN && bRect.intersects(GamePanel.player.pRect)){	//
			pDeathSound.play();
			GamePanel.lives-=1;
			y = GamePanel.HEIGHT+l;
			GamePanel.player.pdeath = true;	//triggers fancy player hit animation
		}
		if(GamePanel.player.deathTimer>=10){	//ends fancy player hit animation after 10 frames
			GamePanel.player.pdeath = false;
			GamePanel.player.deathTimer = 0;
		}
		if(d==GamePanel.UP && bRect.intersects(GamePanel.ufo.ufoRect)){
			eDeathSound.play();	//plays fancy death sound
			y = -l;
			GamePanel.score+=Util.randint(1,6)*50;
			GamePanel.ufo.ufodeath = true;	//triggers fancy death animation
		}
		if(GamePanel.ufo.deathTimer>=10){	//ends fancy death animation after 10 frames
			GamePanel.ufo.ufodeath = false;
			GamePanel.ufo.deathTimer = 0;
			GamePanel.ufo.y = GamePanel.HEIGHT+GamePanel.ufo.l;
		}
	}
	
	public void draw(Graphics g){	//draws bullet
		g.setColor(Color.WHITE);
		g.fillRect(x,y,w,l);
	}    
}