import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.JPanel;


public class DrawingPanel extends JPanel {
	private BufferedImage resultImage;
	private BufferedImage wipImage;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BufferedImage getResultImage() {
		return resultImage;
	}

	public void setResultImage(BufferedImage resultImage) {
		this.resultImage = resultImage;
	}

	public BufferedImage getWipImage() {
		return wipImage;
	}

	public void setWipImage(BufferedImage wipImage) {
		this.wipImage = wipImage;
	}

	public Raster getResultImageData() {
		return resultImage.getData();
	}

	public void setResultImageData(Raster resultImageData) {
		this.resultImage.setData(resultImageData);
	}

	public Raster getWipImageData() {
		return wipImage.getData();
	}

	public void setWipImageData(Raster wipImageData) {
		this.wipImage.setData(wipImageData);
	}

	public void copyResultToWip() {
		wipImage.setData(resultImage.getData());
	}
	
	
	public DrawingPanel(int imgWidth, int imgHeight) {
		super();
		constructImage(imgWidth,imgHeight);
	}
	
	public void changeImages(BufferedImage res, BufferedImage wip) {
		resultImage = res;
		wipImage = wip;
	}
	
	public void constructImage(int imgWidth, int imgHeight) {
		resultImage = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);
		wipImage = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);
		Graphics2D gc = (Graphics2D)resultImage.getGraphics();
        gc.setColor(Color.WHITE);
        gc.fillRect(0, 0, imgWidth, imgHeight); 
	}
	
	@Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(resultImage,0 ,0 ,null);
    }

	public void drawResultImage() {
		Graphics g = this.getGraphics();
        g.drawImage(resultImage,0 ,0 ,null);
	}
	
	public void drawWipImage() {
		Graphics g = this.getGraphics();
        g.drawImage(wipImage,0 ,0 ,null);
	}
}
