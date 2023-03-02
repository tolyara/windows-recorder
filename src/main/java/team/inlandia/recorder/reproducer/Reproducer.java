package team.inlandia.recorder.reproducer;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import team.inlandia.recorder.event.KeyboardInput;
import team.inlandia.recorder.event.MouseClick;
import team.inlandia.recorder.event.UserEvent;
import team.inlandia.recorder.image.ImageUtil;
import team.inlandia.recorder.image.ScreenshotSaverUtil;
import team.inlandia.recorder.observer.Observable;
import team.inlandia.recorder.observer.Observer;
import team.inlandia.recorder.util.PathCreator;

//singleton
public class Reproducer implements Observable<UserEvent> {

	private static final Reproducer reproducer = new Reproducer();

	private List<UserEvent> eventsForReplay = new CopyOnWriteArrayList<>();
	
	private List<Observer<UserEvent>> observers = new ArrayList<>();

	private Reproducer() {

	}

	public static Reproducer getInstance() {
		return reproducer;
	}

	public List<UserEvent> getEvents() {
		return eventsForReplay;
	}

	public void setEvents(List<UserEvent> eventsForReplay) {
		this.eventsForReplay = eventsForReplay;
	}

	public void setNewEvent(UserEvent event) {
		eventsForReplay.add(event);
		notifyObservers(event);
	}

	public void removeLastEvent() {
		if (!eventsForReplay.isEmpty()) {
			eventsForReplay.remove(eventsForReplay.size() - 1);
		}
	}

	public void reproduce(ImageView imageView) {

		for (UserEvent userEvent : eventsForReplay) {
			if (userEvent instanceof KeyboardInput) {
				doSetInput(((KeyboardInput) userEvent).getInput());
				pressEnter();
			} else if (userEvent instanceof MouseClick) {
				MouseClick userClick = (MouseClick) userEvent;
				Point point = userClick.getPoint();
				String botScreenshot = doClickAndSaveScreenshot((int) point.getX(), (int) point.getY());
				if (!isScreenshotsEqual(userClick, botScreenshot)) {
					imageView.setImage(new Image(PathCreator
							.getPathAsURL(createDifferenceImage(userClick.getScreenshotPath(), botScreenshot))));
					return;
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String doClickAndSaveScreenshot(int x, int y) {
		Robot bot = new Robot();

		bot.mouseMove(x, y);
		bot.mousePress(MouseButton.PRIMARY);
		bot.mouseRelease(MouseButton.PRIMARY);
		return ScreenshotSaverUtil.saveScreenshot("robot");
	}

	private boolean isScreenshotsEqual(MouseClick userClick, String comparedImage) {
		File file1 = new File(userClick.getScreenshotPath());
		File file2 = new File(comparedImage);
		if (userClick.hasSelectedZone()) {
			return ImageUtil.visuallyCompare(file1, file2, userClick.getSelectedZone());
		} else {
			return ImageUtil.visuallyCompare(file1, file2);
		}

	}

	private String createDifferenceImage(String img1, String img2) {
		File file1 = new File(img1);
		File file2 = new File(img2);
		String diffImage = null;
		if (!ImageUtil.visuallyCompare(file1, file2)) {
			diffImage = ImageUtil.createDifferenceImage(file1, file2);
		}
		return diffImage;
	}

	private void doSetInput(String inputText) {
		Robot bot = new Robot();
		StringSelection stringSelection = new StringSelection(inputText);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		clipboard.setContents(stringSelection, stringSelection);
		bot.keyPress(KeyCode.CONTROL);
		bot.keyPress(KeyCode.V);
		bot.keyRelease(KeyCode.V);
		bot.keyRelease(KeyCode.CONTROL);
	}

	private void pressEnter() {
		doType(KeyCode.ENTER);
	}

	private void doType(KeyCode keyCode) {
		Robot bot = new Robot();

		bot.keyPress(keyCode);
		bot.keyRelease(keyCode);
	}

	public void clearEvents() {
		eventsForReplay.clear();
	}

	public boolean isEmpty() {
		return eventsForReplay.isEmpty();
	}

	@Override
	public void addObserver(Observer<UserEvent> observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<UserEvent> observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers(UserEvent event) {
		for (Observer<UserEvent> observer : observers) {
			observer.handleEvent(event);
		}
	}

}
