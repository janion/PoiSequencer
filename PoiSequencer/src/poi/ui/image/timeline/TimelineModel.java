package poi.ui.image.timeline;

import java.util.ArrayList;
import java.util.List;

public class TimelineModel {
	
	private List<ImageModel> images = new ArrayList<>();
	
	public List<ImageModel> getImages() {
		return images;
	}
	
	public void addImage(ImageModel image) {
		images.add(image);
	}
	
	public void addImage(ImageModel image, int index) {
		images.add(index, image);
	}
	
	public void removeImage(ImageModel image) {
		images.remove(image);
	}

}
