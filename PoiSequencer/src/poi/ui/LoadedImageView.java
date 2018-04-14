package poi.ui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poi.ui.image.edit.EditImageView;

public class LoadedImageView {
	
	private BorderPane borderPane;
	private FlowPane flowPane;
	
	public LoadedImageView() {
		flowPane = new FlowPane();
		
		ScrollPane scrollPane = new ScrollPane(flowPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		borderPane = new BorderPane(scrollPane);
		Button newButton = new Button("New");
		newButton.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(borderPane.getScene().getWindow());
			EditImageView editImageView = new EditImageView();
			stage.setScene(new Scene(editImageView.getNode()));
			stage.showAndWait();
			
			if (editImageView.isSubmitted()) {
				flowPane.getChildren().add(new ImageView(SwingFXUtils.toFXImage(editImageView.getImage(), null)));
			}
		});
		borderPane.setTop(new HBox(newButton, new Button("Load")));
		
		// TODO remove
		for (int i = 0; i < 40; i++) {
			flowPane.getChildren().add(new Button(String.valueOf(i)));
		}
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

}
