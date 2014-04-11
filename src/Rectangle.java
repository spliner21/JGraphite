import java.awt.BasicStroke;
import java.awt.Graphics;


public class Rectangle extends Tool {
	int beginningX = -1, beginningY = -1;
	int stroke = 5;
	boolean fill = false;

	Rectangle(Graphics g1,Graphics g2) {
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

	public int getStroke() {
		return stroke;
	}
	public void setStroke(int stroke) {
		this.stroke = stroke;
		image.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
		editing.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}
	public boolean getFill() {
		return fill;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
	}
}
