package poi.utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageUtilities {
	
	private ImageUtilities() {
		// Do nothing
	}
	  
	public static Image resample(BufferedImage input, int scaleFactor) {
		return resample(SwingFXUtils.toFXImage(input, null), scaleFactor);
	}
	  
	public static Image resample(Image input, int scaleFactor) {
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int S = scaleFactor;

		WritableImage output = new WritableImage(W * S, H * S);

		PixelReader reader = input.getPixelReader();
		PixelWriter writer = output.getPixelWriter();

		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				final int argb = reader.getArgb(x, y);
				for (int dy = 0; dy < S; dy++) {
					for (int dx = 0; dx < S; dx++) {
						writer.setArgb(x * S + dx, y * S + dy, argb);
					}
				}
			}
		}

		return output;
	}
	
	public static BufferedImage compressImage(BufferedImage inputImage, int height) {
        int width = (int) (inputImage.getWidth() * (height / (double) inputImage.getHeight()));
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(width,
        		height, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();
 
        return outputImage;
	}
	
	public static BufferedImage convertImageTo6BitColourPalette(BufferedImage inputImage) {
        // creates output image
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(),
        		inputImage.getHeight(), inputImage.getType());
 
        for (int x = 0; x < inputImage.getWidth(); x++) {
        	for (int y = 0; y < inputImage.getHeight(); y++) {
        		int oldRgb = inputImage.getRGB(x, y);
        		int newR = 85 * round(((oldRgb >> 16) & 0xff) / 85.0);
        		int newG = 85 * round(((oldRgb >> 8) & 0xff) / 85.0);
        		int newB = 85 * round((oldRgb & 0xff) / 85.0);
        		
        		int newRgb = newB + (newG << 8) + (newR << 16);
            	outputImage.setRGB(x, y, newRgb);
            }
        }
 
        return outputImage;
	}
	
	private static int round(double value) {
		return (int) (value + 0.5);
	}

}
