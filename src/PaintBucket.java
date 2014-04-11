import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;


public class PaintBucket extends Tool {
	Color accepted;	// currently accepted color
	boolean mark[][];
	Queue<Point> pointsQueue;

	PaintBucket(Graphics g1,Graphics g2) {
		super(g1,g2);

	}

	@Override
	public void mousePressed(int x, int y) {

		image.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}

	@Override
	public void mouseHold(int x, int y) {
		
	}

	@Override
	public void mouseReleased(int x, int y) {
		
	}


	public void fillWithColor(int x, int y, BufferedImage img) {
		int crgb = img.getRGB(x, y);
		mark = new boolean[img.getWidth()][img.getHeight()];
		accepted = new Color(crgb & 0x00FFFFFF);
		if(accepted.equals(image.getColor()))
			return;
		pointsQueue = new LinkedList<Point>();
		
		pointsQueue.add(new Point(x,y));
		
		Point p;
		while(!pointsQueue.isEmpty()) {
			
			p = pointsQueue.remove();
			if(mark[p.x][p.y])
				continue;
			mark[p.x][p.y] = true;
			crgb = img.getRGB(p.x, p.y);
			Color c = new Color(crgb & 0x00FFFFFF);
			if(!c.equals(accepted))
				continue;
			
			image.drawLine(p.x,p.y,p.x,p.y);
			
			if(p.x+1 < img.getWidth() && !mark[p.x+1][p.y])
				pointsQueue.add(new Point(p.x+1,p.y));
			if(p.x-1 > 0 && !mark[p.x-1][p.y])
				pointsQueue.add(new Point(p.x-1,p.y));
			if(p.y+1 < img.getHeight() && !mark[p.x][p.y+1])
				pointsQueue.add(new Point(p.x,p.y+1));
			if(p.y-1 > 0 && !mark[p.x][p.y-1])
				pointsQueue.add(new Point(p.x,p.y-1));
			
		
		}
	}
	
}
