package poi.ui.image.loaded;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poi.ui.image.ImageData;
import poi.ui.image.edit.EditImageView;

public class LoadedImageView {
	
	private BorderPane borderPane;
	private FlowPane flowPane;
	
	public LoadedImageView() {
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
				LoadedImageNode loadedImage = new LoadedImageNode(editImageView.getImageData());
				loadedImage.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
					if (event.getClickCount() == 2) {
						openEditView(editImageView.getImageData(), loadedImage);
					}
				});
				flowPane.getChildren().add(loadedImage.getNode());
			} else {
				loadedImageNode.refresh();
			}
		}
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

}
