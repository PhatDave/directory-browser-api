package main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie;

import lombok.AllArgsConstructor;

import java.nio.file.Path;

@AllArgsConstructor
public class Trie {
	private main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode root;

	public void insert(Path path) {
		main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode current = root;
		String pathString = path.toString();
		pathString += "\\";
		String reassembledPath = "";

		for (int i = 0; i < pathString.length(); i++) {
			char ch = pathString.charAt(i);
			reassembledPath += ch;

			main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode node = current.getChildren()
			                                                                                                 .get(ch);
			if (node == null) {
				node = new main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode();
				current.getChildren()
				       .put(ch, node);
				node.setParent(current);
				node.setPath(reassembledPath);
			}
			current = node;
		}
		current.setLeaf(true);
	}

	public Path search(Path path) {
		main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode current = root;
		String pathString = path.toString();
		pathString += "\\";

		for (int i = 0; i < pathString.length(); i++) {
			char ch = pathString.charAt(i);

			main.java.hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Trie.TrieNode node = current.getChildren()
			                                                                                                 .get(ch);
			if (node == null) {
				return null;
			}
			current = node;
			if (current.isLeaf()) {
				return current.getCompiledPath();
			}
		}
		return current.getCompiledPath();
	}
}
