package team.javafx.recorder;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import team.javafx.recorder.image.ImageUtil;
import team.javafx.recorder.image.ScreenshotSaverUtil;
import team.javafx.recorder.listener.GlobalKeyBoardListener;
import team.javafx.recorder.listener.GlobalMouseListener;
import team.javafx.recorder.reproducer.Reproducer;

@Deprecated
public class Demo {

	private static Reproducer reproducer = Reproducer.getInstance();

	public static void main(String[] args) throws InterruptedException, AWTException {
//		registerNativeHookLibrary();		
//		runSearchInGoogledDemo();

		ScreenshotSaverUtil.saveScreenshot("test2");
		
//		runMarkDifferentPixelsDemo();

//		System.out.println(0x002A);
//		new Typer().doTypeSingle(KeyEvent.VK_PERIOD);		
//		reproducer.reproduce(Arrays.asList("google.com", "testsearch"));
	}

	static void runSearchInGoogledDemo() {
		initMouseListener();
		initKeyboardListener();
		createAwtApp(reproducer);
	}

	static void runMarkDifferentPixelsDemo() {
		File file1 = new File(ScreenshotSaverUtil.path + "\\samples\\img1.jpg");
		File file2 = new File(ScreenshotSaverUtil.path + "\\samples\\img2.jpg");
		ImageUtil.createDifferenceImage(file1, file2);
	}

	private static void runInputFieldDemo() {
		try {
			RobotDemo.doKeyInput();
		} catch (AWTException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void runRecorderDemo() {
		initMouseListener();
		createAwtApp(reproducer);
	}

	private static void createAwtApp(Reproducer reproducer) {
		Frame frame = getFrame();
		Panel panel = new Panel();
		Button stopButton = new Button("Stop");
		Button replayButton = new Button("Replay");

		frame.add(panel);
		panel.add(stopButton);
		panel.add(replayButton);

		stopButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				try {
					GlobalScreen.unregisterNativeHook();
					reproducer.removeLastEvent();
				} catch (NativeHookException e) {
					e.printStackTrace();
				}
			}
		});
		replayButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				// reproducer.reproduce();
				System.exit(0);
			}
		});
	}

	private static void initMouseListener() {
		GlobalScreen.addNativeMouseListener(new GlobalMouseListener());
	}

	private static void initKeyboardListener() {
		GlobalScreen.addNativeKeyListener(new GlobalKeyBoardListener());
	}

	private static void registerNativeHookLibrary() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	private static boolean compareScreenshots() {
		File file1 = new File(ScreenshotSaverUtil.path + "\\samples\\111.jpg");
		File file2 = new File(ScreenshotSaverUtil.path + "\\samples\\222.jpg");
		return ImageUtil.visuallyCompare(file1, file2);
	}

	private static Frame getFrame() {
		final Frame jFrame = new Frame();
		jFrame.setVisible(true);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension dimension = toolkit.getScreenSize();
		jFrame.setBounds(dimension.width / 2 - 250, dimension.height / 2 - 150, 500, 300);
		jFrame.setTitle("Windows Recorder Demo");
		return jFrame;
	}

}
