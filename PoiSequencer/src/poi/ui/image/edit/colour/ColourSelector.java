package poi.ui.image.edit.colour;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ColourSelector {
	private ColourPickerDialog dlg;
	private ColouredPane colour;

	public ColourSelector() {
		colour = new ColouredPane();
		colour.getNode().setPrefSize(30, 30);
		colour.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> showColourPicker());
		
		colour.getNode().sceneProperty().addListener((observable, oldVal, newVal) -> {
			sceneSet(newVal);
		});
	}
	
	private void sceneSet(Scene scene) {
		if (scene != null) {
			Stage stage = (Stage) scene.getWindow();
			if (stage != null) {
				bindToStage(stage);
			} else {
				scene.windowProperty().addListener((observable, oldVal, newVal) -> {
					if (newVal != null) {
						bindToStage((Stage) newVal);
					}
				});
			}
		}
	}
	
	private void bindToStage(Stage stage) {
		stage.setOnHiding(event -> {
			if (dlg != null) {
				dlg.getStage().hide();
			}
		});
	}
	
	private void showColourPicker() {
		if (dlg == null) {
			ColourPickerDialog dlg = new ColourPickerDialog(colour::setColour);
			Stage dlgStage = dlg.getStage();
			dlgStage.initStyle(StageStyle.UNDECORATED);

			Bounds bounds = colour.getNode().localToScreen(colour.getNode().getBoundsInLocal());
			dlgStage.setX(bounds.getMinX());
			dlgStage.setY(bounds.getMinY());
			dlgStage.setOnHidden(closeEvent -> clearDialog());
			dlgStage.show();
		}
	}
	
	private void clearDialog() {
		dlg = null;
	}
	
	public Pane getNode() {
		return colour.getNode();
	}
	
	public Colour getColour() {
		return colour.getColour();
	}

}
