package hr.neos.directorybrowserapi.service.DirectoryReader.FileInformation;

import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.File;
import hr.neos.directorybrowserapi.service.DirectoryReader.Processor;
import hr.neos.directorybrowserapi.service.DirectoryReader.Task;
import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FileInformationProcessor implements Processor {
	private final Stack<Path> inputStack;
	private final Stack<File> outputStack;
	private final Integer threads;
	@Getter
	private final ThreadPoolExecutor executor;
	@Getter
	private final List<Future> pendingTasks = new ArrayList<>();
	private List<Task> tasks = null;

	public FileInformationProcessor(Stack<Path> inputStack, Stack<File> outputStack, Integer threads) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.threads = threads;
		this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
//		logger.info("Created with " + threads + " threads");
	}

	public void start() {
//		logger.info("Started");
		tasks = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			tasks.add(new FileInformationProcessorTask(inputStack, outputStack));
		}
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
