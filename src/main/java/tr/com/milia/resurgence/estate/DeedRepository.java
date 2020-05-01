package tr.com.milia.resurgence.estate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeedRepository extends JpaRepository<Deed, Building> {
}
