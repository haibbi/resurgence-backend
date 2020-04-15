package tr.com.milia.resurgence;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class TempController {

	@GetMapping("/")
	public String root() {
		return "Hi!";
	}

	@GetMapping("/user")
	public Mono<String> greetUser() {
		return ReactiveSecurityContextHolder
			.getContext()
			.map(SecurityContext::getAuthentication)
			.map(Principal::getName)
			.map(name -> String.format("Hello, %s", name));
	}

	@PreAuthorize("hasRole('YOKBOYLEBIROL')")
	@GetMapping("/denied")
	public Mono<String> denied() {
		return Mono.just("You shall not pass!");
	}

}
