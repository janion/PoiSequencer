package poi.ui.image.timeline;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import poi.ui.image.timeline.scale.TimeScaleRuler;

public class TimelineView {

	private static final double INITIAL_ZOOM_LEVEL = 10;
	private static final double ZOOM_INCREMENT = 2;
	private static final double MAX_ZOOM_LEVEL = INITIAL_ZOOM_LEVEL * Math.pow(ZOOM_INCREMENT, 4);
	private static final double MIN_ZOOM_LEVEL = INITIAL_ZOOM_LEVEL / Math.pow(ZOOM_INCREMENT, 4);

	private TimelineModel model;

	private double zoomLevel;
	
	private Map<ImageModel, TimelineImageNode> imageNodeMap = new HashMap<>();
	
	private HBox hbox;
	private TimeScaleRuler ruler;
	private ScrollPane scrollPane;
	private BorderPane borderPane;

	public TimelineView(TimelineModel model) {
		this.model = model;
		hbox = new HBox();
		zoomLevel = INITIAL_ZOOM_LEVEL;
		BorderPane internalBorderPane = new BorderPane(hbox);
		ruler = new TimeScaleRuler(model, 1);
		internalBorderPane.setBottom(ruler.getNode());
		
		scrollPane = new ScrollPane(internalBorderPane);
		scrollPane.setFitToHeight(true);
		
		borderPane = new BorderPane(scrollPane);
		
		Pane pane1 = new StackPane();
		Pane inner1 = new StackPane();
		Button plusButton = new Button("+");
		plusButton.prefHeightProperty().bind(inner1.heightProperty());
//		plusButton.prefWidthProperty().bind(inner1.widthProperty());
		pane1.getChildren().addAll(inner1, plusButton);
		
		Pane pane2 = new StackPane();
		Pane inner2 = new StackPane();
		Button minusButton = new Button("-");
		minusButton.prefHeightProperty().bind(inner2.heightProperty());
		minusButton.prefWidthProperty().bind(inner2.widthProperty());
		pane2.getChildren().addAll(inner2, minusButton);

		plusButton.setOnAction(event -> zoomIn());
		minusButton.setOnAction(event -> zoomOut());

		VBox.setVgrow(pane1, Priority.ALWAYS);
		VBox.setVgrow(pane2, Priority.ALWAYS);
		
		borderPane.setRight(new VBox(pane1, pane2));

		model.getObserverManager().addObserver(TimelineModel.IMAGE_ADDED,
				dataAndIndex -> addImage(dataAndIndex.getFirst(), dataAndIndex.getSecond()));

		model.getObserverManager().addObserver(TimelineModel.IMAGE_REMOVED, this::removeImage);
		model.getObserverManager().addObserver(TimelineModel.CLEARED, nullValue -> clear());
	}
	
	private void zoomIn() {
		if (zoomLevel < MAX_ZOOM_LEVEL) {
			setZoomLevel(zoomLevel * ZOOM_INCREMENT);
		}
	}
	
	private void zoomOut() {
		if (zoomLevel > MIN_ZOOM_LEVEL) {
			setZoomLevel(zoomLevel / ZOOM_INCREMENT);
		}
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
	
	public void clear() {
		imageNodeMap.clear();
		hbox.getChildren().clear();
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
		for (TimelineImageNode img : imageNodeMap.values()) {
			img.setWidth(zoomLevel * img.getDuration());
		}

		ruler.setTickIncrement(nearestIncrement(10.0 / zoomLevel));
	}
	
	private double nearestIncrement(double requested) {
		double[] options = {0.1, 0.5, 1, 2, 5, 10, 20, 50, 100};
		
		double closeness = 10000;
		double best = 0;
		
		for (double option : options) {
			double newCloseness = Math.abs(1 - requested / option);
			if (newCloseness < closeness) {
				closeness = newCloseness;
				best = option;
			}
		}
		
		return best;
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}
	
	public BorderPane getNode() {
		return borderPane;
	}
	
}
