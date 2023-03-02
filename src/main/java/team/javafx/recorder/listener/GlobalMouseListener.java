package team.javafx.recorder.listener;

import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import javafx.application.Platform;
import team.javafx.recorder.event.MouseClick;
import team.javafx.recorder.image.ScreenshotSaverUtil;
import team.javafx.recorder.observer.Observable;
import team.javafx.recorder.observer.Observer;
import team.javafx.recorder.reproducer.Reproducer;

public class GlobalMouseListener implements NativeMouseInputListener, Observable {

	private List<Observer> observers = new ArrayList<>();

	@Override
	public void addObserver(Observer observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers(Object event) {
		for (Observer observer : observers) {
			observer.handleEvent(event);
		}
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {

	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		System.out.println("Mouse Pressed: " + e.getButton());
		System.out.println("Position: " + e.getX() + " " + e.getY());
		System.out.println();

		notifyObservers(e);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				waitForSomeActionPerformedAfterClick();
				String screenshotPath = ScreenshotSaverUtil.saveScreenshot("user");
				Reproducer.getInstance().setNewEvent(
						new MouseClick("mouse click", MouseInfo.getPointerInfo().getLocation(), screenshotPath));
//				notifyObservers(e);
			}
		});
	}

	private void waitForSomeActionPerformedAfterClick() {
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {

	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {

	}

}
