package team.javafx.recorder.event;

import java.io.Serializable;

import team.javafx.recorder.tool.SelectedZone;

public abstract class UserEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String eventName = "";

	public UserEvent(String name) {
		this.eventName = name;
	}

	public String getName() {
		return eventName;
	}

	public abstract String getScreenshotPath();
	
	public abstract void setSelectedZone(SelectedZone selectedZone);

}
