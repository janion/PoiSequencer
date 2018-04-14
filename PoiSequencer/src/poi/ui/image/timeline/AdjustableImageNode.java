package poi.ui.image.timeline;

import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class AdjustableImageNode {
	
	private StackPane stackPane;
	private HBox hbox;
	private Pane pane;
	
	private final double prefWidth;
	
	public AdjustableImageNode(Image img, double width) {
		prefWidth = width;
		
		pane = new Pane();
		hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_LEFT);
		ScrollPane scrollPane = new ScrollPane(hbox);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		stackPane = new StackPane(pane, scrollPane);
//		stackPane.setStyle("-fx-background-color: GREY; -fx-border-color: GREY; -fx-border-width: 5");
		hbox.setStyle("-fx-background-color: DIMGREY");
		
		addImageView(img);
		ChangeListener<Number> listener = (observable, oldVal, newVal) -> addOrRemoveImages(scrollPane, img);
		scrollPane.widthProperty().addListener(listener);
		scrollPane.heightProperty().addListener(listener);
		
		setWidth(width);
	}
	
	private void addOrRemoveImages(ScrollPane scrollPane, Image img) {
		while (scrollPane.getWidth() > hbox.getChildren().stream().map(ImageView.class::cast)
				.collect(Collectors.summingDouble(ImageView::getFitWidth))) {
			addImageView(img);
		}
		
		ImageView view = (ImageView) hbox.getChildren().get(0);
		int minViews = (int) Math.ceil(scrollPane.getWidth() / view.getFitWidth());
		while(hbox.getChildren().size() > minViews) {
			hbox.getChildren().remove(minViews);
		}
	}
	
	private void addImageView(Image img) {
		ImageView imageView = new ImageView(img);
		hbox.getChildren().add(imageView);
		
		imageView.fitHeightProperty().bind(pane.heightProperty());
		imageView.fitWidthProperty().bind(imageView.fitHeightProperty().multiply(img.getWidth() / img.getHeight()));
	}
	
	public StackPane getNode() {
		return stackPane;
	}
	
	public void setWidth(double width) {
		stackPane.setMinWidth(width);
		stackPane.setMaxWidth(width);
	}
	
	public double getWidth() {
		return stackPane.getMinWidth();
	}
	
	public double getPrefWidth() {
		return prefWidth;
	}

}
