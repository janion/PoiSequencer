package poi.ui.image.loaded;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poi.observable.Observable;
import poi.observable.ObserverManager;
import poi.observable.ObserverManagerImpl;
import poi.observable.ObserverType;
import poi.ui.image.ImageData;
import poi.ui.image.edit.EditImageView;

public class LoadedImageView implements Observable {
	
	public static final ObserverType<ImageData> REQUEST_ADD_TO_TIMELINE = new ObserverType<>();
	
	private LoadedImageModel imageModel;
	private BorderPane borderPane;
	private FlowPane flowPane;
	
	private Map<ImageData, LoadedImageNode> imageNodes;
	
	private ObserverManager observerManager = new ObserverManagerImpl();
	
	public LoadedImageView(LoadedImageModel imageModel) {
		this.imageModel = imageModel;
		
		imageNodes = new HashMap<>();
		
		imageModel.getObserverManager().addObserver(LoadedImageModel.IMAGE_ADDED, this::addImage);
		imageModel.getObserverManager().addObserver(LoadedImageModel.IMAGE_REMOVED, this::removeImage);
		imageModel.getObserverManager().addObserver(LoadedImageModel.CLEARED, nullValue -> clear());
		
		flowPane = new FlowPane();
		flowPane.setVgap(4);
		flowPane.setHgap(4);
		
		ScrollPane scrollPane = new ScrollPane(flowPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		borderPane = new BorderPane(scrollPane);
	}
	
	private void openEditView(ImageData imageData, LoadedImageNode loadedImageNode) {
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(borderPane.getScene().getWindow());
		EditImageView editImageView = new EditImageView(imageData);
		stage.setScene(new Scene(editImageView.getNode()));
		stage.showAndWait();
		
		if (editImageView.isSubmitted()) {
			loadedImageNode.refresh();
		}
	}
	
	private void addImage(ImageData image) {
		LoadedImageNode loadedImage = createLoadedImageNode(image);
		flowPane.getChildren().add(loadedImage.getNode());
		imageNodes.put(image, loadedImage);
	}
	
	private void removeImage(ImageData image) {
		LoadedImageNode loadedImage = imageNodes.remove(image);
		if (image != null) {
			flowPane.getChildren().remove(loadedImage.getNode());
		}
	}
	
	private void clear() {
		imageNodes.clear();
		flowPane.getChildren().clear();
	}
	
	private LoadedImageNode createLoadedImageNode(ImageData imageData) {
		LoadedImageNode loadedImage = new LoadedImageNode(imageData);
		ImageView node = loadedImage.getNode();
		
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getClickCount() == 2 && MouseButton.PRIMARY.equals(event.getButton())) {
				openEditView(imageData, loadedImage);
			}
		});

		loadedImage.getObserverManager().addObserver(LoadedImageNode.ADD_TO_TIMELINE,
				nullValue -> observerManager.notifyObservers(REQUEST_ADD_TO_TIMELINE, imageData));

		loadedImage.getObserverManager().addObserver(LoadedImageNode.REMOVE,
				nullValue -> imageModel.removeImage(imageData));
		
		return loadedImage;
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

	@Override
	public ObserverManager getObserverManager() {
		return observerManager;
	}

}
