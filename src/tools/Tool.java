package tools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;



/**
 * Class representing tool used in editor.
 * @author spliner21
 */
public abstract class Tool {
	Graphics2D image;
	Graphics2D editing;

	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Tool(Graphics g1, Graphics g2) {
		image = (Graphics2D)g1;
		editing = (Graphics2D)g2;
		image.setBackground(Color.WHITE);
		editing.setBackground(Color.WHITE);
	}
	
	/**
	 * Method to use when mouse is pressed 
	 * @param x mouse X coordinate
	 * @param y mouse Y coordinate
	 */
	public abstract void mousePressed(int x, int y);

	/**
	 * Method to use when mouse is being held 
	 * @param x mouse X coordinate
	 * @param y mouse Y coordinate
	 */
	public abstract void mouseHold(int x, int y);

	/**
	 * Method to use when mouse is released 
	 * @param x mouse X coordinate
	 * @param y mouse Y coordinate
	 */
	public abstract void mouseReleased(int x, int y);
	
	/**
	 * Foreground color setter 
	 * @param c new foreground color
	 */
	public void setColor(Color c)
	{
		image.setColor(c);
		editing.setColor(c);
	}

	/**
	 * Background color setter 
	 * @param c new background color
	 */
	public void setBackgroundColor(Color c)
	{
		image.setBackground(c);
		editing.setBackground(c);
	}
	
	/**
	 * Both colors setter
	 * @param fg new foreground color
	 * @param bg new background color
	 */
	public void changeColors(Color fg, Color bg)
	{
		image.setColor(fg);
		editing.setColor(fg);
		image.setBackground(bg);
		editing.setBackground(bg);
	}
	
	/**
	 * Graphics refresher - setter for graphics reference
	 * @param g1 result image graphics
	 * @param g2 wip image graphics
	 */
	public void refreshGraphics(Graphics g1, Graphics g2) {
		image = (Graphics2D)g1;
		editing = (Graphics2D)g2;
	}
}
