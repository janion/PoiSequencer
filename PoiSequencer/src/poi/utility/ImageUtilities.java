package poi.utility;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageUtilities {
	
	private ImageUtilities() {
		// Do nothing
	}
	
	public static BufferedImage copyImage(BufferedImage input) {
		ColorModel cm = input.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = input.copyData(null);
		
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
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

        int compressionFactorX = inputImage.getWidth() / (width);
        int compressionFactorY = inputImage.getHeight() / (height);

        for (int x = 0; x < width; x++) {
        	for (int y = 0; y < height; y++) {
            	int originX = x * compressionFactorX;
            	int originY = y * compressionFactorY;

            	int r = 0;
            	int g = 0;
            	int b = 0;

                for (int i = 0; i < compressionFactorX; i++) {
                	for (int j = 0; j < compressionFactorY; j++) {
            			int rgb = inputImage.getRGB(originX + i, originY + j);
                		r += (rgb >> 16) & 0xff;
                		g += (rgb >> 8) & 0xff;
                		b += rgb & 0xff;
                	}
                }
                int totalPixels = compressionFactorX * compressionFactorY;
                int newRgb = (b / totalPixels) + ((g / totalPixels) << 8) + ((r / totalPixels) << 16);
                outputImage.setRGB(x, y, newRgb);
            }
        }
 
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
	
	public static BufferedImage compressImageTo6BitColourPalette(BufferedImage inputImage, int height) {
		return convertImageTo6BitColourPalette(compressImage(inputImage, height));
	}
	
	private static int round(double value) {
		return (int) (value + 0.5);
	}

}
