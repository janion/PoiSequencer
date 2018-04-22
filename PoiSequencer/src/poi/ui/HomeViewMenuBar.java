package poi.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poi.ui.image.ImageData;
import poi.ui.image.edit.EditImageView;
import poi.ui.image.loaded.LoadedImageModel;
import poi.ui.image.timeline.TimelineModel;
import poi.utility.ImageUtilities;
import poi.utility.ProjectUtilities;

public class HomeViewMenuBar {
	
	private CurrentProjectModel currentProjectModel;
	private LoadedImageModel loadedImageModel;
	private TimelineModel timelineModel;
	
	private MenuBar menuBar;
	
	public HomeViewMenuBar(CurrentProjectModel currentProjectModel, LoadedImageModel loadedImageModel,
			TimelineModel timelineModel) {
		this.currentProjectModel = currentProjectModel;
		this.loadedImageModel = loadedImageModel;
		this.timelineModel = timelineModel;
		
		createMenuBar();
	}
	
	private void createMenuBar() {
		menuBar = new MenuBar();
		menuBar.getMenus().addAll(createFileMenu(), createImageMenu());
	}
	
	private Menu createFileMenu() {
		MenuItem saveProjectItem = new MenuItem("Save Project");
		saveProjectItem.setOnAction(event -> saveProject());
		MenuItem openProjectItem = new MenuItem("Open Project");
		openProjectItem.setOnAction(event -> loadProject());
		MenuItem newProjectItem = new MenuItem("New Project");
		newProjectItem.setOnAction(event -> newProject());
		MenuItem exportItem = new MenuItem("Export Timeline");
		MenuItem quitItem = new MenuItem("Quit");
		quitItem.setOnAction(event -> menuBar.getScene().getWindow().hide());
		
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().addAll(saveProjectItem, openProjectItem, newProjectItem, exportItem, quitItem);
		
		return fileMenu;
	}
	
	private Menu createImageMenu() {
		MenuItem newImageItem = new MenuItem("New");
		newImageItem.setOnAction(event -> openEditView());
		MenuItem loadImageItem = new MenuItem("Load");
		loadImageItem.setOnAction(event -> loadImage());
		
		Menu imageMenu = new Menu("Image");
		imageMenu.getItems().addAll(newImageItem, loadImageItem);
		
		return imageMenu;
	}
	
	private void openEditView() {
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(menuBar.getScene().getWindow());
		EditImageView editImageView = new EditImageView();
		stage.setScene(new Scene(editImageView.getNode()));
		stage.showAndWait();
		
		if (editImageView.isSubmitted()) {
			loadedImageModel.addImage(editImageView.getImageData());
		}
	}
	
	private void loadImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Bitmap Files", "*.bmp"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JPEG Files", "*.jpg"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
		File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
		
		if (file != null) {
			try {
				BufferedImage img = ImageUtilities.compressImageTo6BitColourPalette(ImageIO.read(file), 16);
				ImageData imageData = new ImageData(img, file.toURI().toURL());
				loadedImageModel.addImage(imageData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		
		File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
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
		
		File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
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
	
	public MenuBar getMenuBar() {
		return menuBar;
	}

}
