package tr.com.milia.resurgence.security;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2TokenVerifier {

	OAuth2User verify(OAuth2Request request);

	String name();

}
