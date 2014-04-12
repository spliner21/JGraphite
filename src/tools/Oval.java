package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


/**
 * Class for oval tool
 * @author spliner21
 */
public class Oval extends Tool {
	/* Oval start point */
	int beginningX = -1, beginningY = -1;
	/* Oval thickness */
	int stroke = 5;
	/* Oval fill decision - is oval going to be filled? */
	boolean fill = false;

	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Oval(Graphics g1,Graphics g2) {
		super(g1,g2);
		
	}

	@Override
	public void mousePressed(int x, int y) {
		image.setStroke(new BasicStroke(stroke));
		editing.setStroke(new BasicStroke(stroke));
		beginningX = x;
		beginningY = y;
	}

	@Override
	public void mouseHold(int x, int y) {
		if(fill)
			editing.fillOval(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
		else
			editing.drawOval(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
	}

	@Override
	public void mouseReleased(int x, int y) {
		if(fill)
			image.fillOval(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
		else
			image.drawOval(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
	}

	/**
	 * Oval thickness getter
	 * @return oval thickness
	 */
	public int getStroke() {
		return stroke;
	}
	/**
	 * Oval thickness setter
	 * @param stroke new oval thickness
	 */
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
	/**
	 * Oval fill getter
	 * @return current oval fill status
	 */
	public boolean getFill() {
		return fill;
	}
	/**
	 * Oval fill setter
	 * @param fill is oval going to be filled?
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}
}
