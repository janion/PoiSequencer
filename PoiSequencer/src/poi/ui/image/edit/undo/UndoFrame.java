package poi.ui.image.edit.undo;

import java.util.HashSet;
import java.util.Set;

public class UndoFrame {
	
	private Set<ColourChange> colourChanges;

	public UndoFrame() {
		this(new HashSet<>());
	}

	public UndoFrame(Set<ColourChange> colourChanges) {
		this.colourChanges = colourChanges;
	}
	
	public void addColourChange(ColourChange colourChange) {
		if (!colourChanges.contains(colourChange)) {
			colourChanges.add(colourChange);
		}
	}
	
	public Set<ColourChange> getColourChanges() {
		return colourChanges;
	}

}
