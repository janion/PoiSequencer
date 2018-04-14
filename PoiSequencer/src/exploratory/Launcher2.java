package exploratory;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import poi.ui.image.timeline.AdjustableImageNode;

public class Launcher2 extends Application {

   @Override
   public void start(Stage stage) throws Exception {
		BorderPane borderPane = new BorderPane(new Pane());

		AdjustableImageNode img1 = new AdjustableImageNode(new Image(new File("src/exploratory/mini5.bmp").toURI().toURL().openStream()), 50);
		AdjustableImageNode img2 = new AdjustableImageNode(new Image(new File("src/exploratory/mini7.bmp").toURI().toURL().openStream()), 250);
		
		HBox hbox = new HBox(img1.getNode(), img2.getNode());
		
		ScrollPane scrollPane = new ScrollPane(hbox);
		scrollPane.setPrefHeight(200);
		scrollPane.setFitToHeight(true);
		borderPane.setCenter(scrollPane);
		
		Button minusBtn = new Button("-");
		Button plusBtn = new Button("+");
		minusBtn.setOnAction(event -> {
			img1.setWidth(img1.getWidth() / 1.2);
			img2.setWidth(img2.getWidth() / 1.2);
		});
		plusBtn.setOnAction(event -> {
			img1.setWidth(img1.getWidth() * 1.2);
			img2.setWidth(img2.getWidth() * 1.2);
		});
		borderPane.setTop(new HBox(minusBtn, plusBtn));

		Scene scene = new Scene(borderPane);

		stage.setScene(scene);
		stage.setWidth(200);
		stage.setHeight(200);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
