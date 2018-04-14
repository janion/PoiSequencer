package poi.ui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		
		splitPane = new SplitPane(new LoadedImageView(model).getNode(), timeline.getNode());
		splitPane.setOrientation(Orientation.VERTICAL);
//		splitPane.setDividerPosition(0, 1.0);
		
		
		// TODO remove
		try {
			model.addImage(new ImageModel(50.0, new ImageData(ImageIO.read(new File("src/exploratory/mini5.bmp")))));
			model.addImage(new ImageModel(250.0, new ImageData(ImageIO.read(new File("src/exploratory/mini7.bmp")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SplitPane getNode() {
		return splitPane;
	}

}
