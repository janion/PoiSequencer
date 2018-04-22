package poi.ui.image;

import java.awt.image.BufferedImage;
import java.net.URL;

import poi.utility.ImageUtilities;

public class ImageData {

	private BufferedImage image;
	private URL filePath;

	public ImageData(BufferedImage image) {
		this(image, null);
	}
	
	public ImageData(ImageData toCopy) {
		this(ImageUtilities.copyImage(toCopy.getImage()), toCopy.getFilePath());
	}

	public ImageData(BufferedImage image, URL filePath) {
		this.image = image;
		this.filePath = filePath;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public void setFilePath(URL filePath) {
		this.filePath = filePath;
	}

	public BufferedImage getImage() {
		return image;
	}

	public URL getFilePath() {
		return filePath;
	}

}
