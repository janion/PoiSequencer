package poi.ui.image.loaded;

import java.awt.image.BufferedImage;

import javafx.scene.image.ImageView;
import poi.ui.image.ImageData;
import poi.ui.image.utility.ImageUtilities;

public class LoadedImageNode {
	
	private static final int SCALE_FACTOR = 4;
	
	private ImageData imageData;
	private ImageView imageView;
	
	public LoadedImageNode(ImageData imageData) {
		this.imageData = imageData;
		
		BufferedImage img = imageData.getImage();
		imageView = new ImageView(ImageUtilities.resample(img, SCALE_FACTOR));
	}
	
	public void refresh() {
		BufferedImage img = imageData.getImage();
		imageView.setImage(ImageUtilities.resample(img, SCALE_FACTOR));
	}
	
	public ImageView getNode() {
		return imageView;
	}

}
