package tr.com.milia.resurgence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileUtilsTest {

	@Test
	void pngFileTest() {
		MockMultipartFile file = new MockMultipartFile("name",
			"original",
			"image/png",
			new byte[]{0, 1});

		String filename = FileUtils.addExtension(file, "test");
		Assertions.assertEquals("test.png", filename);
	}
}
