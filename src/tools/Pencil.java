package tools;
import java.awt.BasicStroke;
import java.awt.Graphics;


public class Pencil extends Tool {
	int lastX = -1, lastY = -1;

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
