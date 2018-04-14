package poi.observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ObserverManagerImpl implements ObserverManager {
	
	private Map<ObserverType<?>, List<Observer<?>>> observers;
	private ReentrantLock lock = new ReentrantLock();
	
	public ObserverManagerImpl() {
		observers = new HashMap<>();
	}

	@Override
	public <T> void addObserver(ObserverType<T> observerType, Observer<T> observer) {
		lock.lock();
		try {
			List<Observer<?>> observersForType = observers.get(observerType);
			if (observersForType == null) {
				observersForType = new ArrayList<>();
				observers.put(observerType, observersForType);
			}
			observersForType.add(observer);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <T> void removeObserver(ObserverType<T> observerType, Observer<T> observer) {
		lock.lock();
		try {
			List<Observer<?>> observersForType = observers.get(observerType);
			if (observersForType != null) {
				observersForType.remove(observer);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <T> void notifyObservers(ObserverType<T> observerType, T value) {
		lock.lock();
		try {
			List<Observer<?>> observersForType = observers.get(observerType);
			if (observersForType != null) {
				for (Observer<?> observer : observersForType) {
					@SuppressWarnings("unchecked")
					Observer<T> castObserver = (Observer<T>) observer;
					castObserver.notify(value);
				}
			}
		} finally {
			lock.unlock();
		}
	}

}
