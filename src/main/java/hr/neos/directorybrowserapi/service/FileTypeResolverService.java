package hr.neos.directorybrowserapi.service;

import com.google.common.io.Files;
import hr.neos.directorybrowserapi.repository.FileTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileTypeResolverService {
	private final FileTypeRepository fileTypeRepository;

	public String resolveType(Path path) {
		Map<String, String> type;
		if (path.toFile().isDirectory()) {
			type = (Map<String, String>) fileTypeRepository.getTypeFor("directory");
		} else {
			String fileName = path.getFileName().toString();
			String fileExtension = Files.getFileExtension(fileName);
			type = (Map<String, String>) fileTypeRepository.getTypeFor(fileExtension);
		}
		return type.get("name");
	}

	public String resolveColor(Path path) {
		Map<String, String> type;
		if (path.toFile().isDirectory()) {
			type = (Map<String, String>) fileTypeRepository.getTypeFor("directory");
		} else {
			String fileName = path.getFileName().toString();
			String fileExtension = Files.getFileExtension(fileName);
			type = (Map<String, String>) fileTypeRepository.getTypeFor(fileExtension);
		}
		return type.get("color");
	}

	public String resolveIcon(String type) {
		String icon = fileTypeRepository.getIconForType(type);
		return icon;

	}
}
