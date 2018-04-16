package poi.ui.image.timeline.scale;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import poi.observable.Observer;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;

public class TimeScaleRuler {
	
	private Observer<Double> durationChangeObserver;
	
	private TimelineModel timelineModel;
	private double tickIncrement;
	
	private HBox hbox;

	public TimeScaleRuler(TimelineModel timelineModel, double scale) {
		this.timelineModel = timelineModel;
		this.tickIncrement = scale;
		
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
		double t = tickIncrement;
		while(t <= timelineModel.getTotalDuration()) {
			hbox.getChildren().addAll(createSpacer(), createLine(t));
			t += tickIncrement;
		}
	}
	
	private Pane createLine(double time) {
		Pane pane = new Pane();
		pane.setPrefWidth(2);
		HBox.setHgrow(pane, Priority.NEVER);
		pane.setStyle("-fx-border-color: BLACK");

		if (Math.abs(time % (5 * tickIncrement)) > 0.0001
				&& Math.abs((time % (5 * tickIncrement)) - (5 * tickIncrement)) > 0.0001) {
			pane.minHeightProperty().bind(hbox.heightProperty().divide(4));
			pane.maxHeightProperty().bind(hbox.heightProperty().divide(4));
		} else {
			Label label = new Label(String.format("%.1f", time));
			label.setTranslateX(3);
			pane.getChildren().add(label);
		}
		return pane;
	}
	
	private Pane createSpacer() {
		Pane pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);
		return pane;
	}
	
	public void setTickIncrement(double tickIncrement) {
		this.tickIncrement = tickIncrement;
		refresh();
	}
	
	public HBox getNode() {
		return hbox;
	}

}
