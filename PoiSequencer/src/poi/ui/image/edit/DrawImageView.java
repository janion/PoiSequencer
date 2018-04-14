package poi.ui.image.edit;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import poi.ui.image.edit.colour.Colour;
import poi.ui.image.edit.colour.ColourSelector;
import poi.ui.image.edit.colour.ColouredPane;
import poi.ui.image.edit.undo.ColourChange;
import poi.ui.image.edit.undo.UndoFrame;
import poi.ui.image.edit.undo.UndoStack;

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
	
	private BufferedImage image;
	private GridPane gridPane;
	private ColourSelector colourSelector;
	
	private UndoStack undoStack;
	private UndoFrame currentFrame;
	private Map<Pair<Integer, Integer>, ColouredPane> pixels;
	
	public DrawImageView(BufferedImage image, ColourSelector colourSelector) {
		this.image = image;
		this.colourSelector = colourSelector;
		undoStack = new UndoStack();
		
		gridPane = new GridPane();
		pixels = new HashMap<>();
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;
				ColouredPane pane = new ColouredPane(new Colour(red, green, blue));
				gridPane.add(new VBox(pane.getNode(), new Pane()), x, y);
				
				IndexedPane indexedPane = new IndexedPane(pane, x, y);

				pane.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> singlePixelChange(indexedPane));
				pane.getNode().addEventHandler(MouseDragEvent.DRAG_DETECTED, event -> startPaint(indexedPane));
				pane.getNode().addEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, event -> changePixelColour(indexedPane));
				pane.getNode().addEventHandler(MouseDragEvent.MOUSE_DRAG_RELEASED, event -> endPaint());
				
				pixels.put(new Pair<>(x, y), pane);
			}
		}
		
		setupUndoRedo();
	}
	
	private void setupUndoRedo() {
		gridPane.sceneProperty().addListener((observable, oldVal, newVal) -> {
			sceneSet(newVal);
		});
	}
	
	private void sceneSet(Scene scene) {
		if (scene != null) {
			scene.addEventHandler(KeyEvent.KEY_PRESSED, this::undo);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, this::redo);
		}
	}
	
	private void undo(KeyEvent event) {
		if (KeyCode.Z.equals(event.getCode()) && event.isControlDown()) {
			UndoFrame frame = undoStack.undo();
			if (frame == null) {
				return;
			}
			for (ColourChange colourChange : frame.getColourChanges()) {
				ColouredPane pane = pixels.get(new Pair<>(colourChange.getX(), colourChange.getY()));
				Colour oldColour = colourChange.getOldColour();
				pane.setColour(oldColour);
				image.setRGB(colourChange.getX(), colourChange.getY(), (oldColour.getR() << 16) + (oldColour.getG() << 8) + oldColour.getB());
			}
		}
	}
	
	private void redo(KeyEvent event) {
		if (KeyCode.Y.equals(event.getCode()) && event.isControlDown()) {
			UndoFrame frame = undoStack.redo();
			if (frame == null) {
				return;
			}
			for (ColourChange colourChange : frame.getColourChanges()) {
				ColouredPane pane = pixels.get(new Pair<>(colourChange.getX(), colourChange.getY()));
				Colour newColour = colourChange.getNewColour();
				pane.setColour(newColour);
				image.setRGB(colourChange.getX(), colourChange.getY(), (newColour.getR() << 16) + (newColour.getG() << 8) + newColour.getB());
			}
		}
	}
	
	private void startPaint(IndexedPane pane) {
		currentFrame = new UndoFrame();
		pane.getPane().getNode().startFullDrag();
		changePixelColour(pane);
	}
	
	private void endPaint() {
		undoStack.addFrame(currentFrame);
		currentFrame = null;
	}
	
	private void singlePixelChange(IndexedPane pane) {
		currentFrame = new UndoFrame();
		changePixelColour(pane);
		undoStack.addFrame(currentFrame);
		currentFrame = null;
	}
	
	private void changePixelColour(IndexedPane pane) {
		Colour newColour = colourSelector.getColour();
		int x = pane.getX();
		int y = pane.getY();
		
		int rgb = image.getRGB(x, y);
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		currentFrame.addColourChange(new ColourChange(x, y, new Colour(r, g, b), newColour));
		
		pane.getPane().setColour(newColour);
		image.setRGB(x, y, (newColour.getR() << 16) + (newColour.getG() << 8) + newColour.getB());
	}
	
	public GridPane getNode() {
		return gridPane;
	}

}
