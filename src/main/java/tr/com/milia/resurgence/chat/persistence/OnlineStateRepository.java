package tr.com.milia.resurgence.chat.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OnlineStateRepository extends JpaRepository<OnlineState, OnlineStateId> {

	@Query(value = """
		select distinct on (name) name, online, time
		from online_state
		order by name, time desc
		""", nativeQuery = true)
	List<OnlineState> findTopByName();

}
