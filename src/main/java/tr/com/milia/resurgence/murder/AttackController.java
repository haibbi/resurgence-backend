package tr.com.milia.resurgence.murder;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;

@RestController
@RequestMapping("/attack")
public class AttackController {

	private final AttackService service;

	public AttackController(AttackService service) {
		this.service = service;
	}

	@PostMapping("/{victim}/{bullet}")
	public AttackResult attack(TokenAuthentication authentication,
							   @PathVariable("victim") String victim,
							   @PathVariable("bullet") long bullet) {
		String attacker = authentication.getPlayerName();
		return service.attack(attacker, victim, bullet);
	}

}
