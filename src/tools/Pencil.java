package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


/**
 * Class for pencil tool
 * @author spliner21
 */
public class Pencil extends Tool {
	/* Last pressed point coordinates */
	int lastX = -1, lastY = -1;

	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Pencil(Graphics g1,Graphics g2) {
		super(g1,g2);
		
	}

	@Override
	public void mousePressed(int x, int y) {
		image.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		lastX = x;
		lastY = y;
		
	}

	@Override
	public void mouseHold(int x, int y) {
		image.drawLine(lastX, lastY, x, y);
		lastX = x;
		lastY = y;
		
	}

	@Override
	public void mouseReleased(int x, int y) {
		image.drawLine(lastX, lastY, x, y);
		lastX = -1;
		lastY = -1;
		
	}

}
