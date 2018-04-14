package poi.ui.image.timeline;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class TimelineView {

	private double zoomLevel = 1;
	private List<AdjustableImageNode> imageNodes = new ArrayList<>();
	
	private HBox hbox;
	private ScrollPane scrollPane;

	public TimelineView() {
		hbox = new HBox();
		scrollPane = new ScrollPane(hbox);
		scrollPane.setFitToHeight(true);
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
		for (AdjustableImageNode img : imageNodes) {
			img.setWidth(zoomLevel * img.getPrefWidth());
		}
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}
	
	public void addImage(AdjustableImageNode img) {
		img.setWidth(zoomLevel * img.getPrefWidth());
		imageNodes.add(img);
		hbox.getChildren().add(img.getNode());
	}
	
	public void removeImage(AdjustableImageNode img) {
		imageNodes.remove(img);
		hbox.getChildren().remove(img.getNode());
	}
	
	public ScrollPane getNode() {
		return scrollPane;
	}
	
}
