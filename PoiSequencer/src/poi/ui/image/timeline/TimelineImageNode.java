package poi.ui.image.timeline;

import java.util.OptionalDouble;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TimelineImageNode extends AdjustableImageNode {
	
	private ImageModel imageModel;
	private TimelineModel timelineModel;
	
	private ContextMenu contextMenu;

	public TimelineImageNode(ImageModel imageModel, TimelineModel timelineModel) {
		super(SwingFXUtils.toFXImage(imageModel.getImageData().getImage(), null), imageModel.getDuration());
		this.imageModel = imageModel;
		this.timelineModel = timelineModel;
		
		getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showContextMenu(event, imageModel));
	}
	
	private void showContextMenu(MouseEvent event, ImageModel imageModel) {
		if (MouseButton.SECONDARY.equals(event.getButton()) && contextMenu == null) {
			contextMenu = new ContextMenu();
			contextMenu.setOnHidden(hideEvent -> contextMenu = null);
			contextMenu.getItems().add(createRemoveItem(imageModel));
			contextMenu.getItems().add(createChangeDurationItem(imageModel));
			
			contextMenu.show(getNode(), event.getScreenX(), event.getScreenY());
		}
	}
	
	private MenuItem createRemoveItem(ImageModel imageModel) {
		MenuItem removeItem = new MenuItem("Remove");
		removeItem.setOnAction(event -> {
			timelineModel.removeImage(imageModel);
		});
		
		return removeItem;
	}
	
	private MenuItem createChangeDurationItem(ImageModel imageModel) {
		MenuItem removeItem = new MenuItem("Change duration");
		removeItem.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(getNode().getScene().getWindow());
			stage.initStyle(StageStyle.UNDECORATED);
			ImageDurationPane pane = new ImageDurationPane(imageModel);
			Scene scene = new Scene(pane.getNode());
			stage.setScene(scene);
			stage.show();
		});
		
		return removeItem;
	}
	
	public double getDuration() {
		return imageModel.getDuration();
	}

}
