package poi.ui.image.timeline.scale;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import poi.observable.Observer;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;

public class TimeScaleRuler {
	
	private Observer<Double> durationChangeObserver;
	
	private TimelineModel timelineModel;
	private int scale;
	
	private HBox hbox;

	public TimeScaleRuler(TimelineModel timelineModel, int scale) {
		this.timelineModel = timelineModel;
		this.scale = scale;
		
		hbox = new HBox();
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		hbox.setMinHeight(20);
		refresh();
		
		durationChangeObserver = value -> refresh();

		timelineModel.getObserverManager().addObserver(TimelineModel.IMAGE_ADDED, imageModelAndIndex -> imageAdded(imageModelAndIndex.getFirst()));
		timelineModel.getObserverManager().addObserver(TimelineModel.IMAGE_REMOVED, this::imageRemoved);
	}
	
	private void imageAdded(ImageModel imageModel) {
		imageModel.getObserverManager().addObserver(ImageModel.DURATION_CHANGED, durationChangeObserver);
		refresh();
	}
	
	private void imageRemoved(ImageModel imageModel) {
		imageModel.getObserverManager().removeObserver(ImageModel.DURATION_CHANGED, durationChangeObserver);
		refresh();
	}
	
	private void refresh() {
		hbox.getChildren().clear();
		
		hbox.getChildren().addAll(createLine(0));
		int t = scale;
		while(t <= timelineModel.getTotalDuration()) {
			hbox.getChildren().addAll(createSpacer(), createLine(t));
			t += scale;
		}
	}
	
	private Pane createLine(int time) {
		Pane pane = new Pane();
		pane.setPrefWidth(2);
		HBox.setHgrow(pane, Priority.NEVER);
		pane.setStyle("-fx-border-color: BLACK");
		
		if (time % (5 * scale) != 0) {
			pane.minHeightProperty().bind(hbox.heightProperty().divide(2));
			pane.maxHeightProperty().bind(hbox.heightProperty().divide(2));
		}
		return pane;
	}
	
	private Pane createSpacer() {
		Pane pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);
		return pane;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
		refresh();
	}
	
	public HBox getNode() {
		return hbox;
	}

}
