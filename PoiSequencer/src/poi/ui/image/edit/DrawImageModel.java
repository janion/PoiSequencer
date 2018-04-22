package poi.ui.image.edit;

import java.awt.image.BufferedImage;

import poi.observable.Observable;
import poi.observable.ObserverManager;
import poi.observable.ObserverManagerImpl;
import poi.observable.ObserverType;
import poi.ui.image.edit.colour.Colour;
import poi.ui.image.edit.undo.ColourChange;
import poi.ui.image.edit.undo.UndoFrame;
import poi.ui.image.edit.undo.UndoStack;
import poi.utility.ImageUtilities;
import poi.utility.Pair;

public class DrawImageModel implements Observable {
	
	public static final ObserverType<Colour> COLOUR_SELECTED = new ObserverType<>();
	public static final ObserverType<Pair<Integer, Integer>> PIXEL_COLOURED = new ObserverType<>();
	public static final ObserverType<DrawMode> DRAW_MODE = new ObserverType<>();
	
	private ObserverManager observerManager = new ObserverManagerImpl();
	
	private DrawMode drawMode;
	private Colour selectedColour;
	private BufferedImage image;

	private UndoStack undoStack;
	private UndoFrame currentFrame;
	
	public DrawImageModel(BufferedImage image) {
		this.image = ImageUtilities.copyImage(image);
		
		undoStack = new UndoStack();
		drawMode = DrawMode.PENCIL;
		selectedColour = new Colour(0, 0,  0);
	}

	public void setSelectedColour(Colour selectedColour) {
		this.selectedColour = selectedColour;
		observerManager.notifyObservers(COLOUR_SELECTED, selectedColour);
	}
	
	public void setPixelColour(int x, int y) {
		switch (drawMode) {
			case PENCIL:
				setSinglePixelColour(x, y, selectedColour);
				break;
			case PAINT_POT:
				fill(x, y);
				break;
		}
	}
	
	public void startPaint() {
		currentFrame = new UndoFrame();
	}
	
	public void endPaint() {
		undoStack.addFrame(currentFrame);
		currentFrame = null;
	}
	
	private void setSinglePixelColour(int x, int y, Colour colour) {
		boolean singlePixel = currentFrame == null;
		if (singlePixel) {
			currentFrame = new UndoFrame();
		}
		Colour initialColour = new Colour(image.getRGB(x, y));
		setSinglePixelColourWithoutUndoFrame(x, y, colour);
		currentFrame.addColourChange(new ColourChange(x, y, initialColour, colour));
		if (singlePixel) {
			undoStack.addFrame(currentFrame);
			currentFrame = null;
		}
	}
	
	private void setSinglePixelColourWithoutUndoFrame(int x, int y, Colour colour) {
		image.setRGB(x, y, colour.toInt());
		observerManager.notifyObservers(PIXEL_COLOURED, new Pair<>(x, y));
	}
	
	private void fill(int x, int y) {
		currentFrame = new UndoFrame();
		fill(x, y, new Colour(image.getRGB(x, y)));
		undoStack.addFrame(currentFrame);
		currentFrame = null;
	}
	
	private void fill(int x, int y, Colour initialColour) {
		if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
			return;
		}
		
		if (!initialColour.equals(selectedColour) && new Colour(image.getRGB(x, y)).equals(initialColour)) {
			setSinglePixelColour(x, y, selectedColour);
			fill(x + 1, y, initialColour);
			fill(x - 1, y, initialColour);
			fill(x, y + 1, initialColour);
			fill(x, y - 1, initialColour);
		}
	}

	public void undo() {
		UndoFrame frame = undoStack.undo();
		if (frame == null) {
			return;
		}
		for (ColourChange colourChange : frame.getColourChanges()) {
			setSinglePixelColourWithoutUndoFrame(colourChange.getX(), colourChange.getY(), colourChange.getOldColour());
		}
	}
	
	public void redo() {
		UndoFrame frame = undoStack.redo();
		if (frame == null) {
			return;
		}
		for (ColourChange colourChange : frame.getColourChanges()) {
			setSinglePixelColourWithoutUndoFrame(colourChange.getX(), colourChange.getY(), colourChange.getNewColour());
		}
	}
	
	public void setDrawMode(DrawMode drawMode) {
		this.drawMode = drawMode;
		observerManager.notifyObservers(DRAW_MODE, drawMode);
	}
	
	public Colour getSelectedColour() {
		return selectedColour;
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public Colour getColour(int x, int y) {
		return new Colour(image.getRGB(x, y));
	}
	
	public DrawMode getDrawMode() {
		return drawMode;
	}

	@Override
	public ObserverManager getObserverManager() {
		return observerManager;
	}

}
