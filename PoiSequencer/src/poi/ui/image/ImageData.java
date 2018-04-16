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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ImageData other = (ImageData) obj;
		if (filePath == null) {
			if (other.filePath != null) {
				return false;
			}
		} else if (!filePath.equals(other.filePath)) {
			return false;
		}
		if (image == null) {
			if (other.image != null) {
				return false;
			}
		} else if (!image.equals(other.image)) {
			return false;
		}
		return true;
	}

}
