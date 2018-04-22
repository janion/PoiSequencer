package poi.ui;

import java.net.URL;

public class CurrentProjectModel {

	private URL currentProject;
	private URL lastProject;
	private URL lastTimelineExport;
	
	public void setCurrentProject(URL currentProject) {
		if (currentProject == null) {
			lastProject = this.currentProject;
			lastTimelineExport = null;
		}
		this.currentProject = currentProject;
	}
	
	public URL getCurrentProject() {
		return currentProject;
	}
	
	public URL getLastProject() {
		return lastProject != null ? lastProject : currentProject;
	}
	
	public URL getLastTimelineExport() {
		return lastTimelineExport;
	}
	
	public void setLastTimelineExport(URL lastTimelineExport) {
		this.lastTimelineExport = lastTimelineExport;
	}

}
