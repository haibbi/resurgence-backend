package tr.com.milia.resurgence;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "default")
public class DefaultKeyValueProperties {

	private String familyImage;

	public String getFamilyImage() {
		return familyImage;
	}

	public void setFamilyImage(String familyImage) {
		this.familyImage = familyImage;
	}
}
