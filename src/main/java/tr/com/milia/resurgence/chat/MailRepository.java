package tr.com.milia.resurgence.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {

	List<Mail> findAllByFrom_Name(String from);

	List<Mail> findAllByTo_Name(String to);

}
