package tr.com.milia.resurgence.murder;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.time.Instant;
import java.util.List;

public interface ResearchResultRepository extends JpaRepository<ResearchResult, Long> {

	List<ResearchResult> findAllBySeekerAndExpireTimeGreaterThanEqualOrderByExpireTime(Player seeker, Instant expireTime);

}
