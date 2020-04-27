package tr.com.milia.resurgence.gambling;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface LotteryRepository extends JpaRepository<LotteryTicket, Long> {

	List<LotteryTicket> findAllByPurchaseTimeBefore(Instant purchaseTime);

}
