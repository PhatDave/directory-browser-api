package hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryTraversal;

import hr.neos.directorybrowserapi.service.DirectoryReader.Task;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Stack;

@AllArgsConstructor
public class DirectoryTraversalProducerTask implements Task {
	private final Path path;
	private final Integer maxDepth;
	private final Stack<Path> stack;

	@Override
	public void run() {
//		logger.info("Started for path " + path);
		try {
			Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
//					logger.info("Adding file to stack: " + path);
					stack.push(path);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path path, IOException exc) {
//					logger.warning("Failed to visit path: " + path);
					return FileVisitResult.SKIP_SUBTREE;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setRun(boolean run) {
	}

	@Override
	public void setStopDemanded(boolean stopDemanded) {
	}
}
