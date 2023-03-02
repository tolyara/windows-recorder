package team.javafx.recorder.observer;

public interface Observable<E> {
	
	public void addObserver(Observer<E> observer);
	
	public void removeObserver(Observer<E> observer);

	public void notifyObservers(E event);

}
