package poi.ui;

import java.net.URL;

public class CurrentProjectModel {

	private URL currentProject;
	private URL lastProject;
	
	public void setCurrentProject(URL currentProject) {
		if (currentProject == null) {
			lastProject = this.currentProject;
		}
		this.currentProject = currentProject;
	}
	
	public URL getCurrentProject() {
		return currentProject;
	}
	
	public URL getLastProject() {
		return lastProject != null ? lastProject : currentProject;
	}

}
