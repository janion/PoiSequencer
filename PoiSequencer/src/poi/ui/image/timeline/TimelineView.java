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
	
	private TimelineModel model;

	private double zoomLevel = 1;
	
	private Map<ImageModel, TimelineImageNode> imageNodeMap = new HashMap<>();
	
	private HBox hbox;
	private TimeScaleRuler ruler;
	private ScrollPane scrollPane;

	public TimelineView(TimelineModel model) {
		this.model = model;
		hbox = new HBox();
		zoomLevel = 10;
		BorderPane borderPane = new BorderPane(hbox);
		ruler = new TimeScaleRuler(model, 1);
		borderPane.setBottom(ruler.getNode());
		
		scrollPane = new ScrollPane(borderPane);
		scrollPane.setFitToHeight(true);

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
			img.setWidth(zoomLevel * img.getDuration());
		}
		
		ruler.setTickIncrement(10.0 / zoomLevel);
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}
	
	public BorderPane getNode() {
		BorderPane borderPane = new BorderPane(scrollPane);
		
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

		plusButton.setOnAction(event -> setZoomLevel(zoomLevel * 2));
		minusButton.setOnAction(event -> setZoomLevel(zoomLevel / 2));

		VBox.setVgrow(pane1, Priority.ALWAYS);
		VBox.setVgrow(pane2, Priority.ALWAYS);
		
		borderPane.setRight(new VBox(pane1, pane2));
		return borderPane;
	}
	
}
