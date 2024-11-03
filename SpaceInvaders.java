import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceInvaders extends JFrame{
	GamePanel game= new GamePanel();
	public static final int LEFT = 1, RIGHT = 2;
	public SpaceInvaders(){
		super("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(game);
		pack();  // set the size of my Frame exactly big enough to hold the contents
		setVisible(true);
	}

	public static void main(String[] arguments) {
		SpaceInvaders frame = new SpaceInvaders();		
	}
}

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	public static final int INTRO=0, GAME=1, END=2, LEFT = 1, RIGHT = 2;
	private int screen = INTRO;

	private boolean []keys;
	Timer timer;
	Image back;
	private Enemy [] enemies;
	private Player player;
	private int score; 

	public GamePanel(){
		back = new ImageIcon("intro.png").getImage();
		keys = new boolean[KeyEvent.KEY_LAST+1];

		setPreferredSize(new Dimension(800, 600));
		score=0;
		enemies = new Enemy[50];
		for(int i=0; i<enemies.length; i++){
			enemies[i] = new Enemy(50*i+50,50,5,10);
		}
		player = new Player(400,550,37,39,10);
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
		timer = new Timer(20, this);
		timer.start();
	}

	public void move(){
		if(screen == GAME){
			player.move(keys);
			for(Enemy e: enemies){
				e.move();
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
	public void keyTyped(KeyEvent ke){}
	@Override
	public void	mouseClicked(MouseEvent e){}

	@Override
	public void	mouseEntered(MouseEvent e){}

	@Override
	public void	mouseExited(MouseEvent e){}

	@Override
	public void	mousePressed(MouseEvent e){
		if(screen == INTRO){
			screen = GAME;
		}
	}

	@Override
	public void	mouseReleased(MouseEvent e){}

	@Override
	public void paint(Graphics g){
		if(screen == INTRO){
			g.drawImage(back,0,0,null);					
		}
		else if(screen == GAME){
			g.setColor(Color.BLACK);
			g.fillRect(0,0,800,600);
			Font fnt = new Font("Arial",Font.PLAIN,32);
			g.setColor(Color.WHITE);
			g.setFont(fnt);
			g.drawString(""+score, 400,100); 
			for(Enemy e: enemies){
				e.draw(g);
			}
			player.draw(g);
		}
	}
}