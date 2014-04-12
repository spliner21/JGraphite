package tools;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Class for paint bucket tool
 * @author spliner21
 */
public class PaintBucket extends Tool {
	/* Currently accepted color */
	Color accepted;
	/* Tool visits marks - set to true, when tool visits (x,y) pixel */
	boolean mark[][];
	/* Tool visits queue */
	Queue<Point> pointsQueue;

	/**
	 * Constructor with images graphic objects
	 * @param g1 result image graphics object
	 * @param g2 wip image graphics object
	 */
	public PaintBucket(Graphics g1,Graphics g2) {
		super(g1,g2);

	}

	@Override
	public void mousePressed(int x, int y) {
		image.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	}

	@Override
	@Deprecated
	public void mouseHold(int x, int y) {
		
	}

	@Override
	@Deprecated
	public void mouseReleased(int x, int y) {
		
	}


	/**
	 * Fill image part with color, starting in (x,y)
	 * Implementation of non-recursive Flood Fill algorithm, based on Queue. 
	 * @param x start point X coordinate
	 * @param y start point Y coordinate
	 * @param img image to be filled
	 */
	public void fillWithColor(int x, int y, BufferedImage img) {
		/* Check if coordinates are not outside the image */
		if(x > img.getWidth() || y > img.getHeight())
			return;
		
		/* Set reference color to be replaced */
		int crgb = img.getRGB(x, y);
		accepted = new Color(crgb & 0x00FFFFFF);
		
		/* Check if we are replacing color with the same color */
		if(accepted.equals(image.getColor()))
			return;

		/* Initialize marks array */
		mark = new boolean[img.getWidth()][img.getHeight()];
		
		/* Initialize points queue */
		pointsQueue = new LinkedList<Point>();
		
		pointsQueue.add(new Point(x,y));
		
		/* Main loop of the algorithm */
		Point p;
		while(!pointsQueue.isEmpty()) {
			/* get next point from queue */
			p = pointsQueue.remove();
			
			/* if point was already visited, skip this iteration */
			if(mark[p.x][p.y])
				continue;
			
			/* mark the point as visited */
			mark[p.x][p.y] = true;
			
			/* get point color, and compare it with reference */
			crgb = img.getRGB(p.x, p.y);
			Color c = new Color(crgb & 0x00FFFFFF);
			if(!c.equals(accepted))
				continue;
			
			image.drawLine(p.x,p.y,p.x,p.y);
			
			/* Queue next four neighbors */
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
