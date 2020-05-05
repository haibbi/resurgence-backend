package tr.com.milia.resurgence.family;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	List<Announcement> findAllByFamilyOrderByTimeDesc(Family family);

}
