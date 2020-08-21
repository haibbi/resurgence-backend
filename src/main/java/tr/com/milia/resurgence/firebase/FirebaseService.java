package tr.com.milia.resurgence.firebase;

import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseService {

	private final FirebaseConfiguration configuration;

	public FirebaseService(FirebaseConfiguration configuration) {
		this.configuration = configuration;
	}

	public String uploadFile(MultipartFile file, String filename) throws IOException {
		var stream = file.getInputStream();
		var contentType = file.getContentType();

		var bucket = StorageClient.getInstance().bucket();
		bucket.create(filename, stream, contentType);

		String filepath = URLEncoder.encode(filename, StandardCharsets.UTF_8);

		return UriComponentsBuilder.newInstance()
			.scheme("https")
			.host("firebasestorage.googleapis.com")
			.pathSegment("v0", "b", configuration.getStorageBucket(), "o", filepath)
			.queryParam("alt", "media")
			.build().toUriString();
	}

}
