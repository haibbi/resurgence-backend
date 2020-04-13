package tr.com.milia.resurgence.account;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

	private final Repository repository;

	public AccountService(Repository repository) {
		this.repository = repository;
	}

	public Optional<Account> findByEmail(String email) {
		return repository.findAccountByEmail(email);
	}
}
