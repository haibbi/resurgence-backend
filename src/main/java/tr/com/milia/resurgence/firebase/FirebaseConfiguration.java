package tr.com.milia.resurgence.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@ConfigurationProperties("resurgence.firebase")
public class FirebaseConfiguration {

	private String databaseUrl;
	private String storageBucket;
	private String credentialFile;

	@Bean(destroyMethod = "delete")
	FirebaseApp firebaseApp() throws IOException {
		var file = new File(credentialFile);
		var inputStream = new FileInputStream(file);

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(inputStream))
			.setDatabaseUrl(databaseUrl)
			.setStorageBucket(storageBucket)
			.build();

		return FirebaseApp.initializeApp(options);
	}

	@Bean
	FirebaseService firebaseService(FirebaseApp app) {
		return new FirebaseService(this, app);
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getStorageBucket() {
		return storageBucket;
	}

	public void setStorageBucket(String storageBucket) {
		this.storageBucket = storageBucket;
	}

	public String getCredentialFile() {
		return credentialFile;
	}

	public void setCredentialFile(String credentialFile) {
		this.credentialFile = credentialFile;
	}

}
