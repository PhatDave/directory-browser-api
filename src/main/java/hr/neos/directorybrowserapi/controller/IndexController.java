package hr.neos.directorybrowserapi.controller;

import hr.neos.directorybrowserapi.service.DirectoryReader.DirectoryReaderService;
import hr.neos.directorybrowserapi.service.FileTypeResolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {
	private final DirectoryReaderService directoryReaderService;
	private final FileTypeResolverService fileTypeResolverService;

	@GetMapping("/dir")
	public ResponseEntity<?> getDir(@RequestParam("dir") String dir) {
		return ResponseEntity.ok(directoryReaderService.scanPathDto(dir));
	}

	@GetMapping("/icon")
	public ResponseEntity<?> getIcon(@RequestParam("fileType") String fileType) {
		return ResponseEntity.ok(fileTypeResolverService.resolveIcon(fileType));
	}

	@GetMapping("/depth")
	public ResponseEntity<?> getRecursionDepth () {
		return ResponseEntity.ok(DirectoryReaderService.getMaxRecursion());
	}

	@PostMapping("/depth")
	public ResponseEntity<?> setRecursionDepth (@RequestParam("depth") String depth) {
		DirectoryReaderService.setMaxRecursion(Integer.valueOf(depth));
		return ResponseEntity.ok("");
	}
}
