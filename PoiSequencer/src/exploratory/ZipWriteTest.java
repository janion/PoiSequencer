package exploratory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

public class ZipWriteTest {

	public static void main(String[] args) throws IOException {
		File f = new File("test.poi");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		ZipEntry e = new ZipEntry("mytext.txt");
		out.putNextEntry(e);
        out.write("hello".getBytes());
        out.closeEntry();

		ZipEntry e2 = new ZipEntry("image.bmp");
		out.putNextEntry(e2);
		BufferedImage img = ImageIO.read(ZipWriteTest.class.getResourceAsStream("mini5.bmp"));
		img.setRGB(0, 0, 255 << 8);
        ImageIO.write(img, "bmp", out);
        out.closeEntry();
		out.close();
	}

}
