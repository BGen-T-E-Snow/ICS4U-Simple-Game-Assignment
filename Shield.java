//Shield.java
//Spencer Trepanier
//Shield class creates a shield object.
//Shields are an arraylist of rectangles, each vulnerable to enemy/ufo or player bullets
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Shield{
	public static int x,y,w,l;
	public Rectangle sRect;
	public ArrayList<Rectangle> shieldSquares;

	public Shield(int xx, int ww, int ll) {
		x = xx;
		y = 450;
		w = ww;
		l = ll;
		shieldSquares = new ArrayList<Rectangle>();
		for(int i=0; i<12; i++){	//builds the shield
			for(int j=0; j<8; j++){
				shieldSquares.add(new Rectangle(x+i*5,y+j*5,w/5,l/5));
			}
		}
		sRect = new Rectangle(x,y,w,l);
	}
	
	public void draw(Graphics g){	//draws the shield
		g.setColor(Color.GREEN);
		for(Rectangle sS:shieldSquares){	//draws each individual square
			g.fillRect((int)sS.getX(),(int)sS.getY(),(int)sS.getWidth(),(int)sS.getHeight());
		}
	}     
}