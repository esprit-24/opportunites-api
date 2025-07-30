package esprit.opportunites.repository;

import esprit.opportunites.domain.Opportunite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Opportunite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpportuniteRepository extends JpaRepository<Opportunite, Long> {}
