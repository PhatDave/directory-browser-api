package main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Getter
@Setter
public class TrieNode {
	private Map<Character, TrieNode> children;
	private boolean isLeaf;
	private Path originalPath;
	private String path;
	private Path compiledPath;
	private TrieNode parent;

	public TrieNode() {
		children = new HashMap<>();
		isLeaf = false;
		originalPath = null;
		path = null;
		parent = null;
	}

	public Path getCompiledPath() {
		if (compiledPath == null) {
			compiledPath = Path.of(path);
		}
		return compiledPath;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", TrieNode.class.getSimpleName() + "[", "]")
				.add("children=" + children)
				.add("isLeaf=" + isLeaf)
				.add("originalPath=" + originalPath)
				.toString();
	}
}
