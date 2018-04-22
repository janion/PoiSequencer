package exploratory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import poi.ui.HomeView;

public class Launcher extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		HomeView homeView = new HomeView();
		Scene scene = new Scene(homeView.getNode());

		stage.setScene(scene);
		stage.setWidth(700);
		stage.setHeight(500);
		stage.show();
		
		homeView.setInitialDividerPosition();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
