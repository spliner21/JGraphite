import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;


public class Manipulators {

	public static BufferedImage rotate(BufferedImage img, int angle) { 
		double radians = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(radians)), cos = Math.abs(Math.cos(radians));
	    int w = img.getWidth(), h = img.getHeight();
	    int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
	    
	    GraphicsConfiguration gc = getDefaultConfiguration();
	    BufferedImage result = gc.createCompatibleImage(neww, newh, img.getTransparency());
	    Graphics2D g = result.createGraphics();
	    
	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(radians, w/2, h/2);
	    g.drawRenderedImage(img, null);
	    
	    g.dispose();
	    return result; 
	}
	
	public static Raster flip(BufferedImage img, boolean vertical) {
		int w,h,width,height;
		w = (!vertical)?-1:1;
		h = vertical?-1:1;
		width = (!vertical)?-img.getWidth():0;
		height = vertical?-img.getHeight():0;
		AffineTransform tx = AffineTransform.getScaleInstance(w,h);
		tx.translate(width,height);
		AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null).getData();
	}
	
	public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
}
