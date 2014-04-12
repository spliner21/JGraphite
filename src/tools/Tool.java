package tools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;



/**
 * Class representing tool used in editor.
 * @author spliner21
 *
 */
public abstract class Tool {
	Graphics2D image;
	Graphics2D editing;
	
	public Tool(Graphics g1, Graphics g2) {
		image = (Graphics2D)g1;
		editing = (Graphics2D)g2;
		image.setBackground(Color.WHITE);
		editing.setBackground(Color.WHITE);
	}
	
	public abstract void mousePressed(int x, int y);
	
	public abstract void mouseHold(int x, int y);
	
	public abstract void mouseReleased(int x, int y);
	
	public void setColor(Color c)
	{
		image.setColor(c);
		editing.setColor(c);
	}
	
	public void setBackgroundColor(Color c)
	{
		image.setBackground(c);
		editing.setBackground(c);
	}
	
	public void changeColors(Color fg, Color bg)
	{
		image.setColor(fg);
		editing.setColor(fg);
		image.setBackground(bg);
		editing.setBackground(bg);
	}
	
	public void refreshGraphics(Graphics g1, Graphics g2) {
		image = (Graphics2D)g1;
		editing = (Graphics2D)g2;
	}
}
