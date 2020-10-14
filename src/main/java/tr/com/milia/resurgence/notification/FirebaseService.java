package tr.com.milia.resurgence.notification;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FirebaseService {

	public static final Map<String, String> REQUIRED_MESSAGE_DATA = Map.of(
		"click_action", "FLUTTER_NOTIFICATION_CLICK"
	);

	private final FirebaseConfiguration configuration;
	private final StorageClient storageClient;
	private final FirebaseMessaging firebaseMessaging;

	public FirebaseService(FirebaseConfiguration configuration, FirebaseApp app) {
		this.configuration = configuration;
		storageClient = StorageClient.getInstance(app);
		firebaseMessaging = FirebaseMessaging.getInstance(app);
	}

	public String uploadFile(MultipartFile file, String filename) throws IOException {
		var stream = file.getInputStream();
		var contentType = file.getContentType();

		var bucket = storageClient.bucket();
		bucket.create(filename, stream, contentType);

		String filepath = URLEncoder.encode(filename, StandardCharsets.UTF_8);

		return UriComponentsBuilder.newInstance()
			.scheme("https")
			.host("firebasestorage.googleapis.com")
			.pathSegment("v0", "b", configuration.getStorageBucket(), "o", filepath)
			.queryParam("alt", "media")
			.build().toUriString();
	}

	public String sendSimpleNotificationToUser(String token, String title, String body)
		throws FirebaseMessagingException {
		var notification = Notification.builder().setTitle(title).setBody(body).build();
		var message = Message.builder()
			.setToken(token)
			.setNotification(notification)
			.putAllData(FirebaseService.REQUIRED_MESSAGE_DATA)
			.build();
		return firebaseMessaging.send(message);
	}
}
