package esprit.opportunites.repository;

import esprit.opportunites.domain.Domaine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Domaine entity.
 */
@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long> {
    /**
     * Find domaine by intitule (case insensitive)
     */
    Optional<Domaine> findByIntituleIgnoreCase(String intitule);

    /**
     * Find domaines by intitule containing text (case insensitive)
     */
    List<Domaine> findByIntituleContainingIgnoreCase(String intitule);

    /**
     * Find domaines by description containing text (case insensitive)
     */
    List<Domaine> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Search domaines by intitule or description containing text
     */
    @Query(
        "SELECT d FROM Domaine d WHERE " +
        "LOWER(d.intitule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
    )
    List<Domaine> searchByIntituleOrDescription(@Param("searchTerm") String searchTerm);

    /**
     * Find all domaines ordered by intitule
     */
    List<Domaine> findAllByOrderByIntituleAsc();

    /**
     * Check if domaine exists by intitule (case insensitive)
     */
    boolean existsByIntituleIgnoreCase(String intitule);

    /**
     * Count domaines containing text in intitule or description
     */
    @Query(
        "SELECT COUNT(d) FROM Domaine d WHERE " +
        "LOWER(d.intitule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
    )
    long countByIntituleOrDescriptionContaining(@Param("searchTerm") String searchTerm);

    /**
     * Find domaines with pagination and search
     */
    @Query(
        "SELECT d FROM Domaine d WHERE " +
        "(:searchTerm IS NULL OR :searchTerm = '' OR " +
        "LOWER(d.intitule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
        "ORDER BY d.intitule ASC"
    )
    List<Domaine> findBySearchTerm(@Param("searchTerm") String searchTerm);
}
