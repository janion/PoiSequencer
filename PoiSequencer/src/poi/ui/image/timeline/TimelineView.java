package poi.ui.image.timeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class TimelineView {
	
	private TimelineModel model;

	private double zoomLevel = 1;
	private List<AdjustableImageNode> imageNodes = new ArrayList<>();
	
	private Map<ImageModel, AdjustableImageNode> imageNodeMap = new HashMap<>();
	
	private HBox hbox;
	private ScrollPane scrollPane;

	public TimelineView(TimelineModel model) {
		this.model = model;
		hbox = new HBox();
		scrollPane = new ScrollPane(hbox);
		scrollPane.setFitToHeight(true);

		model.getObserverManager().addObserver(TimelineModel.IMAGE_ADDED,
				dataAndIndex -> addImage(dataAndIndex.getFirst(), dataAndIndex.getSecond()));

		model.getObserverManager().addObserver(TimelineModel.IMAGE_REMOVED, this::removeImage);
	}
	
	private void addImage(ImageModel imageModel, int index) {
		AdjustableImageNode img = new AdjustableImageNode(
				SwingFXUtils.toFXImage(imageModel.getImageData().getImage(), null), imageModel.getDuration());

		img.setWidth(zoomLevel * img.getPrefWidth());
		imageNodes.add(img);
		hbox.getChildren().add(img.getNode());
	}
	
	public void removeImage(ImageModel imageModel) {
		AdjustableImageNode img = imageNodeMap.remove(imageModel);
		if (img != null) {
			imageNodes.remove(img);
			hbox.getChildren().remove(img.getNode());
		}
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
	
	public ScrollPane getNode() {
		return scrollPane;
	}
	
}
