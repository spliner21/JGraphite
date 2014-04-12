package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


/**
 * Class for rectangle tool
 * @author spliner21
 */
public class Rectangle extends Tool {
	/* Rectangle start point */
	int beginningX = -1, beginningY = -1;
	/* Rectangle thickness */
	int stroke = 5;
	/* Rectangle fill decision - is rectangle going to be filled? */
	boolean fill = false;

	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public Rectangle(Graphics g1,Graphics g2) {
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
		if(fill)
			editing.fillRect(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
		else
			editing.drawRect(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
	}

	@Override
	public void mouseReleased(int x, int y) {
		if(fill)
			image.fillRect(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
		else
			image.drawRect(beginningX>x?x:beginningX, beginningY>y?y:beginningY, Math.abs(x-beginningX), Math.abs(y-beginningY));
	}

	/**
	 * Rectangle thickness getter
	 * @return oval thickness
	 */
	public int getStroke() {
		return stroke;
	}
	/**
	 * Rectangle thickness setter
	 * @param stroke new rectangle thickness
	 */
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
	/**
	 * Rectangle fill getter
	 * @return current rectangle fill status
	 */
	public boolean getFill() {
		return fill;
	}
	/**
	 * Rectangle fill setter
	 * @param fill is rectangle going to be filled?
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}
}
