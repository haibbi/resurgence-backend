package tr.com.milia.resurgence.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;

public interface BankAccountLogRepository extends JpaRepository<BankAccountLog, Long> {

	List<BankAccountLog> findTop5ByOwnerOrderByTimeDesc(Player owner);

}
