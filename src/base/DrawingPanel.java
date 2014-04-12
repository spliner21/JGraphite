package base;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.JPanel;


/**
 * Custom implementation of JPanel, designed to be a drawing area.
 * @author spliner21
 */
public class DrawingPanel extends JPanel {
	/**
	 * Serial Version UID of this implementation.
	 */
	private static final long serialVersionUID = -8806192107103540586L;
	
	/* Result and Work-In-Progress images */
	private BufferedImage resultImage;
	private BufferedImage wipImage;
	
	
	/**
	 * Result image getter
	 * @return result image object
	 */
	public BufferedImage getResultImage() {
		return resultImage;
	}

	/**
	 * Result image setter
	 * @param resultImage new result image object
	 */
	public void setResultImage(BufferedImage resultImage) {
		this.resultImage = resultImage;
	}

	/**
	 * Wip image getter
	 * @return wip image object
	 */
	public BufferedImage getWipImage() {
		return wipImage;
	}

	/**
	 * Wip image setter
	 * @param wipImage new wip image object
	 */
	public void setWipImage(BufferedImage wipImage) {
		this.wipImage = wipImage;
	}
	
	/**
	 * Image objects setter
	 * @param res new result image object
	 * @param wip new wip image object
	 */
	public void changeImages(BufferedImage res, BufferedImage wip) {
		resultImage = res;
		wipImage = wip;
	}

	/**
	 * Result image data getter
	 * @return result image data
	 */
	public Raster getResultImageData() {
		return resultImage.getData();
	}

	/**
	 * Result image data setter
	 * @param resultImageData new data for result image
	 */
	public void setResultImageData(Raster resultImageData) {
		this.resultImage.setData(resultImageData);
	}

	/**
	 * Wip image data getter
	 * @return wip image data
	 */
	public Raster getWipImageData() {
		return wipImage.getData();
	}

	/**
	 * Wip image data setter
	 * @param wipImageData new data for wip image
	 */
	public void setWipImageData(Raster wipImageData) {
		this.wipImage.setData(wipImageData);
	}

	/**
	 * Method that copies data from result image to wip image.
	 */
	public void copyResultToWip() {
		wipImage.setData(resultImage.getData());
	}
	
	
	/**
	 * Constructor that uses image dimensions.
	 * @param imgWidth image X dimension
	 * @param imgHeight imags Y dimension
	 */
	public DrawingPanel(int imgWidth, int imgHeight) {
		super();
		constructImage(imgWidth,imgHeight);
	}
	
	/**
	 * Method, that creates new image objects
	 * @param imgWidth new image X dimension
	 * @param imgHeight new image Y dimension
	 */
	public void constructImage(int imgWidth, int imgHeight) {
		resultImage = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);
		wipImage = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);
		Graphics2D gc = (Graphics2D)resultImage.getGraphics();
        gc.setColor(Color.WHITE);
        gc.fillRect(0, 0, imgWidth, imgHeight); 
	}

	/**
	 * Method, that draws result image on panel
	 */
	public void drawResultImage() {
		Graphics g = this.getGraphics();
        g.drawImage(resultImage,0 ,0 ,null);
	}
	
	/**
	 * Method, that draws wip image on panel
	 */
	public void drawWipImage() {
		Graphics g = this.getGraphics();
        g.drawImage(wipImage,0 ,0 ,null);
	}
	
	@Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(resultImage,0 ,0 ,null);
    }
}
