package tr.com.milia.resurgence.family;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/family/bank")
public class FamilyBankController {

	private final FamilyService service;
	private final FamilyBankLogService logService;

	public FamilyBankController(FamilyService service, FamilyBankLogService logService) {
		this.service = service;
		this.logService = logService;
	}

	@PostMapping("/{operation:deposit|withdraw}/{amount}")
	public void deposit(TokenAuthentication authentication,
						@PathVariable("operation") String operation,
						@PathVariable("amount") long amount) {
		String playerName = authentication.getPlayerName();
		if ("deposit".equals(operation)) {
			service.deposit(playerName, amount);
		} else if ("withdraw".equals(operation)) {
			service.withdraw(playerName, amount);
		}
	}

	@GetMapping("log")
	public List<FamilyBankLogResponse> logs(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		return logService.findAllLogs(player).stream()
			.map(FamilyBankLogResponse::new)
			.collect(Collectors.toList());
	}

}
