package team.javafx.recorder.fortest;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.javafx.recorder.image.ScreenshotSaverUtil;

public class TestFx extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
//		Robot robot = new Robot();
		
//		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
//		System.out.println(screenBounds);
		
		ScreenshotSaverUtil.saveScreenshot("test");

	}
	
	private void testImage(Stage primaryStage) {
		// load the image
//        Image image = new Image("file:///C:\\Users\\��������\\git\\webtesttool\\WindowsRecorder\\test.jpg");

		Image image = null;
		try {
			image = new Image(Paths.get("C:\\Users\\��������\\git\\webtesttool\\WindowsRecorder\\test.jpg").toUri()
					.toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// simple displays ImageView the image as is
		ImageView iv1 = new ImageView();
		iv1.setImage(image);

		// resizes the image to have width of 100 while preserving the ratio and using
		// higher quality filtering method; this ImageView is also cached to
		// improve performance
		ImageView iv2 = new ImageView();
		iv2.setImage(image);
		iv2.setFitWidth(100);
		iv2.setPreserveRatio(true);
		iv2.setSmooth(true);
		iv2.setCache(true);

		// defines a viewport into the source image (achieving a "zoom" effect) and
		// displays it rotated
		ImageView iv3 = new ImageView();
		iv3.setImage(image);
		Rectangle2D viewportRect = new Rectangle2D(40, 35, 110, 110);
		iv3.setViewport(viewportRect);
		iv3.setRotate(90);

		Group root = new Group();
		Scene scene = new Scene(root);
		scene.setFill(Color.BLACK);
		HBox box = new HBox();
		box.getChildren().add(iv1);
//        box.getChildren().add(iv2);
//        box.getChildren().add(iv3);
		root.getChildren().add(box);

		primaryStage.setTitle("ImageView");
		primaryStage.setWidth(415);
		primaryStage.setHeight(200);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
}

