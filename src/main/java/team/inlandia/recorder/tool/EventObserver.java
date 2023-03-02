package team.inlandia.recorder.tool;

import javafx.scene.control.ListView;
import team.inlandia.recorder.event.UserEvent;
import team.inlandia.recorder.observer.Observer;

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
