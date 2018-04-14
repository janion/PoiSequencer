package poi.ui.image.edit;

import java.awt.image.BufferedImage;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import poi.ui.image.ImageData;
import poi.ui.image.edit.colour.ColourSelector;

public class EditImageView {
	
	private static final int IMAGE_SIZE = 16;
	
	private BorderPane borderPane;
	private ImageData image;
	
	private boolean submitted = false;
	
	public EditImageView() {
		this(new ImageData(new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB)));
	}
	
	public EditImageView(ImageData image) {
		this.image = image;
		
		borderPane = new BorderPane();
		
		ColourSelector colourSelector = new ColourSelector();
		colourSelector.getNode().setMaxSize(30, 30);
		borderPane.setRight(colourSelector.getNode());
		
		borderPane.setCenter(new ScrollPane(new DrawImageView(image, colourSelector).getNode()));
		

		Button doneButton = new Button("\u2714");
		doneButton.setStyle("-fx-text-fill: GREEN; -fx-font-size: 20");
		Button cancelButton = new Button("\u2718");
		cancelButton.setStyle("-fx-text-fill: RED; -fx-font-size: 20");

		doneButton.setOnAction(event -> close(true));
		cancelButton.setOnAction(event -> close(false));
		
		Pane filler = new Pane();
		HBox.setHgrow(filler, Priority.ALWAYS);
		borderPane.setBottom(new HBox(doneButton, filler, cancelButton));
	}
	
	private void close(boolean isSubmitted) {
		this.submitted = isSubmitted;
		borderPane.getScene().getWindow().hide();
	}
	
	public BorderPane getNode() {
		return borderPane;
	}
	
	public boolean isSubmitted() {
		return submitted;
	}
	
	public ImageData getImageData() {
		return image;
	}

}
