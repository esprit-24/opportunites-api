package esprit.opportunites.service;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.repository.DomaineRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing {@link Domaine}.
 */
@Service
@Transactional
public class DomaineService {

    private final Logger log = LoggerFactory.getLogger(DomaineService.class);

    private final DomaineRepository domaineRepository;

    public DomaineService(DomaineRepository domaineRepository) {
        this.domaineRepository = domaineRepository;
    }

    /**
     * Save a domaine.
     *
     * @param domaine the entity to save.
     * @return the persisted entity.
     */
    public Domaine save(Domaine domaine) {
        log.debug("Request to save Domaine : {}", domaine);
        validateDomaine(domaine);
        return domaineRepository.save(domaine);
    }

    /**
     * Update a domaine.
     *
     * @param domaine the entity to update.
     * @return the persisted entity.
     */
    public Domaine update(Domaine domaine) {
        log.debug("Request to update Domaine : {}", domaine);
        validateDomaine(domaine);
        validateDomaineExists(domaine.getId());
        return domaineRepository.save(domaine);
    }

    /**
     * Partially update a domaine.
     *
     * @param domaine the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Domaine> partialUpdate(Domaine domaine) {
        log.debug("Request to partially update Domaine : {}", domaine);

        return domaineRepository
            .findById(domaine.getId())
            .map(existingDomaine -> {
                if (StringUtils.hasText(domaine.getIntitule())) {
                    existingDomaine.setIntitule(domaine.getIntitule());
                }
                if (StringUtils.hasText(domaine.getDescription())) {
                    existingDomaine.setDescription(domaine.getDescription());
                }
                return existingDomaine;
            })
            .map(domaineRepository::save);
    }

    /**
     * Get all the domaines.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Domaine> findAll() {
        log.debug("Request to get all Domaines");
        return domaineRepository.findAllByOrderByIntituleAsc();
    }

    /**
     * Get all the domaines with pagination.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Domaine> findAll(Pageable pageable) {
        log.debug("Request to get all Domaines with pagination");
        return domaineRepository.findAll(pageable);
    }

    /**
     * Get one domaine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Domaine> findOne(Long id) {
        log.debug("Request to get Domaine : {}", id);
        return domaineRepository.findById(id);
    }

    /**
     * Get one domaine by intitule.
     *
     * @param intitule the intitule of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Domaine> findByIntitule(String intitule) {
        log.debug("Request to get Domaine by intitule : {}", intitule);
        return domaineRepository.findByIntituleIgnoreCase(intitule);
    }

    /**
     * Search domaines by term.
     *
     * @param searchTerm the search term.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Domaine> search(String searchTerm) {
        log.debug("Request to search Domaines : {}", searchTerm);
        if (!StringUtils.hasText(searchTerm)) {
            return findAll();
        }
        return domaineRepository.searchByIntituleOrDescription(searchTerm.trim());
    }

    /**
     * Count domaines by search term.
     *
     * @param searchTerm the search term.
     * @return the count of entities.
     */
    @Transactional(readOnly = true)
    public long count(String searchTerm) {
        log.debug("Request to count Domaines by search term : {}", searchTerm);
        if (!StringUtils.hasText(searchTerm)) {
            return domaineRepository.count();
        }
        return domaineRepository.countByIntituleOrDescriptionContaining(searchTerm.trim());
    }

    /**
     * Delete the domaine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Domaine : {}", id);
        validateDomaineExists(id);
        domaineRepository.deleteById(id);
    }

    /**
     * Check if domaine exists by intitule.
     *
     * @param intitule the intitule to check.
     * @return true if exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean existsByIntitule(String intitule) {
        log.debug("Request to check if Domaine exists by intitule : {}", intitule);
        return StringUtils.hasText(intitule) && domaineRepository.existsByIntituleIgnoreCase(intitule.trim());
    }

    /**
     * Check if domaine exists by intitule excluding current id.
     *
     * @param intitule the intitule to check.
     * @param excludeId the id to exclude from check.
     * @return true if exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean existsByIntituleAndIdNot(String intitule, Long excludeId) {
        log.debug("Request to check if Domaine exists by intitule excluding id : {} - {}", intitule, excludeId);
        return domaineRepository.findByIntituleIgnoreCase(intitule).map(Domaine::getId).filter(id -> !id.equals(excludeId)).isPresent();
    }

    /**
     * Validate domaine data.
     *
     * @param domaine the domaine to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    private void validateDomaine(Domaine domaine) {
        if (domaine == null) {
            throw new IllegalArgumentException("Domaine cannot be null");
        }
        if (!StringUtils.hasText(domaine.getIntitule())) {
            throw new IllegalArgumentException("Intitule is required");
        }
        if (!StringUtils.hasText(domaine.getDescription())) {
            throw new IllegalArgumentException("Description is required");
        }

        // Vérifier l'unicité de l'intitulé
        String intitule = domaine.getIntitule().trim();
        if (domaine.getId() == null) {
            // Nouveau domaine
            if (existsByIntitule(intitule)) {
                throw new IllegalArgumentException("A domaine with this intitule already exists");
            }
        } else {
            // Mise à jour
            if (existsByIntituleAndIdNot(intitule, domaine.getId())) {
                throw new IllegalArgumentException("A domaine with this intitule already exists");
            }
        }
    }

    /**
     * Validate that domaine exists.
     *
     * @param id the id to check.
     * @throws IllegalArgumentException if domaine doesn't exist.
     */
    private void validateDomaineExists(Long id) {
        if (id == null || !domaineRepository.existsById(id)) {
            throw new IllegalArgumentException("Domaine not found with id: " + id);
        }
    }
}
