package hr.neos.directorybrowserapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectoryDto {
	private Boolean isDirectory;
	private Long fileCount;
	private String type;
	private String name;
	private String iconColor;
	private Long size;
	private Long oldestAccess;
	private Long newestAccess;
	private Long oldestModify;
	private Long newestModify;
	private Long oldestCreate;
	private Long newestCreate;
}
