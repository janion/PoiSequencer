package poi.observable;

public interface ObserverManager {
	
	public <T> void addObserver(ObserverType<T> observerType, Observer<T> observer);
	
	public <T> void removeObserver(ObserverType<T> observerType, Observer<T> observer);
	
	public <T> void notifyObservers(ObserverType<T> observerType, T value);

}
