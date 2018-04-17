package poi.ui;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import poi.ui.image.ImageData;
import poi.ui.image.loaded.LoadedImageView;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;
import poi.ui.image.timeline.TimelineView;

public class HomeView {
	
	private SplitPane splitPane;
	private TimelineView timeline;
	
	public HomeView() {
		TimelineModel model = new TimelineModel();
		timeline = new TimelineView(model);
		
		LoadedImageView loadedImageView = new LoadedImageView();
		loadedImageView.getObserverManager().addObserver(LoadedImageView.REQUEST_ADD_TO_TIMELINE,
				imageData -> model.addImage(new ImageModel(10.0, new ImageData(imageData))));
		splitPane = new SplitPane(loadedImageView.getNode(), timeline.getNode());
		splitPane.setOrientation(Orientation.VERTICAL);
//		splitPane.setDividerPosition(0, 1.0);
	}
	
	public SplitPane getNode() {
		return splitPane;
	}

}
