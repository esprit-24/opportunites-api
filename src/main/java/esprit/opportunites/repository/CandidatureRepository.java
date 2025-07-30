package esprit.opportunites.repository;

import esprit.opportunites.domain.Candidature;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Candidature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {}
