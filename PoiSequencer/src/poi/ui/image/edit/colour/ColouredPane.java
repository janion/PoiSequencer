package poi.ui.image.edit.colour;

import javafx.scene.layout.Pane;

public class ColouredPane {
	
	private Pane pane;
	private Colour colour;
	
	public ColouredPane() {
		this(new Colour(0, 0, 0));
	}
	
	public ColouredPane(Colour colour) {
		pane = new Pane();
		setColour(colour);
		pane.setPrefSize(30, 30);
	}
	
	private String generateHexString(int value) {
		String hex = Integer.toHexString(value);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}
	
	public void setColour(Colour colour) {
		this.colour = colour;
		
		String red = generateHexString(colour.getR());
		String green = generateHexString(colour.getG());
		String blue = generateHexString(colour.getB());
		
		pane.setStyle(String.format("-fx-background-color: #%s%s%s", red, green, blue));
	}
	
	public Colour getColour() {
		return colour;
	}
	
	public Pane getNode() {
		return pane;
	}

}
