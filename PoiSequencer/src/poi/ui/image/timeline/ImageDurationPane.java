package poi.ui.image.timeline;

import java.util.OptionalDouble;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ImageDurationPane {
	
	private ImageModel imageModel;
	private VBox vbox;
	private Spinner<Double> spinner;
	
	public ImageDurationPane(ImageModel imageModel) {
		this.imageModel = imageModel;
		spinner = new Spinner<>(0.1, 999.9, imageModel.getDuration());
		spinner.setEditable(true);
		spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				spinner.increment(0); // won't change value, but will commit editor
			}
		});
		HBox spinnerHbox = new HBox(new Label("Duration /s:"), spinner);

		Button cancelButton = new Button ("Cancel");
		cancelButton.setOnAction(event -> close(false));
		Button okButton = new Button ("OK");
		okButton.setOnAction(event -> close(true));
		
		HBox buttonsHbox = new HBox(cancelButton, okButton);
		
		vbox = new VBox(new Label("Set image duration"), spinnerHbox, buttonsHbox);
		vbox.setPadding(new Insets(5));
		vbox.setSpacing(5);
	}
	
	private void close(boolean confirmed) {
		if (confirmed) {
			imageModel.setDuration(spinner.getValue());
		}
		vbox.getScene().getWindow().hide();
	}
	
	public VBox getNode() {
		return vbox;
	}

}
