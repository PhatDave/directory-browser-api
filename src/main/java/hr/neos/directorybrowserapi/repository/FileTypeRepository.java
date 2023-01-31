package hr.neos.directorybrowserapi.repository;

import com.google.gson.Gson;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class FileTypeRepository {
	private final String FILE_TYPES_FILE = "file-types.json";
	private final String TYPE_ICONS_FILE = "type-icons.json";
	private Map<String, Object> fileTypes;
	private Map<String, String> typeIcons;

	public FileTypeRepository() {
		try {
			Resource resource = new ClassPathResource("static/" + FILE_TYPES_FILE);
			byte[] bytes = resource.getInputStream().readAllBytes();
			fileTypes = new Gson().fromJson(new String(bytes), Map.class);

			resource = new ClassPathResource("static/" + TYPE_ICONS_FILE);
			bytes = resource.getInputStream().readAllBytes();
			typeIcons = new Gson().fromJson(new String(bytes), Map.class);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public Object getTypeFor(String type) {
		Object obj = fileTypes.get(type);
		if (obj == null) {
			return fileTypes.get("default");
		}
		return obj;
	}

	public String getIconForType(String type) {
		String obj = typeIcons.get(type);
		if (obj == null) {
			return typeIcons.get("file_type_default");
		}
		return obj;
	}
}
