package hr.neos.directorybrowserapi.service.DirectoryReader;

public interface Task extends Runnable {
	void setRun(boolean run);

	void setStopDemanded(boolean stopDemanded);
}
