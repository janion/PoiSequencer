package poi.ui;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import poi.ui.image.ImageData;
import poi.ui.image.loaded.LoadedImageModel;
import poi.ui.image.loaded.LoadedImageView;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;
import poi.ui.image.timeline.TimelineView;

public class HomeView {
	
	private BorderPane borderPane;
	private CurrentProjectModel currentProjectModel;
	private LoadedImageModel loadedImageModel;
	private TimelineModel timelineModel;
	
	public HomeView() {
		currentProjectModel = new CurrentProjectModel();
		
		timelineModel = new TimelineModel();
		TimelineView timeline = new TimelineView(timelineModel);
		
		loadedImageModel = new LoadedImageModel();
		LoadedImageView loadedImageView = new LoadedImageView(loadedImageModel);
		loadedImageView.getObserverManager().addObserver(LoadedImageView.REQUEST_ADD_TO_TIMELINE,
				imageData -> timelineModel.addImage(new ImageModel(10.0, new ImageData(imageData))));
		SplitPane splitPane = new SplitPane(loadedImageView.getNode(), timeline.getNode());
		splitPane.setOrientation(Orientation.VERTICAL);
//		splitPane.setDividerPosition(0, 1.0);
		
		borderPane = new BorderPane(splitPane);
		borderPane.setTop(new HomeViewMenuBar(currentProjectModel, loadedImageModel, timelineModel).getMenuBar());
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

}
