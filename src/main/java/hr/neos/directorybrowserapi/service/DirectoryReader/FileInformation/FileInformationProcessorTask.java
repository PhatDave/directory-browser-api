package hr.neos.directorybrowserapi.service.DirectoryReader.FileInformation;

import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.File;
import hr.neos.directorybrowserapi.service.DirectoryReader.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EmptyStackException;
import java.util.Stack;

public class FileInformationProcessorTask implements Task {
	private final Stack<Path> inputStack;
	private final Stack<File> outputStack;
	public volatile boolean run = true;
	public volatile boolean stopDemanded = false;

	public FileInformationProcessorTask(Stack<Path> inputStack, Stack<File> outputStack) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
//		logger.info("Created");
	}

	private static File getFileInfo(Path element) {
//		logger.info("Processing " + element.toString());
		File file = new File();
		try {
			file.setPath(element);
			file.setFileName(element.getFileName().toString());

			BasicFileAttributes attributes = Files.readAttributes(element, BasicFileAttributes.class);

			file.setSize(attributes.size());
			file.setCreationTime(attributes.creationTime().toMillis());
			file.setAccessTime(attributes.lastAccessTime().toMillis());
			file.setModificationTime(attributes.lastModifiedTime().toMillis());
			file.setIsDirectory(attributes.isDirectory());
//			logger.info("Got file attributes");
		} catch (IOException e) {
//			logger.warning("Error while getting file attributes");
		}
		return file;
	}

	@Override
	public void run() {
//		logger.info("Started");
		while (run) {
			if (!inputStack.isEmpty()) {
				Path element;
				try {
					element = inputStack.pop();
				} catch (EmptyStackException e) {
//					logger.warning("EmptyStackException");
					continue;
				}
//				logger.info("Processing file: " + element);

				File file = getFileInfo(element);
//				logger.info("File info: " + file);
				outputStack.push(file);
			}

			if (stopDemanded && inputStack.isEmpty()) {
				run = false;
			}
		}
	}

	@Override
	public void setRun(boolean run) {
//		logger.info("setRun(" + run + ")");
		this.run = run;
	}

	@Override
	public void setStopDemanded(boolean stopDemanded) {
//		logger.info("setStopDemanded(" + stopDemanded + ")");
		this.stopDemanded = stopDemanded;
	}
}
