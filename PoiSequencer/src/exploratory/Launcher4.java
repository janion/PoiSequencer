package exploratory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import poi.ui.image.timeline.AdjustableImageNode;
import poi.ui.image.timeline.TimelineView;

public class Launcher4 extends Application {

	private List<TimelineView> timelines = new ArrayList<>();

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane borderPane = new BorderPane(new Pane());
		borderPane.setCenter(new VBox(constructScrollPane().getContent(), constructScrollPane()));

		Button minusBtn = new Button("-");
		Button plusBtn = new Button("+");
		minusBtn.setOnAction(
				event -> timelines.forEach(timeline -> timeline.setZoomLevel(timeline.getZoomLevel() / 1.2)));
		plusBtn.setOnAction(
				event -> timelines.forEach(timeline -> timeline.setZoomLevel(timeline.getZoomLevel() * 1.2)));
		borderPane.setTop(new HBox(minusBtn, plusBtn));

		Scene scene = new Scene(borderPane);

		stage.setScene(scene);
		stage.setWidth(200);
		stage.setHeight(200);
		stage.show();
	}

	private ScrollPane constructScrollPane() throws MalformedURLException, IOException {
		AdjustableImageNode img1 = new AdjustableImageNode(
				new Image(new File("src/exploratory/mini5.bmp").toURI().toURL().openStream()), 50);
		AdjustableImageNode img2 = new AdjustableImageNode(
				new Image(new File("src/exploratory/mini7.bmp").toURI().toURL().openStream()), 250);

		TimelineView view = new TimelineView();
		view.addImage(img1);
		view.addImage(img2);

		timelines.add(view);

		return view.getNode();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
