package hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryRecombinator;

import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Directory;
import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.File;
import hr.neos.directorybrowserapi.service.DirectoryReader.Task;

import java.nio.file.Path;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class DirectoryRecombinatorTask implements Task {
	private final Stack<File> inputStack;
	private final ConcurrentHashMap<Path, Directory> outputMap;
	private final main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.Trie directories;
	public volatile boolean run = true;
	public volatile boolean stopDemanded = false;

	public DirectoryRecombinatorTask(Stack<File> inputStack, ConcurrentHashMap<Path, Directory> outputMap, main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.Trie directories, Path root) {
		this.inputStack = inputStack;
		this.outputMap = outputMap;
		this.directories = directories;
//		logger.info("Created");
	}

	private static void merge(File file, Directory directory) {
//		logger.info("Merging " + file + " into " + directory.getPath());
		synchronized (directory) {
			directory.setSize(directory.getSize() + file.getSize());
//			logger.info("Directory new size: " + directory.getSize());
			directory.setFileCount(directory.getFileCount() + 1);
//			logger.info("Directory new file count: " + directory.getFileCount());

			Long creationTime = file.getCreationTime();
			Long accessTime = file.getAccessTime();
			Long modificationTime = file.getModificationTime();

			if (creationTime < directory.getEarliestCreationTime()) {
				directory.setEarliestCreationTime(creationTime);
//				logger.info("Directory new earliest creation time: " + directory.getEarliestCreationTime());
			}
			if (accessTime < directory.getEarliestAccessTime()) {
				directory.setEarliestAccessTime(accessTime);
//				logger.info("Directory new earliest access time: " + directory.getEarliestAccessTime());
			}
			if (modificationTime < directory.getEarliestModificationTime()) {
				directory.setEarliestModificationTime(modificationTime);
//				logger.info("Directory new earliest modification time: " + directory.getEarliestModificationTime());
			}

			if (creationTime > directory.getLatestCreationTime()) {
				directory.setLatestCreationTime(creationTime);
//				logger.info("Directory new latest creation time: " + directory.getLatestCreationTime());
			}
			if (accessTime > directory.getLatestAccessTime()) {
				directory.setLatestAccessTime(accessTime);
//				logger.info("Directory new latest access time: " + directory.getLatestAccessTime());
			}
			if (modificationTime > directory.getLatestModificationTime()) {
				directory.setLatestModificationTime(modificationTime);
//				logger.info("Directory new latest modification time: " + directory.getLatestModificationTime());
			}
		}
	}

	@Override
	public void run() {
//		logger.info("Started");
		while (run) {
			if (!inputStack.isEmpty()) {
				File file;
				try {
					file = inputStack.pop();
//					logger.info("Processing file: " + file);

					Path path = directories.search(file.getPath());
					if (path == null) {
						path = file.getPath();
//						logger.info("File is not directory, path is: " + path);
					}

					Directory directory = outputMap.get(path);
					if (directory != null) {
						merge(file, directory);
					}
				} catch (EmptyStackException e) {
//					logger.warning("EmptyStackException");
					continue;
				}
			}

			if (stopDemanded && inputStack.isEmpty()) {
				run = false;
			}
		}
	}

	@Override
	public void setRun(boolean run) {
		this.run = run;
	}

	@Override
	public void setStopDemanded(boolean stopDemanded) {
		this.stopDemanded = stopDemanded;
	}
}
