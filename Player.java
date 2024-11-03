//Player.java
//Spencer Trepanier
//Player class creates a player object.
//Player can move left, right, and shoot (makes fancy noise).
//Player is vulnerable to being hit by enemy/ufo bullets.
//Players bullets destroy shields pixels upon collision.
import java.awt.*;
import java.awt.geom.*;
import java.applet.*;
import java.io.*;

public class Player{
	public static int x,y,w,l,v,left,right,shoot;
	public Bullet pBullet;
	public Rectangle pRect;
	File wavFile = new File("sfx_wpn_laser6.wav");
	AudioClip sound;
	Font fontLocal = null;
	public boolean pdeath;
	public int deathTimer;

	public Player(int xx, int ww, int ll, int vv, int lft, int rgt, int sht) {
		x = xx;
		y = 500;
		w = ww;
		l = ll;
		v = vv;
		left = lft;
		right = rgt;
		shoot = sht;
		pdeath = false;
		pRect = new Rectangle(x,y,w,l);
		pBullet = new Bullet(x,-l,4,12,10,GamePanel.UP);
		try{
			sound = Applet.newAudioClip(wavFile.toURL());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		String fName = "invaders.ttf";
		InputStream is = GamePanel.class.getResourceAsStream(fName);
		try{
			fontLocal = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(65f);
		}
		catch(IOException ex){
			System.out.println(ex);	
		}
		catch(FontFormatException ex){
			System.out.println(ex);	
		}
	}
	
	public void move(boolean []keys){	//moves player
		pRect = new Rectangle(x,y,w,l);
		if(keys[left] && x > 0){
			x -= v;
		}
		if(keys[right] && x < GamePanel.WIDTH-w){
			x += v;
		}
		if(keys[shoot] && pBullet.y < 0 && GamePanel.frameCounter>2){	//shoots after 2 frames of the game have elapsed (prevents shooting before the game has started)
			sound.play();
			pBullet = new Bullet(x+w/2-2,y+12,4,12,10,GamePanel.UP);
			GamePanel.bullets.add(pBullet);
		}
	}
	
	public void draw(Graphics g){	//draws player
		g.setColor(Color.GREEN);
		g.setFont(fontLocal);
		if(pdeath){	//tackles fancy player hit animation
			g.drawString("x",x,y+l);
			deathTimer++;
		}
		else{
			g.drawString("w",x,y+l);
		}
	}
}