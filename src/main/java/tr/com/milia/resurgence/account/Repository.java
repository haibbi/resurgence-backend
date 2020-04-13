package tr.com.milia.resurgence.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository extends JpaRepository<Account, Long> {
	Optional<Account> findAccountByEmail(String email);
}
