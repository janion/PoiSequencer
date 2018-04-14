package poi.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ImageExportUtilities {
	
	private ImageExportUtilities() {
		// Do nothing
	}
	
	public static void ExportImageIn6Bit(BufferedImage input, File exportFile, double duration) throws IOException {
		BufferedImage toExport = ImageUtilities.convertImageTo6BitColourPalette(input);
		
		List<Byte> bytes = new ArrayList<>();
		bytes.addAll(getDurationBytes(duration));
		
		for (int y = 0; y < toExport.getHeight(); y++) {
			for (int x = 0; x < toExport.getWidth(); x++) {
				int rgb = toExport.getRGB(x, y);

				int r = ((rgb >> 16) & 0xff) / 85;
				int g = ((rgb >> 8) & 0xff) / 85;
				int b = (rgb & 0xff) / 85;
				byte bite = (byte) (b + (g << 2) + (r << 4));
				bytes.add(bite);
			}
		}

		byte[] byteArray = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			byteArray[i] = bytes.get(i);
		}
		
	    Files.write(exportFile.toPath(), byteArray);
	}
	
	private static List<Byte> getDurationBytes(double duration) {
		String durationString = String.format("%05.1f", duration);
		
		List<Byte> bytes = new ArrayList<>();
		for (byte bite : durationString.getBytes()) {
			bytes.add(bite);
		}
		
		return bytes;
	}

}
