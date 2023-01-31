package hr.neos.directorybrowserapi.service.DirectoryReader.Entity;

import lombok.*;

import java.nio.file.Path;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class File {
	private Path path;
	private String fileName;
	private Long accessTime = 0L;
	private Long creationTime = 0L;
	private Long modificationTime = 0L;
	private Long size = 0L;
	private Boolean isDirectory = false;
}
