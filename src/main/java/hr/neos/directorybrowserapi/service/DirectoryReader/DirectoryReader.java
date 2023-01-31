package hr.neos.directorybrowserapi.service.DirectoryReader;

import hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryRecombinator.DirectoryRecombinator;
import hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryTraversal.DirectoryTraversalProducer;
import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Directory;
import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.File;
import hr.neos.directorybrowserapi.service.DirectoryReader.FileInformation.FileInformationProcessor;

import java.nio.file.Path;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class DirectoryReader {
	public static Integer MAX_RECURSION = 3;
	public static Integer DIRECTORY_TRAVERSAL_TASK_QUEUE_SIZE = 1000;
	public static Integer DIRECTORY_TRAVERSAL_THREADS = 8;
	public static Integer INFO_PARSER_THREADS = 16;
	public static Integer DIRECTORY_RECOMBINATOR_THREADS = 16;

	public static List<Directory> parse(Path path) {
		Stack<Path> pathStack = new Stack<>();
		Processor directoryReader = new DirectoryTraversalProducer(pathStack, MAX_RECURSION, path, DIRECTORY_TRAVERSAL_THREADS, DIRECTORY_TRAVERSAL_TASK_QUEUE_SIZE);

		Stack<File> fileStack = new Stack<>();
		Processor fileInformationProcessor = new FileInformationProcessor(pathStack, fileStack, INFO_PARSER_THREADS);

		ConcurrentHashMap<Path, Directory> recombinedDirectoryMap = new ConcurrentHashMap<>();
		Processor directoryRecombinator = new DirectoryRecombinator(fileStack, recombinedDirectoryMap, DIRECTORY_RECOMBINATOR_THREADS, path);

		directoryReader.start();
		fileInformationProcessor.start();
		directoryRecombinator.start();

		directoryReader.waitUntilDone();
		fileInformationProcessor.stopWhenDone();
		fileInformationProcessor.waitUntilDone();
		directoryRecombinator.stopWhenDone();
		directoryRecombinator.waitUntilDone();

		return List.copyOf(recombinedDirectoryMap.values());
	}
}
