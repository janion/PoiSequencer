package poi.utility;

import static java.nio.file.StandardOpenOption.APPEND;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;

public class ImageExportUtilities {
	
	private ImageExportUtilities() {
		// Do nothing
	}

	public static void ExportTimelineIn6Bit(TimelineModel timelineModel, File exportFile)
			throws IOException, URISyntaxException {
		boolean firstFile = true;
		for (ImageModel imageModel : timelineModel.getImages()) {
			BufferedImage toExport = ImageUtilities.convertImageTo6BitColourPalette(imageModel.getImageData().getImage());
			byte[] byteArray = createByteArray(toExport, imageModel.getDuration(), exportFile);
			
			if (firstFile) {
				Files.write(exportFile.toPath(), byteArray);
				firstFile = false;
			} else {
				Files.write(exportFile.toPath(), byteArray, APPEND);
			}
			Files.write(exportFile.toPath(), new byte[] {(byte) 255}, APPEND);
		}
	}

	public static void ExportImageIn6Bit(ImageModel imageModel, File exportFile)
			throws IOException, URISyntaxException {
		BufferedImage toExport = ImageUtilities.convertImageTo6BitColourPalette(imageModel.getImageData().getImage());
		byte[] byteArray = createByteArray(toExport, imageModel.getDuration(), exportFile);

		Files.write(exportFile.toPath(), byteArray);
	}

	private static byte[] createByteArray(BufferedImage toExport, double duration, File exportFile)
			throws IOException, URISyntaxException {
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

		return byteArray;
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
