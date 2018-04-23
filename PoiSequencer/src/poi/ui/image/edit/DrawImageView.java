package poi.ui.image.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import poi.ui.image.edit.colour.Colour;
import poi.ui.image.edit.colour.ColouredPane;
import poi.utility.Pair;

public class DrawImageView {
	
	private class IndexedPane {
		
		private ColouredPane pane;
		private int x;
		private int y;
		
		public IndexedPane(ColouredPane pane, int x, int y) {
			this.pane = pane;
			this.x = x;
			this.y = y;
		}

		public ColouredPane getPane() {
			return pane;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
	
	private GridPane gridPane;
	private DrawImageModel drawImageModel;
	
	private Map<Pair<Integer, Integer>, ColouredPane> pixels;
	
	public DrawImageView(DrawImageModel drawImageModel) {
		this.drawImageModel = drawImageModel;
		
		gridPane = new GridPane();
		pixels = new HashMap<>();
		
		for (int x = 0; x < drawImageModel.getImage().getWidth(); x++) {
			for (int y = 0; y < drawImageModel.getImage().getHeight(); y++) {
				ColouredPane colouredPane = new ColouredPane(new Colour(drawImageModel.getImage().getRGB(x, y)));
				Pane pane = colouredPane.getNode();
				IndexedPane indexedPane = new IndexedPane(colouredPane, x, y);
				drawImageModel.getObserverManager().addObserver(DrawImageModel.PIXEL_COLOURED,
						coordinates -> changePixelColour(coordinates.getFirst(), coordinates.getSecond(), indexedPane));
				gridPane.add(new VBox(pane, new Pane()), x, y);
				
				final int X = x;
				final int Y = y;

				pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> drawImageModel.setPixelColour(X, Y));
				pane.addEventHandler(MouseDragEvent.DRAG_DETECTED, event -> startPaint(indexedPane));
				pane.addEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, event -> drawImageModel.setPixelColour(X, Y));
				pane.addEventHandler(MouseDragEvent.MOUSE_DRAG_RELEASED, event -> drawImageModel.endPaint());
				
				pixels.put(new Pair<>(x, y), colouredPane);
			}
		}

		drawImageModel.getObserverManager().addObserver(DrawImageModel.DRAW_MODE, this::updateCursor);
		updateCursor(drawImageModel.getDrawMode());
		
		setupUndoRedo();
	}
	
	private void updateCursor(DrawMode drawMode) {
		switch (drawMode) {
			case PENCIL:
				setCursor("pencilCursor2.png", 0, 11);
				break;
			case PAINT_POT:
				setCursor("paintPotCursor2.png", 1, 11);
				break;
		}
	}
	
	private void setCursor(String imageFile, int x, int y) {
		try {
			Image img = new Image(new FileInputStream(new File(imageFile)));
			gridPane.setCursor(new ImageCursor(img, x, y));
		} catch (FileNotFoundException exptn) {
			exptn.printStackTrace();
		}
	}
	
	private void setupUndoRedo() {
		gridPane.sceneProperty().addListener((observable, oldVal, newVal) -> {
			sceneSet(newVal);
		});
	}
	
	private void sceneSet(Scene scene) {
		if (scene != null) {
			scene.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
		}
	}
	
	private void keyPressed(KeyEvent event) {
		if (event.isControlDown()) {
			if (KeyCode.Z.equals(event.getCode())) {
				drawImageModel.undo();
			} else if (KeyCode.Y.equals(event.getCode())) {
				drawImageModel.redo();
			}
		}
	}
	
	private void startPaint(IndexedPane pane) {
		pane.getPane().getNode().startFullDrag();
		drawImageModel.startPaint();
		drawImageModel.setPixelColour(pane.getX(), pane.getY());
	}
	
	private void changePixelColour(int x, int y, IndexedPane indexedPane) {
		if (x == indexedPane.getX() && y == indexedPane.getY()) {
			indexedPane.getPane().setColour(drawImageModel.getColour(x, y));
		}
	}
	
	public DrawImageModel getDrawImageModel() {
		return drawImageModel;
	}
	
	public GridPane getNode() {
		return gridPane;
	}

}
