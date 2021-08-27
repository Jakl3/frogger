import java.awt.Graphics;
import java.awt.Color;
import java.awt.Canvas;
import java.awt.event.*;

public class Box extends Canvas implements KeyListener
{
	private int x, y, w, h;  //these are instance variables
	
		//this is the constructor
	public Box( int ex, int wy, int wd, int ht)
	{
		x = ex;
		y = wy;
		w = wd;
		h = ht;		
	}

	public void paint( Graphics window )
	{
		//this rectangle shows you the boundaries of what you are drawing
		window.setColor( Color.RED );
		window.drawRect( x, y, w, h );
			
		window.setColor(Color.GREEN);
		window.fillRect( x + w / 20 , y + h / 20, w - w / 10, h - h / 10);
		
		window.setColor(Color.WHITE);
		window.fillRect( x + w / 5 , y + h / 5, w - w / 2, h - h / 2);	
			
		//make your own fancy box or any shape you want
		//your shape must be resizeable and scaleable
		
			
	}
	
	public void changePosBy10()
	{

	}
	
	public void keepInBounds()
	{
		if ( x > 725)
			x = 25;
		if( y > 525 )
			y = 25;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if( e.getKeyCode()  == KeyEvent.VK_SPACE )
		{
			y += 10;
			repaint();
		}


	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}