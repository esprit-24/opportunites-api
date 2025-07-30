package esprit.opportunites.service;

import esprit.opportunites.domain.Candidature;
import esprit.opportunites.repository.CandidatureRepository;
import esprit.opportunites.service.dto.CandidatureDTO;
import esprit.opportunites.service.mapper.CandidatureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Candidature}.
 */
@Service
@Transactional
public class CandidatureService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidatureService.class);

    private final CandidatureRepository candidatureRepository;

    private final CandidatureMapper candidatureMapper;

    public CandidatureService(CandidatureRepository candidatureRepository, CandidatureMapper candidatureMapper) {
        this.candidatureRepository = candidatureRepository;
        this.candidatureMapper = candidatureMapper;
    }

    /**
     * Save a candidature.
     *
     * @param candidatureDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidatureDTO save(CandidatureDTO candidatureDTO) {
        LOG.debug("Request to save Candidature : {}", candidatureDTO);
        Candidature candidature = candidatureMapper.toEntity(candidatureDTO);
        candidature = candidatureRepository.save(candidature);
        return candidatureMapper.toDto(candidature);
    }

    /**
     * Update a candidature.
     *
     * @param candidatureDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidatureDTO update(CandidatureDTO candidatureDTO) {
        LOG.debug("Request to update Candidature : {}", candidatureDTO);
        Candidature candidature = candidatureMapper.toEntity(candidatureDTO);
        candidature = candidatureRepository.save(candidature);
        return candidatureMapper.toDto(candidature);
    }

    /**
     * Partially update a candidature.
     *
     * @param candidatureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CandidatureDTO> partialUpdate(CandidatureDTO candidatureDTO) {
        LOG.debug("Request to partially update Candidature : {}", candidatureDTO);

        return candidatureRepository
            .findById(candidatureDTO.getId())
            .map(existingCandidature -> {
                candidatureMapper.partialUpdate(existingCandidature, candidatureDTO);

                return existingCandidature;
            })
            .map(candidatureRepository::save)
            .map(candidatureMapper::toDto);
    }

    /**
     * Get all the candidatures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CandidatureDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Candidatures");
        return candidatureRepository.findAll(pageable).map(candidatureMapper::toDto);
    }

    /**
     * Get one candidature by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CandidatureDTO> findOne(Long id) {
        LOG.debug("Request to get Candidature : {}", id);
        return candidatureRepository.findById(id).map(candidatureMapper::toDto);
    }

    /**
     * Delete the candidature by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Candidature : {}", id);
        candidatureRepository.deleteById(id);
    }
}
