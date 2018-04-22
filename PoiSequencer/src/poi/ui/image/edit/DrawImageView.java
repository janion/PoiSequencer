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
import poi.ui.image.ImageData;
import poi.ui.image.edit.colour.Colour;
import poi.ui.image.edit.colour.ColourSelector;
import poi.ui.image.edit.colour.ColouredPane;
import poi.ui.image.edit.undo.ColourChange;
import poi.ui.image.edit.undo.UndoFrame;
import poi.ui.image.edit.undo.UndoStack;
import poi.utility.ImageUtilities;
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
	
	private BufferedImage image;
	private GridPane gridPane;
	private ColourSelector colourSelector;
	
	private UndoStack undoStack;
	private UndoFrame currentFrame;
	private Map<Pair<Integer, Integer>, ColouredPane> pixels;
	
	public DrawImageView(ImageData imageData, ColourSelector colourSelector) {
		this.image = ImageUtilities.copyImage(imageData.getImage());
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
				ColouredPane colouredPane = new ColouredPane(new Colour(red, green, blue));
				Pane pane = colouredPane.getNode();
				gridPane.add(new VBox(pane, new Pane()), x, y);
				
				IndexedPane indexedPane = new IndexedPane(colouredPane, x, y);

				pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> singlePixelChange(indexedPane));
				pane.addEventHandler(MouseDragEvent.DRAG_DETECTED, event -> startPaint(indexedPane));
				pane.addEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, event -> changePixelColour(indexedPane));
				pane.addEventHandler(MouseDragEvent.MOUSE_DRAG_RELEASED, event -> endPaint());
				
				pixels.put(new Pair<>(x, y), colouredPane);
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
		if (KeyCode.Z.equals(event.getCode()) && event.isControlDown() && currentFrame == null) {
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
		if (KeyCode.Y.equals(event.getCode()) && event.isControlDown() && currentFrame == null) {
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
	
	private void fill(IndexedPane pane) {
		currentFrame = new UndoFrame();
		fill(pane.getX(), pane.getY(), pane.getPane().getColour());
		undoStack.addFrame(currentFrame);
		currentFrame = null;
	}
	
	private void fill(int x, int y, Colour initialColour) {
		ColouredPane startPixel = pixels.get(new Pair<>(x, y));
		if (startPixel == null) {
			return;
		}
		
		Colour newColour = colourSelector.getColour();
		
		if (!initialColour.equals(newColour) && startPixel.getColour().equals(initialColour)) {
			startPixel.setColour(newColour);
			currentFrame.addColourChange(new ColourChange(x, y, initialColour, newColour));
			fill(x + 1, y, initialColour);
			fill(x - 1, y, initialColour);
			fill(x, y + 1, initialColour);
			fill(x, y - 1, initialColour);
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public GridPane getNode() {
		return gridPane;
	}

}
