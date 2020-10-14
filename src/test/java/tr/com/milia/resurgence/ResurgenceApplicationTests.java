package tr.com.milia.resurgence;

import com.google.firebase.FirebaseApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tr.com.milia.resurgence.notification.FirebaseConfiguration;
import tr.com.milia.resurgence.notification.FirebaseService;

@SpringBootTest
class ResurgenceApplicationTests {

	@MockBean
	private FirebaseConfiguration firebaseConfiguration;
	@MockBean
	private FirebaseApp firebaseApp;
	@MockBean
	private FirebaseService firebaseService;

	@Test
	void contextLoads() {
	}

}
