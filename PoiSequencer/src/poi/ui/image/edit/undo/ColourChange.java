package poi.ui.image.edit.undo;

import poi.ui.image.edit.colour.Colour;

public class ColourChange {

	private int x;
	private int y;
	private Colour oldColour;
	private Colour newColour;

	public ColourChange(int x, int y, Colour oldColour, Colour newColour) {
		this.x = x;
		this.y = y;
		this.oldColour = oldColour;
		this.newColour = newColour;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Colour getOldColour() {
		return oldColour;
	}

	public Colour getNewColour() {
		return newColour;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newColour == null) ? 0 : newColour.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ColourChange other = (ColourChange) obj;
		if (newColour == null) {
			if (other.newColour != null) {
				return false;
			}
		} else if (!newColour.equals(other.newColour)) {
			return false;
		}
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
			return false;
		}
		return true;
	}

}
