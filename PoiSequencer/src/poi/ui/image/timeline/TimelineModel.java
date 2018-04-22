package poi.ui.image.timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import poi.observable.Observable;
import poi.observable.ObserverManager;
import poi.observable.ObserverManagerImpl;
import poi.observable.ObserverType;
import poi.utility.Pair;

public class TimelineModel implements Observable {

	public static final ObserverType<Pair<ImageModel, Integer>> IMAGE_ADDED = new ObserverType<>();
	public static final ObserverType<ImageModel> IMAGE_REMOVED = new ObserverType<>();
	public static final ObserverType<Void> CLEARED = new ObserverType<>();
	
	private ObserverManager observerManager = new ObserverManagerImpl();
	
	private List<ImageModel> images = new ArrayList<>();
	
	public List<ImageModel> getImages() {
		return images;
	}
	
	public void addImage(ImageModel image) {
		images.add(image);
		observerManager.notifyObservers(IMAGE_ADDED, new Pair<>(image, images.size() - 1));
	}
	
	public void addImage(ImageModel image, int index) {
		images.add(index, image);
		observerManager.notifyObservers(IMAGE_ADDED, new Pair<>(image, index));
	}
	
	public void removeImage(ImageModel image) {
		images.remove(image);
		observerManager.notifyObservers(IMAGE_REMOVED, image);
	}
	
	public void clear() {
		images.clear();
		observerManager.notifyObservers(CLEARED, null);
	}
	
	@Override
	public ObserverManager getObserverManager() {
		return observerManager;
	}
	
	public double getTotalDuration() {
		return images.stream().collect(Collectors.summingDouble(ImageModel::getDuration));
	}

}
