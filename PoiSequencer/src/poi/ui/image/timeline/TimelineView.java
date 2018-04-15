package poi.ui.image.timeline;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import poi.ui.image.timeline.scale.TimeScaleRuler;

public class TimelineView {
	
	private TimelineModel model;

	private double zoomLevel = 1;
	
	private Map<ImageModel, TimelineImageNode> imageNodeMap = new HashMap<>();
	
	private HBox hbox;
	private ScrollPane scrollPane;

	public TimelineView(TimelineModel model) {
		this.model = model;
		hbox = new HBox();
		zoomLevel = 10;
//		scrollPane = new ScrollPane(hbox);
		BorderPane borderPane = new BorderPane(hbox);
		TimeScaleRuler ruler = new TimeScaleRuler(model, 1);
		borderPane.setBottom(ruler.getNode());
		
		scrollPane = new ScrollPane(borderPane);
		scrollPane.setFitToHeight(true);
//		scrollPane.minHeightProperty().bind(hbox.minHeightProperty().add(ruler.getNode().minHeightProperty()));

		model.getObserverManager().addObserver(TimelineModel.IMAGE_ADDED,
				dataAndIndex -> addImage(dataAndIndex.getFirst(), dataAndIndex.getSecond()));

		model.getObserverManager().addObserver(TimelineModel.IMAGE_REMOVED, this::removeImage);
	}
	
	private void addImage(ImageModel imageModel, int index) {
		TimelineImageNode img = new TimelineImageNode(imageModel, model);
		
		imageModel.getObserverManager().addObserver(ImageModel.DURATION_CHANGED, duration -> img.setWidth(zoomLevel * duration));
		
		img.setWidth(zoomLevel * img.getDuration());
		hbox.getChildren().add(img.getNode());
		imageNodeMap.put(imageModel, img);
		
	}
	
	public void removeImage(ImageModel imageModel) {
		AdjustableImageNode img = imageNodeMap.remove(imageModel);
		if (img != null) {
			hbox.getChildren().remove(img.getNode());
		}
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
		for (TimelineImageNode img : imageNodeMap.values()) {
			img.setWidth(zoomLevel * img.getPrefWidth());
		}
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}
	
	public ScrollPane getNode() {
		return scrollPane;
	}
	
}
