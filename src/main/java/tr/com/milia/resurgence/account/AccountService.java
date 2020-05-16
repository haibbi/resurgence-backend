package tr.com.milia.resurgence.account;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tr.com.milia.resurgence.RandomUtils;

import java.util.Optional;

@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<Account> findByEmail(String email) {
		return accountRepository.findAccountByEmail(email);
	}

	public Account create(String email, String password) throws EmailAlreadyExistException {
		Optional<Account> optionalAccount = findByEmail(email);

		if (optionalAccount.isPresent()) throw new EmailAlreadyExistException(email);

		Account account = new Account(email, passwordEncoder.encode(password), Status.VERIFIED);

		return accountRepository.save(account);
	}

	public void oauth2Register(String email) {
		if (findByEmail(email).isPresent()) return;

		String plainTextPassword = RandomUtils.randomAlphaNumeric(16);
		String password = passwordEncoder.encode(plainTextPassword);
		Account account = new Account(email, password, Status.VERIFIED);
		accountRepository.save(account);
	}

}
