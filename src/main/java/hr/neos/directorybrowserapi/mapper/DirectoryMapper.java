package hr.neos.directorybrowserapi.mapper;

import hr.neos.directorybrowserapi.dto.DirectoryDto;
import hr.neos.directorybrowserapi.service.DirectoryReader.Entity.Directory;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface DirectoryMapper {
	@Mapping(target = "name", source = "fileName")
	@Mapping(target = "newestAccess", source = "latestAccessTime")
	@Mapping(target = "oldestAccess", source = "earliestAccessTime")
	@Mapping(target = "newestModify", source = "latestModificationTime")
	@Mapping(target = "oldestModify", source = "earliestModificationTime")
	@Mapping(target = "newestCreate", source = "latestCreationTime")
	@Mapping(target = "oldestCreate", source = "earliestCreationTime")
	DirectoryDto toDto(Directory directory);

	List<DirectoryDto> toDtos(List<Directory> directories);
}
