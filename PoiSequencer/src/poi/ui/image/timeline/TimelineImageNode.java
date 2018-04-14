package poi.ui.image.timeline;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class TimelineImageNode extends AdjustableImageNode {
	
	private TimelineModel model;
	
	private ContextMenu contextMenu;

	public TimelineImageNode(ImageModel imageModel, TimelineModel model) {
		super(SwingFXUtils.toFXImage(imageModel.getImageData().getImage(), null), imageModel.getDuration());
		this.model = model;
		
		getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showContextMenu(event, imageModel));
	}
	
	private void showContextMenu(MouseEvent event, ImageModel imageModel) {
		if (MouseButton.SECONDARY.equals(event.getButton()) && contextMenu == null) {
			contextMenu = new ContextMenu();
			contextMenu.setOnHidden(hideEvent -> contextMenu = null);
			contextMenu.getItems().add(createRemoveItem(imageModel));
			
			contextMenu.show(getNode(), event.getScreenX(), event.getScreenY());
		}
	}
	
	private MenuItem createRemoveItem(ImageModel imageModel) {
		MenuItem removeItem = new MenuItem("Remove");
		removeItem.setOnAction(event -> {
			model.removeImage(imageModel);
		});
		
		return removeItem;
	}

}
