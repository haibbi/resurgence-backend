package tr.com.milia.resurgence.bank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank/transactions")
public class BankTransactionsController {

	private final BankAccountLogService bankAccountLogService;

	public BankTransactionsController(BankAccountLogService bankAccountLogService) {
		this.bankAccountLogService = bankAccountLogService;
	}

	@GetMapping("/account")
	public List<BankAccountLogResponse> transactions(TokenAuthentication authentication) {
		String owner = authentication.getPlayerName();
		return bankAccountLogService.logs(owner).stream()
			.map(BankAccountLogResponse::new)
			.collect(Collectors.toList());
	}

}
