package poi.ui.image.loaded;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import poi.ui.image.ImageData;

public class LoadedImageNode {
	
	private ImageData imageData;
	private ImageView imageView;
	
	public LoadedImageNode(ImageData imageData) {
		this.imageData = imageData;
		
		BufferedImage img = imageData.getImage();
		imageView = new ImageView(SwingFXUtils.toFXImage(img, null));
		imageView.setFitHeight(64);
		imageView.setFitWidth(64 * (img.getWidth() / (double) img.getHeight()));
	}
	
	public ImageView getNode() {
		return imageView;
	}

}
