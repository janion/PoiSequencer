package exploratory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;
import poi.ui.image.timeline.scale.TimeScaleRuler;

public class ScaleLauncher extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		TimelineModel timelineModel = new TimelineModel();

		BorderPane borderPane = new BorderPane(new TimeScaleRuler(timelineModel, 1).getNode());
		borderPane.setBottom(new TimeScaleRuler(timelineModel, 1).getNode());
		Scene scene = new Scene(borderPane);

		timelineModel.addImage(new ImageModel(10.0, null));
		timelineModel.addImage(new ImageModel(50.0, null));

		stage.setScene(scene);
		stage.setWidth(200);
		stage.setHeight(200);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
