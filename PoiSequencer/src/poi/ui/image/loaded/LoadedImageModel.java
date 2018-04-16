package poi.ui.image.loaded;

import java.util.HashSet;
import java.util.Set;

import poi.observable.Observable;
import poi.observable.ObserverManager;
import poi.observable.ObserverManagerImpl;
import poi.observable.ObserverType;
import poi.ui.image.ImageData;

public class LoadedImageModel implements Observable {
	
	public static final ObserverType<ImageData> IMAGE_ADDED = new ObserverType<>();
	public static final ObserverType<ImageData> IMAGE_REMOVED = new ObserverType<>();
	
	private ObserverManager observerManager = new ObserverManagerImpl();
	
	private Set<ImageData> images = new HashSet<>();
	
	public void addImage(ImageData image) {
		if (!images.contains(image)) {
			images.add(image);
			observerManager.notifyObservers(IMAGE_ADDED, image);
		}
	}
	
	public void removeImage(ImageData image) {
		if (images.contains(image)) {
			images.remove(image);
			observerManager.notifyObservers(IMAGE_REMOVED, image);
		}
	}
	
	public ObserverManager getObserverManager() {
		return observerManager;
	}

}
