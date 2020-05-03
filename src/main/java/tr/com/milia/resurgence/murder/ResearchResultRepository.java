package tr.com.milia.resurgence.murder;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.milia.resurgence.player.Player;

import java.util.List;

public interface ResearchResultRepository extends JpaRepository<ResearchResult, Long> {

	List<ResearchResult> findAllBySeeker(Player seeker);

}
