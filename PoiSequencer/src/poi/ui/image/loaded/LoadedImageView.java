package poi.ui.image.loaded;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poi.ui.image.ImageData;
import poi.ui.image.edit.EditImageView;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;

public class LoadedImageView {
	
	private TimelineModel model;
	private BorderPane borderPane;
	private FlowPane flowPane;
	
	public LoadedImageView(TimelineModel model) {
		this.model = model;
		flowPane = new FlowPane();
		flowPane.setVgap(4);
		flowPane.setHgap(4);
		
		ScrollPane scrollPane = new ScrollPane(flowPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		borderPane = new BorderPane(scrollPane);
		Button newButton = new Button("New");
		newButton.setOnAction(event -> openEditView(null, null));
		borderPane.setTop(new HBox(newButton, new Button("Load")));
	}
	
	private void openEditView(ImageData imageData, LoadedImageNode loadedImageNode) {
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(borderPane.getScene().getWindow());
		EditImageView editImageView = imageData == null ? new EditImageView() : new EditImageView(imageData);
		stage.setScene(new Scene(editImageView.getNode()));
		stage.showAndWait();
		
		if (editImageView.isSubmitted()) {
			if (loadedImageNode == null) {
				LoadedImageNode loadedImage = createLoadedImageNode(editImageView);
				flowPane.getChildren().add(loadedImage.getNode());
			} else {
				loadedImageNode.refresh();
			}
		}
	}
	
	private LoadedImageNode createLoadedImageNode(EditImageView editImageView) {
		LoadedImageNode loadedImage = new LoadedImageNode(editImageView.getImageData());
		ImageView node = loadedImage.getNode();
		
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getClickCount() == 2 && MouseButton.PRIMARY.equals(event.getButton())) {
				openEditView(editImageView.getImageData(), loadedImage);
			}
		});

		loadedImage.getObserverManager().addObserver(LoadedImageNode.ADD_TO_TIMELINE,
				imageData -> model.addImage(new ImageModel(10.0, imageData)));
		
		return loadedImage;
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

}
