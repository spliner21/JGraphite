package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


/**
 * Class for brush tool
 * @author spliner21
 */
public class Brush extends Tool {
	/* Last pressed point coordinates */
	int lastX = -1, lastY = -1;
	/* Brush size */
	int stroke = 10;
	
	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Brush(Graphics g1,Graphics g2) {
		super(g1,g2);
		
	}

	@Override
	public void mousePressed(int x, int y) {
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
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

	/**
	 * Brush size getter
	 * @return brush size
	 */
	public int getStroke() {
		return stroke;
	}
	/**
	 * Brush size setter
	 * @param stroke new brush size
	 */
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
}
