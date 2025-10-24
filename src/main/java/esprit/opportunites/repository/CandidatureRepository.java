package esprit.opportunites.repository;

import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.Candidature;
import esprit.opportunites.domain.Opportunite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Candidature entity.
 */
@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    // 🔍 Récupérer toutes les candidatures d’un candidat
    List<Candidature> findByCandidatId(Long candidatId);

    // 🔍 Récupérer toutes les candidatures pour une opportunité
    List<Candidature> findByOpportuniteId(Long opportuniteId);

    // 🔍 Vérifier si un candidat a déjà postulé à une opportunité
    boolean existsByCandidatAndOpportunite(Candidat candidat, Opportunite opportunite);
}
