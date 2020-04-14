package tr.com.milia.resurgence.account;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Optional<Account> findByEmail(String email) {
		return accountRepository.findAccountByEmail(email);
	}

	public Account create(Account account) throws EmailAlreadyExistException {
		Optional<Account> optionalAccount = findByEmail(account.getEmail());

		if (optionalAccount.isPresent()) throw new EmailAlreadyExistException(account.getEmail());

		return accountRepository.save(account);
	}
}
