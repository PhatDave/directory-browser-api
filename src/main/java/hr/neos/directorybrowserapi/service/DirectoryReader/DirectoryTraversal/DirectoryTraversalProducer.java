package hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryTraversal;

import hr.neos.directorybrowserapi.service.DirectoryReader.Processor;
import hr.neos.directorybrowserapi.service.DirectoryReader.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.*;

@AllArgsConstructor
@Getter
@Setter
public class DirectoryTraversalProducer implements Processor {
	private final Stack<Path> stack;
	@Getter
	private final ThreadPoolExecutor executor;
	public Path path;
	private Integer threads;
	private Integer queueSize;
	private BlockingQueue<Runnable> executorQueue;
	@Getter
	private List<Future> pendingTasks = new ArrayList<>();
	private Integer maxDepth;

	public DirectoryTraversalProducer(Stack<Path> stack, Integer maxDepth, Path path, Integer threads, Integer queueSize) {
		this.stack = stack;
		this.maxDepth = maxDepth;
		this.path = path;
		this.threads = threads;
		this.queueSize = queueSize;
//		logger.info("Created with maxDepth: " + maxDepth + " and path: " + path);

		executorQueue = new ArrayBlockingQueue<>(queueSize);
		this.executor = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, executorQueue);
	}

	@Override
	public void start() {
//		logger.info("Started");
		if (path == null) {
//			logger.severe("Path is null");
			throw new RuntimeException("Path is null");
		}
		if (Files.isReadable(path)) {
			List<Task> tasks = new ArrayList<>();

			try {
				Files.list(path)
				     .forEach(p -> {
					     if (Files.isDirectory(p)) {
//						     logger.info("Added task for directory: " + p);
						     tasks.add(new DirectoryTraversalProducerTask(p, maxDepth, stack));
					     } else {
//						     logger.info("File to stack: " + p);
						     stack.push(p);
					     }
				     });
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
//			logger.info("Running tasks, size: " + tasks.size());
			// This weird hack is here because when removing elements from tasks directly they are being (sometimes)
			// Added back as Future objects
			tasks.forEach(run -> pendingTasks.add(executor.submit(run)));
		}
	}

	@Override
	public void stop() {
//		logger.warning("Stop called, method empty");
	}

	@Override
	public void stopWhenDone() {
//		logger.warning("StopWhenDone called, method empty");
	}
}
