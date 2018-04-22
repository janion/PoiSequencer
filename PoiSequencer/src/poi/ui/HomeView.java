package poi.ui;

import java.io.File;
import java.io.IOException;

import javafx.geometry.Orientation;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import poi.ui.image.ImageData;
import poi.ui.image.loaded.LoadedImageModel;
import poi.ui.image.loaded.LoadedImageView;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;
import poi.ui.image.timeline.TimelineView;
import poi.utility.ProjectUtilities;

public class HomeView {
	
	private BorderPane borderPane;
	private CurrentProjectModel currentProjectModel;
	private LoadedImageModel loadedImageModel;
	private TimelineModel timelineModel;
	
	public HomeView() {
		currentProjectModel = new CurrentProjectModel();
		
		timelineModel = new TimelineModel();
		TimelineView timeline = new TimelineView(timelineModel);
		
		loadedImageModel = new LoadedImageModel();
		LoadedImageView loadedImageView = new LoadedImageView(loadedImageModel);
		loadedImageView.getObserverManager().addObserver(LoadedImageView.REQUEST_ADD_TO_TIMELINE,
				imageData -> timelineModel.addImage(new ImageModel(10.0, new ImageData(imageData))));
		SplitPane splitPane = new SplitPane(loadedImageView.getNode(), timeline.getNode());
		splitPane.setOrientation(Orientation.VERTICAL);
//		splitPane.setDividerPosition(0, 1.0);
		
		borderPane = new BorderPane(splitPane);
		borderPane.setTop(createMenuBar());
	}
	
	private MenuBar createMenuBar() {
		MenuItem saveItem = new MenuItem("Save Project");
		saveItem.setOnAction(event -> saveProject());
		MenuItem openItem = new MenuItem("Open Project");
		openItem.setOnAction(event -> loadProject());
		MenuItem newItem = new MenuItem("New Project");
		newItem.setOnAction(event -> newProject());
		MenuItem exportItem = new MenuItem("Export Timeline");
		MenuItem quitItem = new MenuItem("Quit");
		quitItem.setOnAction(event -> borderPane.getScene().getWindow().hide());
		
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().addAll(saveItem, openItem, newItem, exportItem, quitItem);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(fileMenu);
		
		return menuBar;
	}
	
	private void saveProject() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Poi Project Files", "*.pp"));

		try {
			if (currentProjectModel.getCurrentProject() != null) {
				File currentModel = new File(currentProjectModel.getCurrentProject().toURI());
				fileChooser.setInitialDirectory(currentModel.getParentFile());
				fileChooser.setInitialFileName(currentModel.getName());
			} else if (currentProjectModel.getLastProject() != null) {
				File lastModel = new File(currentProjectModel.getLastProject().toURI());
				fileChooser.setInitialDirectory(lastModel.getParentFile());
			}
		} catch (Exception exptn) {
			exptn.printStackTrace();
		}
		
		File file = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
		if (file != null) {
			try {
				ProjectUtilities.saveProject(loadedImageModel, timelineModel, file);
				currentProjectModel.setCurrentProject(file.toURI().toURL());
			} catch (IOException exptn) {
				exptn.printStackTrace();
			}
		}
	}
	
	private void loadProject() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Poi Project Files", "*.pp"));

		try {
			if (currentProjectModel.getCurrentProject() != null) {
				File currentModel = new File(currentProjectModel.getCurrentProject().toURI());
				fileChooser.setInitialDirectory(currentModel.getParentFile());
				fileChooser.setInitialFileName(currentModel.getName());
			} else if (currentProjectModel.getLastProject() != null) {
				File lastModel = new File(currentProjectModel.getLastProject().toURI());
				fileChooser.setInitialDirectory(lastModel.getParentFile());
			}
		} catch (Exception exptn) {
			exptn.printStackTrace();
		}
		
		File file = fileChooser.showOpenDialog(borderPane.getScene().getWindow());
		if (file != null) {
			try {
				ProjectUtilities.loadProject(loadedImageModel, timelineModel, file);
				currentProjectModel.setCurrentProject(file.toURI().toURL());
			} catch (IOException exptn) {
				exptn.printStackTrace();
			}
		}
	}
	
	private void newProject() {
		loadedImageModel.clear();
		timelineModel.clear();
		currentProjectModel.setCurrentProject(null);
	}
	
	public BorderPane getNode() {
		return borderPane;
	}

}
