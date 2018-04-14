package exploratory;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import poi.ui.image.edit.colour.ColourPickerDialog;
import poi.ui.image.edit.colour.ColouredPane;

public class Launcher6 extends Application {
	
	private ColourPickerDialog dlg;
	private ColouredPane colour;

	@Override
	public void start(Stage stage) throws Exception {
		colour = new ColouredPane();
		colour.getNode().setPrefSize(30, 30);
		colour.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> showColourPicker());
		Scene scene = new Scene(colour.getNode());

		stage.setScene(scene);
		stage.setWidth(200);
		stage.setHeight(200);
		stage.show();
		
		stage.setOnHiding(event -> clearDialog());
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
		if (dlg != null) {
			dlg.getStage().hide();
		}
		dlg = null;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
