package team.javafx.recorder.event;

import team.javafx.recorder.tool.SelectedZone;

public class KeyboardInput extends UserEvent {

	private String input;
	
	public KeyboardInput(String name, String input) {
		super(name);
		this.input = input;
	}

	public String getInput() {
		return input;
	}
	
	@Override
	public String toString() {
		return "KeyboardInput [text=" + input + "]";
	}
	
	public String getScreenshotPath() {
		return null;
	}

	@Override
	public void setSelectedZone(SelectedZone selectedZone) {
		
	}
	
}
