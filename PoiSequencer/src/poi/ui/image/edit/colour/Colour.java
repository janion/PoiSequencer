package poi.ui.image.edit.colour;

public class Colour {

	private int r;
	private int g;
	private int b;
	
	public Colour(int rgb) {
		this((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
	}

	public Colour(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}
	
	public int toInt() {
		return (r << 16) + (g << 8) + b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
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
		Colour other = (Colour) obj;
		if (b != other.b) {
			return false;
		}
		if (g != other.g) {
			return false;
		}
		if (r != other.r) {
			return false;
		}
		return true;
	}
	
	

}
