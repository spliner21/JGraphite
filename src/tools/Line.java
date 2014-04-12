package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


public class Line extends Tool {
	int beginningX, beginningY;
	int stroke = 5;
	

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

	public int getStroke() {
		return stroke;
	}
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
}
