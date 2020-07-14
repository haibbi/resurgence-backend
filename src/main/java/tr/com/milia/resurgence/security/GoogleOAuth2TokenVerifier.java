package tr.com.milia.resurgence.security;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
public class GoogleOAuth2TokenVerifier implements OAuth2TokenVerifier {

	private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
	private static final String PROVIDER_NAME = "google";
	private static final Collection<OAuth2UserAuthority> DEFAULT_AUTHORITIES =
		Collections.singleton(new OAuth2UserAuthority(Collections.singletonMap("PROVIDER", PROVIDER_NAME)));

	private final ClientRegistrationRepository registrationRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	public GoogleOAuth2TokenVerifier(ClientRegistrationRepository registrationRepository) {
		this.registrationRepository = registrationRepository;
	}

	@Override
	public OAuth2User verify(OAuth2Request request) {
		ClientRegistration registration = registrationRepository.findByRegistrationId(name());

		var userInfoEndpointUri = registration.getProviderDetails().getUserInfoEndpoint().getUri();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + request.token);
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(userInfoEndpointUri,
			HttpMethod.GET,
			entity,
			new ParameterizedTypeReference<>() {
			}
		);

		// todo refactor this exception usage
		//  remove hard coded texts
		if (response.getStatusCode().isError()) throw new OAuth2AuthenticationException(
			new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
				"Response Code: " + response.getStatusCode(),
				userInfoEndpointUri)
		);

		Map<String, Object> body = response.getBody();
		if (body == null) {
			throw new OAuth2AuthenticationException(
				new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
					"Empty response body",
					userInfoEndpointUri));
		}

		return new DefaultOAuth2User(DEFAULT_AUTHORITIES, body, "email");
	}

	@Override
	public String name() {
		return PROVIDER_NAME;
	}
}
