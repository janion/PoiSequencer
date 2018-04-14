package poi.ui.image.edit.undo;

import java.util.ArrayList;
import java.util.List;

public class UndoStack {
	
	private List<UndoFrame> frames;
	private int index;
	
	public UndoStack() {
		frames = new ArrayList<>();
		index = -1;
	}
	
	public void addFrame(UndoFrame frame) {
		while(frames.size() > index + 1 && index > -1) {
			frames.remove(index + 1);
		}
		index = frames.size();
		frames.add(frame);
	}
	
	public UndoFrame undo() {
		if (index == -1) {
			return null;
		}
		
		return frames.get(index--);
	}
	
	public UndoFrame redo() {
		if (index == frames.size() - 1) {
			return null;
		}
		
		return frames.get(++index);
	}

}
