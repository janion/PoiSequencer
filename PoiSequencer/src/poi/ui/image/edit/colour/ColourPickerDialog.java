package poi.ui.image.edit.colour;

import java.util.function.Consumer;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ColourPickerDialog {
	
	private static final int GRID_WIDTH = 4;
	
	private Stage stage;
	private ColouredPane chosenColourPane;
	private Consumer<Colour> action;
	
	public ColourPickerDialog(Consumer<Colour> action) {
		this.action = action;
		
		BorderPane borderPane = new BorderPane(createColourPicker());
		
		chosenColourPane = new ColouredPane();
		chosenColourPane.getNode().setPrefSize(3000, 30);
		
		borderPane.setBottom(new HBox(chosenColourPane.getNode()));
		
		Scene scene = new Scene(borderPane);
		scene.addEventHandler(MouseEvent.MOUSE_EXITED, event -> stage.hide());

		stage = new Stage();
		stage.setScene(scene);
		stage.setWidth(200);
		stage.setHeight(150);
	}
	
	private GridPane createColourPicker() {
		GridPane colourPane = new GridPane();
		
		for (int r = 0; r < 4; r++) {
			for (int g = 0; g < 4; g++) {
				for (int b = 0; b < 4; b++) {
					ColouredPane pane = new ColouredPane(new Colour(85 * r, 85 * g, 85 * b));
					pane.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, event -> chosenColourPane.setColour(pane.getColour()));
					pane.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> close());
					
					int count = b + (g << 2) + (r << 4);
					colourPane.add(pane.getNode(), count / GRID_WIDTH, count % GRID_WIDTH);
				}
			}
		}
		
		return colourPane;
	}
	
	private void close() {
		stage.hide();
		action.accept(chosenColourPane.getColour());
	}
	
	public Stage getStage() {
		return stage;
	}
	
}
