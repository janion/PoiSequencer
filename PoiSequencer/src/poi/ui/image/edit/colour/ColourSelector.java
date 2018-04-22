package poi.ui.image.edit.colour;

import java.awt.MouseInfo;
import java.awt.Point;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import poi.ui.image.edit.DrawImageModel;

public class ColourSelector {
	
	private ColourPickerDialog dlg;
	private ColouredPane colourPane;
	private DrawImageModel drawImageModel;

	public ColourSelector(DrawImageModel drawImageModel) {
		this.drawImageModel = drawImageModel;
		colourPane = new ColouredPane();
		colourPane.getNode().setPrefSize(30, 30);
		colourPane.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> showColourPicker());
		
		drawImageModel.getObserverManager().addObserver(DrawImageModel.COLOUR_SELECTED, colourPane::setColour);
		
		colourPane.getNode().sceneProperty().addListener((observable, oldVal, newVal) -> {
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
			dlg = new ColourPickerDialog(drawImageModel::setSelectedColour);
			Stage dlgStage = dlg.getStage();
			dlgStage.initStyle(StageStyle.UNDECORATED);

			Bounds bounds = colourPane.getNode().localToScreen(colourPane.getNode().getBoundsInLocal());
			dlgStage.setX(bounds.getMinX());
			dlgStage.setY(bounds.getMinY());
			dlgStage.setOnHidden(closeEvent -> clearDialog());
			dlgStage.show();
			Point p = MouseInfo.getPointerInfo().getLocation();
			if (!bounds.contains(new Point2D(p.getX(), p.getY()))) {
				dlgStage.hide();
			}
		}
	}
	
	private void clearDialog() {
		dlg = null;
	}
	
	public Pane getNode() {
		return colourPane.getNode();
	}

}
