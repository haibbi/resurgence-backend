package tr.com.milia.resurgence.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;

public interface BankTransferLogRepository extends JpaRepository<BankTransferLog, Long> {

	List<BankTransferLog> findTop5ByFromOrderByTimeDesc(Player from);

	List<BankTransferLog> findTop5ByToOrderByTimeDesc(Player to);

}
