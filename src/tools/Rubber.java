package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


/**
 * Class for rubber tool
 * @author spliner21
 */
public class Rubber extends Tool {
	/* Last pressed point coordinates */
	int lastX = -1, lastY = -1;
	/* Rubber size */
	int stroke = 10;

	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Rubber(Graphics g1,Graphics g2) {
		super(g1,g2);
		
	}

	@Override
	public void mousePressed(int x, int y) {
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		image.setBackground(editing.getColor());
		image.setColor(editing.getBackground());
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
		editing.setBackground(image.getColor());
		editing.setColor(image.getBackground());
		lastX = -1;
		lastY = -1;
		
	}

	/**
	 * Rubber size getter
	 * @return rubber size
	 */
	public int getStroke() {
		return stroke;
	}
	/**
	 * Rubber size setter
	 * @param stroke new rubber size
	 */
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
}
