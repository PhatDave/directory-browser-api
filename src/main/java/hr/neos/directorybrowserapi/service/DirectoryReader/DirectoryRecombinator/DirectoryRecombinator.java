package hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryRecombinator;


import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Directory;
import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.File;
import hr.neos.directorybrowserapi.service.DirectoryReader.Processor;
import hr.neos.directorybrowserapi.service.DirectoryReader.Task;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class DirectoryRecombinator implements Processor {
	private final Stack<File> inputStack;
	private final ConcurrentHashMap<Path, Directory> outputMap;
	private final Integer threads;
	@Getter
	private final ThreadPoolExecutor executor;
	private final Path root;
	@Getter
	private final List<Future> pendingTasks = new ArrayList<>();
	private List<Task> tasks = null;

	public DirectoryRecombinator(Stack<File> inputStack, ConcurrentHashMap<Path, Directory> outputMap, Integer threads, Path root) {
		this.inputStack = inputStack;
		this.outputMap = outputMap;
		this.threads = threads;
		this.root = root;
		this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
//		logger.info("Created with threads: " + threads + " and root: " + root);

		try {
			Files.list(root)
			     .forEach(path -> {
				     Directory directory = new Directory();
				     directory.setIsDirectory(Files.isDirectory(path));
				     directory.setPath(path);
				     directory.setFileName(path.getFileName().toString());

				     outputMap.put(path, directory);
			     });
//			logger.info("Created outputMap with size " + outputMap.size());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		logger.info("Created");
	}

	public void start() {
//		logger.info("Started");
		main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.Trie directories = new main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.Trie(new main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode());
		outputMap.forEach((path, directory) -> {
			if (directory.getIsDirectory()) {
				directories.insert(path);
			}
		});
//		logger.info("Created directories trie");

		tasks = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			tasks.add(new DirectoryRecombinatorTask(inputStack, outputMap, directories, root));
		}
//		logger.info("Executing " + tasks.size() + " tasks");
		tasks.forEach(task -> {
			pendingTasks.add(executor.submit(task));
		});
	}

	public void stop() {
//		logger.info("Stopping");
		tasks.forEach(task -> task.setRun(false));
	}

	public void stopWhenDone() {
//		logger.info("Stopping when done");
		tasks.forEach(task -> task.setStopDemanded(true));
	}
}
