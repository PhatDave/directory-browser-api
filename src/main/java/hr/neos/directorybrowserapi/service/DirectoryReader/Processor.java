package hr.neos.directorybrowserapi.service.DirectoryReader;

import java.util.List;
import java.util.concurrent.Future;

public interface Processor {
	void start();

	default void waitUntilDone() {
		getPendingTasks().forEach(task -> {
			try {
				task.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	void stop();

	void stopWhenDone();

	List<Future> getPendingTasks();
}
