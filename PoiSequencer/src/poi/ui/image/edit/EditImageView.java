package poi.ui.image.edit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import poi.ui.image.ImageData;
import poi.ui.image.edit.colour.ColourSelector;

public class EditImageView {
	
	private static final int IMAGE_SIZE = 16;

	private BorderPane borderPane;
	private ImageData image;
	private DrawImageModel drawImageModel;
	private DrawImageView drawImageView;
	
	private boolean submitted = false;
	
	public EditImageView() {
		this(new ImageData(new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB)));
	}
	
	public EditImageView(ImageData image) {
		this.image = image;
		
		borderPane = new BorderPane();
		
		drawImageModel = new DrawImageModel(image.getImage());
		ColourSelector colourSelector = new ColourSelector(drawImageModel);
		colourSelector.getNode().setMaxSize(30, 30);
		
		ImageView selectedTool = new ImageView();
		updateImage(selectedTool, drawImageModel.getDrawMode());
		BorderPane pane = new BorderPane(selectedTool);
		pane.setPadding(new Insets(5));
		BorderPane.setAlignment(selectedTool, Pos.CENTER);
		borderPane.setRight(new VBox(colourSelector.getNode(), pane, createPencilButton(selectedTool),
				createPaintPotButton(selectedTool), new Pane()));
		
		drawImageView = new DrawImageView(drawImageModel);
		borderPane.setCenter(new ScrollPane(drawImageView.getNode()));
		
		// Don't like this. Would be better in DrawImageView where drag entered an exited events
		// add and remove pixels
//		Pane dragPane = new Pane();
//		dragPane.setMinSize(10, 10);
//		dragPane.setMaxSize(10, 10);
//		dragPane.setStyle("-fx-background-color: GREY; -fx-border-width: 1; -fx-border-color: BLACK");
//		BorderPane bp = new BorderPane(drawImageView.getNode());
//		bp.setRight(dragPane);
//		BorderPane.setAlignment(dragPane, Pos.CENTER_LEFT);
//		borderPane.setCenter(new ScrollPane(bp));
//		
//		dragPane.addEventHandler(MouseDragEvent.DRAG_DETECTED, event -> {
//			Bounds bounds = dragPane.localToScreen(dragPane.getBoundsInLocal());
//			double x = event.getScreenX();
//			double y = event.getScreenY();
//			if (y >= bounds.getMinY() && y < bounds.getMaxY()) {
//				if (x < bounds.getMinX()) {
//					System.out.println("Ensmallen");
//				} else {
//					System.out.println("Enbiggen");
//				}
//			}
//		});

		Button doneButton = new Button("\u2714");
		doneButton.setStyle("-fx-text-fill: GREEN; -fx-font-size: 20");
		Button cancelButton = new Button("\u2718");
		cancelButton.setStyle("-fx-text-fill: RED; -fx-font-size: 20");

		doneButton.setOnAction(event -> close(true));
		cancelButton.setOnAction(event -> close(false));

		Label coordinateLabel = new Label();
		drawImageView.getObserverManager().addObserver(DrawImageView.MOUSE_POSITION, coordinate -> coordinateLabel
				.setText(String.format("(%d, %d)", coordinate.getFirst(), coordinate.getSecond())));
		drawImageView.getObserverManager().addObserver(DrawImageView.MOUSE_LEFT,
				nullValue -> coordinateLabel.setText(""));

		Pane filler1 = new Pane();
		HBox.setHgrow(filler1, Priority.ALWAYS);
		Pane filler2 = new Pane();
		HBox.setHgrow(filler2, Priority.ALWAYS);
		
		HBox hbox = new HBox(doneButton, filler1, coordinateLabel, filler2, cancelButton);
		hbox.setAlignment(Pos.CENTER);
		borderPane.setBottom(hbox);
	}
	
	private Button createPencilButton(ImageView imageView) {
		Button button = new Button();
		button.setGraphic(new ImageView(pencilImage()));
		button.setOnAction(event -> drawImageModel.setDrawMode(DrawMode.PENCIL));

		drawImageModel.getObserverManager().addObserver(DrawImageModel.DRAW_MODE, mode -> updateImage(imageView, mode));
		return button;
	}
	
	private Button createPaintPotButton(ImageView imageView) {
		Button button = new Button();
		button.setGraphic(new ImageView(paintPotImage()));
		button.setOnAction(event -> drawImageModel.setDrawMode(DrawMode.PAINT_POT));

		drawImageModel.getObserverManager().addObserver(DrawImageModel.DRAW_MODE, mode -> updateImage(imageView, mode));
		return button;
	}
	
	private void updateImage(ImageView imageView, DrawMode drawMode) {
		switch (drawMode) {
			case PENCIL:
				imageView.setImage(pencilImage());
				break;
			case PAINT_POT:
				imageView.setImage(paintPotImage());
				break;
		}
	}
	
	private Image pencilImage() {
		try {
			return new Image(new FileInputStream(new File("pencil.png")));
		} catch (FileNotFoundException exptn) {
			exptn.printStackTrace();
			return null;
		}
	}
	
	private Image paintPotImage() {
		try {
			return new Image(new FileInputStream(new File("paintPot.png")));
		} catch (FileNotFoundException exptn) {
			exptn.printStackTrace();
			return null;
		}
	}
	
	private void close(boolean isSubmitted) {
		this.submitted = isSubmitted;
		if (isSubmitted) {
			image.setImage(drawImageModel.getImage());
		}
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
