package poi.ui.image.timeline;

import poi.ui.image.ImageData;

public class ImageModel {
	
	private double duration;
	private ImageData imageData;
	
	public ImageModel(double duration, ImageData imageData) {
		this.duration = duration;
		this.imageData = imageData;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public ImageData getImageData() {
		return imageData;
	}

}
