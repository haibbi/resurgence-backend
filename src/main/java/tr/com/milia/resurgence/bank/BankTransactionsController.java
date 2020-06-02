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
	private final BankTransferLogService transferLogService;

	public BankTransactionsController(BankAccountLogService bankAccountLogService,
									  BankTransferLogService transferLogService) {
		this.bankAccountLogService = bankAccountLogService;
		this.transferLogService = transferLogService;
	}

	@GetMapping("/account")
	public List<BankAccountLogResponse> account(TokenAuthentication authentication) {
		String owner = authentication.getPlayerName();
		return bankAccountLogService.logs(owner).stream()
			.map(BankAccountLogResponse::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/transfer")
	public List<BankTransferLogResponse> transfers(TokenAuthentication authentication) {
		String player = authentication.getPlayerName();
		return transferLogService.findAll(player).stream()
			.map(log -> {
				if (player.equals(log.getTo().getName()))
					return BankTransferLogResponse.in(log);
				else
					return BankTransferLogResponse.out(log);
			})
			.collect(Collectors.toList());
	}

}
