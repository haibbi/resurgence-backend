package tr.com.milia.resurgence;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	private FileUtils() {
	}

	public static String addExtension(MultipartFile file, String filename) {
		if (file.isEmpty()) throw new IllegalArgumentException();

		String contentType = file.getContentType();
		if (contentType == null) throw new NullPointerException();

		MediaType mediaType = MediaType.parseMediaType(contentType);
		return filename + "." + mediaType.getSubtype();
	}
}
