package poi.ui.image.loaded;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poi.ui.image.ImageData;
import poi.ui.image.edit.EditImageView;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;
import poi.utility.ImageUtilities;

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
		
		Button loadButton = new Button("Load");
		loadButton.setOnAction(event -> loadImage());
		
		borderPane.setTop(new HBox(newButton,loadButton));
	}
	
	private void loadImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Bitmap Files", "*.bmp"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JPEG Files", "*.jpg"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
		File file = fileChooser.showOpenDialog(borderPane.getScene().getWindow());
		
		if (file != null) {
			try {
				BufferedImage img = ImageUtilities.compressImageTo6BitColourPalette(ImageIO.read(file), 16);
				ImageData imageData = new ImageData(img, file.toURI().toURL());
				LoadedImageNode loadedImage = createLoadedImageNode(imageData);
				flowPane.getChildren().add(loadedImage.getNode());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
				LoadedImageNode loadedImage = createLoadedImageNode(editImageView.getImageData());
				flowPane.getChildren().add(loadedImage.getNode());
			} else {
				loadedImageNode.refresh();
			}
		}
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
				nullValue -> model.addImage(new ImageModel(10.0, imageData)));

		loadedImage.getObserverManager().addObserver(LoadedImageNode.REMOVE,
				nullValue -> flowPane.getChildren().remove(node));
		
		return loadedImage;
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

}
