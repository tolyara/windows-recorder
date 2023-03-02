package team.inlandia.recorder.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import team.inlandia.recorder.util.PathCreator;


public class ScreenshotSaverUtil {
	
	public static String path = System.getProperty("user.dir") + "\\screenshots\\";

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");

	public static synchronized String saveScreenshot(String folder) {
		Calendar now = Calendar.getInstance();				
//		BufferedImage image = null;
		String dirForSave =  path + "\\" + folder + "\\";
		String imageName = dirForSave + formatter.format(now.getTime()) + ".png";
		
//		try {
//			image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//		} 
//		catch (HeadlessException | AWTException e) {
//			e.printStackTrace();
//		}
	
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		WritableImage image = new Robot().getScreenCapture(null, screenBounds);
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

		PathCreator.createDirIfNotExist(dirForSave);
		saveImage(bufferedImage, imageName);		
		return imageName;
	}
	
//	public static void saveImageFx(WritableImage image, String imageName) {
//
//	}

	public static void saveImage(BufferedImage image, String imageName) {
		try {
			ImageIO.write(image, "png", new File(imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}


