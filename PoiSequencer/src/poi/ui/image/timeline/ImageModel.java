package poi.ui.image.timeline;

import poi.observable.Observable;
import poi.observable.ObserverManager;
import poi.observable.ObserverManagerImpl;
import poi.observable.ObserverType;
import poi.ui.image.ImageData;

public class ImageModel implements Observable {
	
	public static final ObserverType<Double> DURATION_CHANGED = new ObserverType<>();
	
	private ObserverManager observerManager = new ObserverManagerImpl();
	
	private double duration;
	private ImageData imageData;
	
	public ImageModel(double duration, ImageData imageData) {
		this.duration = duration;
		this.imageData = imageData;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		if (duration != this.duration) {
			this.duration = duration;
			observerManager.notifyObservers(DURATION_CHANGED, duration);
		}
	}
	
	public ImageData getImageData() {
		return imageData;
	}
	
	@Override
	public ObserverManager getObserverManager() {
		return observerManager;
	}

}
