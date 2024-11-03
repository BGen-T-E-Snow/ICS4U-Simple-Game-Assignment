//UFO.java
//Spencer Trepanier
//UFO class creates a ufo object.
//UFO spawns randomly and starts at a random side of the screen and moves in the opposite direction.
//UFO is vulnerable to Player bullets.
//UFO shoots player at a random rate (chance of firing changes upon enemies hitting a wall or level increasing)
//UFO destroys shield pixels upon collision.
//UFO has a random score from 50-300 inclusive in increments of 50.
//UFO makes fancy noises and animation upon destruction.
import java.awt.*;
import java.awt.geom.*;
import java.io.*;

public class UFO{
	public int x,y,w,l,v;
	private Bullet ufoBullet;
	public Rectangle ufoRect;
	public boolean ufodeath;
	public int deathTimer;
	Font fontLocal = null;

	public UFO(int yy, int ww, int ll, int vv){
		y = yy;
		w = ww;
		l = ll;
		v = vv;
		ufoBullet = new Bullet(x,GamePanel.HEIGHT+l,4,12,10,GamePanel.DOWN);
		ufoRect = new Rectangle(x,y,w,l);
		
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
	
	public void setSide(){	//sets side randomly
		if(Util.randint(0,1)==1){
			x = -w;
		}
		else{
			x = GamePanel.WIDTH;
			v *=-1;
		}
	}
	
	public void move(){	//moves ufo
		ufoRect = new Rectangle(x,y,w,l);
		x+=v;
	}
	
	public void shoot(){	//shoots at random
		if(Util.randint(0,GamePanel.shootChance)==GamePanel.shootChance && ufoBullet.y > GamePanel.HEIGHT){
			ufoBullet = new Bullet(x+w/2-2,y+12,4,12,10,GamePanel.DOWN);
			GamePanel.bullets.add(ufoBullet);
		}
	}
	
	public void draw(Graphics g){	//draws ufo and death animation
		g.setFont(fontLocal);
		g.setColor(Color.WHITE);
		if(ufodeath){	//tackles fancy death animation
			g.drawString("z",x,y+l);
			deathTimer++;
		}
		else{
			g.setColor(Color.RED);
			g.drawString("v",x,y+l);
		}
	}     
}