package hr.neos.directorybrowserapi.service.DirectoryReader;

import hr.neos.directorybrowserapi.dto.DirectoryDto;
import hr.neos.directorybrowserapi.mapper.DirectoryMapper;
import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Directory;
import hr.neos.directorybrowserapi.service.FileTypeResolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryReaderService {
	private final DirectoryMapper directoryMapper;
	private final FileTypeResolverService fileTypeResolverService;

	public static Integer getMaxRecursion() {
		return DirectoryReader.MAX_RECURSION;
	}

	public static void setMaxRecursion(Integer maxRecursion) {
		DirectoryReader.MAX_RECURSION = maxRecursion;
	}

	public List<Directory> scanPath(String dir) {
		Path path = Path.of(dir);

		List<Directory> directories = DirectoryReader.parse(path);
		directories.forEach(directory -> {
			directory.setType(fileTypeResolverService.resolveType(directory.getPath()));
			directory.setIconColor(fileTypeResolverService.resolveColor(directory.getPath()));
		});
		return directories;
	}

	public List<DirectoryDto> scanPathDto(String dir) {
		List<Directory> directories = scanPath(dir);
		return directoryMapper.toDtos(directories);
	}
}
