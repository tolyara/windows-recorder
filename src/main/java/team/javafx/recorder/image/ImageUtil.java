package team.javafx.recorder.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import team.javafx.recorder.tool.SelectedZone;
import team.javafx.recorder.util.PathCreator;

/**
 * Utility methods used to interact with images.
 */
public class ImageUtil {

	private final static Logger logger = LogManager.getLogger(ImageUtil.class);

	private static final boolean equals(final int[] data1, final int[] data2) {
		final int length = data1.length;
		if (length != data2.length) {
			logger.debug("File lengths are different.");
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (data1[i] != data2[i]) {

				// If the alpha is 0 for both that means that the pixels are 100%
				// transparent and the color does not matter. Return false if
				// only 1 is 100% transparent.
				if ((((data1[i] >> 24) & 0xff) == 0) && (((data2[i] >> 24) & 0xff) == 0)) {
					logger.debug(String.format("Both pixles at spot %s are different but 100% transparent.",
							Integer.valueOf(i)));
				} else {
					logger.debug(String.format("The pixel %s is different.", Integer.valueOf(i)));
					return false;
				}
			}
		}
		logger.debug("Both groups of pixels are the same.");
		return true;
	}

	private static final int[] getPixels(final BufferedImage img, final File file) {

		final int width = img.getWidth();
//		final int imageWidth = img.getWidth();
		final int height = img.getHeight();

		int[] pixelData = getPixelData(img, file, 0, 0, width, height);

		return pixelData;
	}

	private static final int[] getPixels(final BufferedImage img, final File file, SelectedZone zone) {

		final int width = zone.getZone().width;
//		final int imageWidth = img.getWidth();
		final int height = zone.getZone().height;
		final int leftUpperX = zone.getZone().x;
		final int leftUpperY = zone.getZone().y;
		
//		range.getWeight()
//		range.getHeight()

		int[] pixelData = getPixelData(img, file, leftUpperX, leftUpperY, width, height);

		return pixelData;
	}

	private static int[] getPixelData(final BufferedImage img, final File file, final int leftUpperX, final int leftUpperY,
			final int width, final int height) {

		int[] pixelData = new int[width * height];

		final Image pixelImg;
		if (img.getColorModel().getColorSpace() == ColorSpace.getInstance(ColorSpace.CS_sRGB)) {
			pixelImg = img;
		} else {
			pixelImg = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(img, null);
		}

//		final PixelGrabber pg = new PixelGrabber(pixelImg, 0, 0, width, height, pixelData, 0, imageWidth);
		final PixelGrabber pg = new PixelGrabber(pixelImg, leftUpperX, leftUpperY, width, height, pixelData, 0, width);

		try {
			if (!pg.grabPixels()) {
				throw new RuntimeException();
			}
		} catch (final InterruptedException ie) {
			throw new RuntimeException(file.getPath(), ie);
		}
		return pixelData;
	}

	/**
	 * Gets the {@link BufferedImage} from the passed in {@link File}.
	 * 
	 * @param file The <code>File</code> to use.
	 * @return The resulting <code>BufferedImage</code>
	 */
	@SuppressWarnings("unused")
	final static BufferedImage getBufferedImage(final File file) {
		Image image;

		try (final FileInputStream inputStream = new FileInputStream(file)) {
			// ImageIO.read(file) is broken for some images so I went this
			// route
			image = Toolkit.getDefaultToolkit().createImage(file.getCanonicalPath());

			// forces the image to be rendered
			new ImageIcon(image);
		} catch (final Exception e2) {
			throw new RuntimeException(file.getPath(), e2);
		}

		final BufferedImage converted = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = converted.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return converted;
	}

	/**
	 * Compares file1 to file2 to see if they are the same based on a visual pixel
	 * by pixel comparison. This has issues with marking images different when they
	 * are not. Works perfectly for all images.
	 * 
	 * @param file1 First file to compare
	 * @param file2 Second image to compare
	 * @return <code>true</code> if they are equal, otherwise <code>false</code>.
	 */
	private final static boolean visuallyCompareJava(final File file1, final File file2) {
		return equals(getPixels(getBufferedImage(file1), file1), getPixels(getBufferedImage(file2), file2));
	}

	private final static boolean visuallyCompareJava(final File file1, final File file2, SelectedZone zone) {
		return equals(getPixels(getBufferedImage(file1), file1, zone), getPixels(getBufferedImage(file2), file2, zone));
	}

	/**
	 * Compares file1 to file2 to see if they are the same based on a visual pixel
	 * by pixel comparison. This has issues with marking images different when they
	 * are not. Works perfectly for all images.
	 * 
	 * @param file1 Image 1 to compare
	 * @param file2 Image 2 to compare
	 * @return <code>true</code> if both images are visually the same.
	 */
	public final static boolean visuallyCompare(final File file1, final File file2) {

		return compareImages(file1, file2, null);
	}

	private static boolean compareImages(final File file1, final File file2, SelectedZone zone) {
		logger.debug(String.format("Start comparing %s and %s ...", file1.getPath(), file2.getPath()));
		if (file1 == file2) {
			return true;
		}

		boolean answer;
		if (zone == null) {
			answer = visuallyCompareJava(file1, file2);
		} else {
			answer = visuallyCompareJava(file1, file2, zone);
		}

		if (!answer) {
			logger.debug(String.format(
					"The files %s and %s are not pixel by pixel the same image. Manual comparison required.",
					file1.getPath(), file2.getPath()));
		}
		logger.debug(String.format("Finish comparing %s and %s.", file1.getPath(), file2.getPath()));

		return answer;
	}

	public final static boolean visuallyCompare(final File file1, final File file2, SelectedZone zone) {

		return compareImages(file1, file2, zone);
//		logger.debug(String.format("Start comparing %s and %s ...", file1.getPath(), file2.getPath()));
//		if (file1 == file2) {
//			return true;
//		}
//
//		boolean answer = visuallyCompareJava(file1, file2, zone);
//		if (!answer) {
//			logger.debug(String.format(
//					"The files %s and %s are not pixel by pixel the same image. Manual comparison required.",
//					file1.getPath(), file2.getPath()));
//		}
//		logger.debug(String.format("Finish comparing %s and %s.", file1.getPath(), file2.getPath()));
//
//		return answer;
	}

	/**
	 * @param file The image to check
	 * @return <code>true</code> if the image contains one or more pixels with some
	 *         percentage of transparency (Alpha)
	 */
	public final static boolean containsAlphaTransparency(final File file) {
		logger.debug(String.format("Start Alpha pixel check for %s ...", file.getPath()));

		boolean answer = false;
		for (final int pixel : getPixels(getBufferedImage(file), file)) {
			// If the alpha is 0 for both that means that the pixels are 100%
			// transparent and the color does not matter. Return false if
			// only 1 is 100% transparent.
			if (((pixel >> 24) & 0xff) != 255) {
				logger.debug("The image contains Aplha Transparency.");
				return true;
			}
		}

		logger.debug(String.format("The image does not contain Aplha Transparency."));
		logger.debug(String.format("End Alpha pixel check for %s.", file.getPath()));

		return answer;
	}

	// TODO refactor, many matches with ScreenshotSaverUtil.saveScreenshot
	public static String createDifferenceImage(final File base, final File compared) {
		String dirForSave = ScreenshotSaverUtil.path + "\\diff\\";
		PathCreator.createDirIfNotExist(dirForSave);
		String diffImageName = dirForSave + "diff_" + base.getName() + ".jpg";
		ScreenshotSaverUtil.saveImage(getDifferenceImage(base, compared), diffImageName);
		return diffImageName;

//		try {
//			BufferedImage diffImage = getDifferenceImage(file1, file2);
//			ImageIO.write(diffImage, "JPG", new File(ScreenshotSaverUtil.path + "\\diff\\diff.jpg"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private static BufferedImage getDifferenceImage(File baseImage, File compareImage) {
		BufferedImage bImage = null;
		BufferedImage cImage = null;
		try {
			bImage = ImageIO.read(baseImage);
			cImage = ImageIO.read(compareImage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int height = bImage.getHeight();
		int width = bImage.getWidth();
		BufferedImage rImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				try {
					int pixelC = cImage.getRGB(x, y);
					int pixelB = bImage.getRGB(x, y);
					if (pixelB == pixelC) {
						rImage.setRGB(x, y, bImage.getRGB(x, y));
					} else {
						int a = 0xff | bImage.getRGB(x, y) >> 24, r = 0xff & bImage.getRGB(x, y) >> 16,
								g = 0x00 & bImage.getRGB(x, y) >> 8, b = 0x00 & bImage.getRGB(x, y);

						int modifiedRGB = a << 24 | r << 16 | g << 8 | b;
						rImage.setRGB(x, y, modifiedRGB);
					}
				} catch (Exception e) {
					// handled hieght or width mismatch
					rImage.setRGB(x, y, 0x80ff0000);
				}
			}
		}
		return rImage;
	}

}