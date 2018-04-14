package poi.ui.image;

import java.awt.image.BufferedImage;
import java.net.URL;

public class ImageData {

	private BufferedImage image;
	private URL filePath;

	public ImageData(BufferedImage image) {
		this(image, null);
	}

	public ImageData(BufferedImage image, URL filePath) {
		this.image = image;
		this.filePath = filePath;
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
