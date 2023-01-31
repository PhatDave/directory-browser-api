package hr.neos.directorybrowserapi.service.DirectoryReader.Entity;

import lombok.*;

import java.nio.file.Path;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Directory {
	private Path path;
	private String fileName;
	private Long earliestAccessTime = Long.MAX_VALUE;
	private Long latestAccessTime = 0L;
	private Long earliestCreationTime = Long.MAX_VALUE;
	private Long latestCreationTime = 0L;
	private Long earliestModificationTime = Long.MAX_VALUE;
	private Long latestModificationTime = 0L;
	private Long size = 0L;
	private Boolean isDirectory = true;
	private Long fileCount = 0L;
	private String type;
	private String iconColor;
}
