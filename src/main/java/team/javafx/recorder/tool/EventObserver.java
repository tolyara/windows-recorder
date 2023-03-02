package team.javafx.recorder.tool;

import javafx.scene.control.ListView;
import team.javafx.recorder.event.UserEvent;
import team.javafx.recorder.observer.Observer;

public class EventObserver implements Observer<UserEvent> {

	ListView<UserEvent> userEventsLog;
	
	public EventObserver(ListView<UserEvent> userEventsLog) {
		this.userEventsLog = userEventsLog;
	}

	@Override
	public void handleEvent(UserEvent event) {
		userEventsLog.getItems().add(event);
	}

}
