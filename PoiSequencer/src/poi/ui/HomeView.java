package poi.ui;

import java.io.File;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import poi.ui.image.timeline.AdjustableImageNode;
import poi.ui.image.timeline.TimelineView;

public class HomeView {
	
	private SplitPane splitPane;
	private TimelineView timeline;
	
	public HomeView() {
		timeline = new TimelineView();
		
		splitPane = new SplitPane(new LoadedImageView().getNode(), timeline.getNode());
		splitPane.setOrientation(Orientation.VERTICAL);
//		splitPane.setDividerPosition(0, 1.0);
		
		
		// TODO remove
		AdjustableImageNode img1 = null;
		AdjustableImageNode img2 = null;
		try {
			img1 = new AdjustableImageNode(new Image(new File("src/exploratory/mini5.bmp").toURI().toURL().openStream()), 50);
			img2 = new AdjustableImageNode(new Image(new File("src/exploratory/mini7.bmp").toURI().toURL().openStream()), 250);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (img1 != null) {
			timeline.addImage(img1);
		}
		if (img2 != null) {
			timeline.addImage(img2);
		}
	}
	
	public SplitPane getNode() {
		return splitPane;
	}

}
