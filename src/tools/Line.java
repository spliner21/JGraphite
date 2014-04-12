package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


/**
 * Class for line tool
 * @author spliner21
 */
public class Line extends Tool {
	/* line start point */
	int beginningX, beginningY;
	/* line thickness */
	int stroke = 5;
	
	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Line(Graphics g1,Graphics g2) {
		super(g1,g2);
		
	}

	@Override
	public void mousePressed(int x, int y) {
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		beginningX = x;
		beginningY = y;
	}

	@Override
	public void mouseHold(int x, int y) {
		editing.drawLine(beginningX, beginningY, x, y);
		
	}

	@Override
	public void mouseReleased(int x, int y) {
		image.drawLine(beginningX, beginningY, x, y);
	}

	/**
	 * Line thickness getter
	 * @return line thickness
	 */
	public int getStroke() {
		return stroke;
	}
	/**
	 * Line thickness setter
	 * @param stroke new line thickness
	 */
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
}
