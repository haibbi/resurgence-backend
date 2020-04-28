package tr.com.milia.resurgence.family;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyBankLogRepository extends JpaRepository<FamilyBankLog, Long> {
	List<FamilyBankLog> findAllByOrderByDateDesc();
}
