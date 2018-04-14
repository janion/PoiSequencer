package poi.ui.image.loaded;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import poi.observable.Observable;
import poi.observable.ObserverManager;
import poi.observable.ObserverManagerImpl;
import poi.observable.ObserverType;
import poi.ui.image.ImageData;
import poi.utility.ImageUtilities;

public class LoadedImageNode implements Observable {
	
	public static final ObserverType<ImageData> ADD_TO_TIMELINE = new ObserverType<>();
	
	private static final int SCALE_FACTOR = 4;
	
	private ImageData imageData;
	private ImageView imageView;
	
	private ContextMenu contextMenu;
	
	private ObserverManager observerManager = new ObserverManagerImpl();
	
	public LoadedImageNode(ImageData imageData) {
		this.imageData = imageData;
		
		BufferedImage img = imageData.getImage();
		imageView = new ImageView(ImageUtilities.resample(img, SCALE_FACTOR));

		
		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::showContextMenu);
	}
	
	private void showContextMenu(MouseEvent event) {
		if (MouseButton.SECONDARY.equals(event.getButton())) {
			if (contextMenu != null) {
				contextMenu.hide();
			}
			contextMenu = new ContextMenu();
			contextMenu.getItems().add(createSaveItem());
			contextMenu.getItems().add(createAddToTimelineItem());
			
			contextMenu.setOnHidden(hideEvent -> contextMenu = null);
			contextMenu.show(imageView, event.getScreenX(), event.getScreenY());
		}
	}
	
	private MenuItem createSaveItem() {
		MenuItem saveItem = new MenuItem("Save");
		saveItem.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Bitmap Files", "*.bmp"));
			File file = fileChooser.showSaveDialog(imageView.getScene().getWindow());
			if (file != null) {
				try {
					ImageIO.write(imageData.getImage(), "bmp", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return saveItem;
	}
	
	private MenuItem createAddToTimelineItem() {
		MenuItem timelineItem = new MenuItem("Add to timeline");
		timelineItem.setOnAction(event -> observerManager.notifyObservers(ADD_TO_TIMELINE, imageData));
		
		return timelineItem;
	}
	
	public void refresh() {
		BufferedImage img = imageData.getImage();
		imageView.setImage(ImageUtilities.resample(img, SCALE_FACTOR));
	}
	
	public ImageView getNode() {
		return imageView;
	}

	@Override
	public ObserverManager getObserverManager() {
		return observerManager;
	}

}
