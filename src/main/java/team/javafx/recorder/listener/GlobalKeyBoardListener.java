package team.javafx.recorder.listener;

import java.util.ArrayList;
import java.util.List;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;
import team.javafx.recorder.event.KeyboardInput;
import team.javafx.recorder.observer.Observer;
import team.javafx.recorder.reproducer.Reproducer;

public class GlobalKeyBoardListener implements NativeKeyListener, Observer {

	private StringBuilder keyboardInput = new StringBuilder();

	private List<NativeKeyEvent> fullKeyboardStroke = new ArrayList<NativeKeyEvent>();

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {

	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		String key = NativeKeyEvent.getKeyText(e.getKeyCode());
		System.out.println("Key Pressed: " + key);

		if (e.getKeyCode() == NativeKeyEvent.VC_ENTER) {
			saveInputAndResetBuffer();
		} else if (e.getKeyCode() == NativeKeyEvent.VC_PERIOD) {
			keyboardInput.append('.');
			fullKeyboardStroke.add(e);
		} else if (e.getKeyCode() == NativeKeyEvent.VC_BACKSPACE) {
			// TODO - delete char in depending of cursor, not only last char
			deleteLastChar();
		} else if (e.isActionKey()) {
			// don't save system keys
		} else {
			checkIsUpperCase(key);
			fullKeyboardStroke.add(e);
		}

		System.out.println("keyboardInput = " + keyboardInput);
		System.out.println();
	}

	private void deleteLastChar() {
		if (keyboardInput.length() > 0) {
			keyboardInput.deleteCharAt(keyboardInput.length() - 1);
		}
	}

	private void checkIsUpperCase(String key) {
		if (fullKeyboardStroke.isEmpty()) {
			keyboardInput.append(key.toLowerCase());
		} else {
			if (isPreviousKeyShift()) {
				keyboardInput.append(key.toUpperCase());
			} else {
				keyboardInput.append(key.toLowerCase());
			}
		}
	}

	// TODO - fix this method
	private boolean isPreviousKeyShift() {
		return fullKeyboardStroke.get(fullKeyboardStroke.size() - 1).getKeyCode() == NativeKeyEvent.VC_SHIFT;
	}

	private void saveInputAndResetBuffer() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (keyboardInput.length() > 0) {
					Reproducer.getInstance().setNewEvent(new KeyboardInput("keyboard input", keyboardInput.toString()));
					keyboardInput.setLength(0);
					fullKeyboardStroke.clear();
				}
			}
		});
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {

	}

	@Override
	public void handleEvent(Object event) {
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				if (keyboardInput.length() > 0) {
//					saveInputAndResetBuffer();
//				}
//			}
//		});
		
		saveInputAndResetBuffer();
	}

}
