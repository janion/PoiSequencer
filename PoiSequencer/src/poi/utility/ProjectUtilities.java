package poi.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import poi.ui.image.ImageData;
import poi.ui.image.loaded.LoadedImageModel;
import poi.ui.image.timeline.ImageModel;
import poi.ui.image.timeline.TimelineModel;

public class ProjectUtilities {

	private static final String IMAGE_FORMAT = "bmp";
	private static final String LOADED_IMAGE_FORMAT = "loaded_%d.bmp";
	private static final String TIMELINE_IMAGE_FORMAT = "timeline_%d.bmp";
	private static final String LOADED_SUMMARY = "loaded.csv";
	private static final String TIMELINE_SUMMARY = "timeline.csv";

	private static final String NULL = "NULL";
	private static final String COMMA = ",";

	public static void saveProject(LoadedImageModel loadedImageModel, TimelineModel timelineModel, File file)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
		
		List<String> loadedFiles = new ArrayList<>();
		
		int loadedImageCount = 0;
		for (ImageData imgData : loadedImageModel.getImages()) {
			String savedImageName = String.format(LOADED_IMAGE_FORMAT, loadedImageCount++);
			ZipEntry entry = new ZipEntry(savedImageName);
			out.putNextEntry(entry);
	        ImageIO.write(imgData.getImage(), IMAGE_FORMAT, out);
	        out.closeEntry();
	        
	        String imageFilePath = imgData.getFilePath() == null ? NULL : imgData.getFilePath().toString();
	        loadedFiles.add(savedImageName + COMMA + imageFilePath);
		}
		
		List<String> timelineFiles = new ArrayList<>();

		int timelineImageCount = 0;
		for (ImageModel imgModel : timelineModel.getImages()) {
			String savedImageName = String.format(TIMELINE_IMAGE_FORMAT, timelineImageCount++);
			ZipEntry entry = new ZipEntry(savedImageName);
			out.putNextEntry(entry);
	        ImageIO.write(imgModel.getImageData().getImage(), IMAGE_FORMAT, out);
	        out.closeEntry();

			String imageFilePath = imgModel.getImageData().getFilePath() == null ? NULL
					: imgModel.getImageData().getFilePath().toString();
			String durationString = String.format("%5.1f", imgModel.getDuration());
			timelineFiles.add(
					savedImageName + COMMA + imageFilePath + COMMA + durationString);
		}

		ZipEntry loadedSummaryEntry = new ZipEntry(LOADED_SUMMARY);
		out.putNextEntry(loadedSummaryEntry);
		for (int i = 0; i < loadedFiles.size(); i++) {
			String line = loadedFiles.get(i);
			out.write(line.getBytes());
			if (i != loadedFiles.size() - 1) {
				out.write('\n');
			}
		}
        out.closeEntry();

		ZipEntry timelineSummaryEntry = new ZipEntry(TIMELINE_SUMMARY);
		out.putNextEntry(timelineSummaryEntry);
		for (int i = 0; i < timelineFiles.size(); i++) {
			String line = timelineFiles.get(i);
			out.write(line.getBytes());
			if (i != timelineFiles.size() - 1) {
				out.write('\n');
			}
		}
        out.closeEntry();
        
		out.close();
	}

	public static void loadProject(LoadedImageModel loadedImageModel, TimelineModel timelineModel, File file)
			throws IOException {
		loadedImageModel.clear();
		timelineModel.clear();
		
		ZipInputStream in = new ZipInputStream(new FileInputStream(file));

		List<String> loadedFiles = new ArrayList<>();
		List<String> timelineFiles = new ArrayList<>();
		Map<String, BufferedImage> zippedImages = new HashMap<>();
		
		ZipEntry entry;
		while ((entry = in.getNextEntry()) != null) {
			if (entry.getName().endsWith(IMAGE_FORMAT)) {
				zippedImages.put(entry.getName(), ImageIO.read(in));
			} else if (entry.getName().equals(LOADED_SUMMARY)) {
				Scanner scanner = new Scanner(in);
				 while (scanner.hasNextLine()) {
					 loadedFiles.add(scanner.nextLine());
				 }
				 // scanner.close() causes errors
			} else if (entry.getName().equals(TIMELINE_SUMMARY)) {
				Scanner scanner = new Scanner(in);
				 while (scanner.hasNextLine()) {
					 timelineFiles.add(scanner.nextLine());
				 }
				 // scanner.close() causes errors
			} else {
				System.out.println("Unknown item: " + entry.getName());
			}
			in.closeEntry();
		}
        
		in.close();
		
		for (String line : loadedFiles) {
			String[] entries = line.split(COMMA);
			loadedImageModel.addImage(new ImageData(zippedImages.get(entries[0]), entries[1].equals(NULL) ? null : new URL(entries[1])));
		}
		
		for (String line : timelineFiles) {
			String[] entries = line.split(COMMA);
			ImageData data = new ImageData(zippedImages.get(entries[0]), entries[1].equals(NULL) ? null : new URL(entries[1]));
			timelineModel.addImage(new ImageModel(Double.valueOf(entries[2]), data));
		}
	}

}
