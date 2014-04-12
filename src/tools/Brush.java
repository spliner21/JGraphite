package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


public class Brush extends Tool {
	int lastX = -1, lastY = -1;
	int stroke = 10;
	
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

	public int getStroke() {
		return stroke;
	}
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
}
