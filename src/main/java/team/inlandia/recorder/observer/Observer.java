package team.inlandia.recorder.observer;

public interface Observer<E> {
	
	public void handleEvent(E event);

}
