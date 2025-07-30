package esprit.opportunites.service;

import esprit.opportunites.domain.Opportunite;
import esprit.opportunites.repository.OpportuniteRepository;
import esprit.opportunites.service.dto.OpportuniteDTO;
import esprit.opportunites.service.mapper.OpportuniteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Opportunite}.
 */
@Service
@Transactional
public class OpportuniteService {

    private static final Logger LOG = LoggerFactory.getLogger(OpportuniteService.class);

    private final OpportuniteRepository opportuniteRepository;

    private final OpportuniteMapper opportuniteMapper;

    public OpportuniteService(OpportuniteRepository opportuniteRepository, OpportuniteMapper opportuniteMapper) {
        this.opportuniteRepository = opportuniteRepository;
        this.opportuniteMapper = opportuniteMapper;
    }

    /**
     * Save a opportunite.
     *
     * @param opportuniteDTO the entity to save.
     * @return the persisted entity.
     */
    public OpportuniteDTO save(OpportuniteDTO opportuniteDTO) {
        LOG.debug("Request to save Opportunite : {}", opportuniteDTO);
        Opportunite opportunite = opportuniteMapper.toEntity(opportuniteDTO);
        opportunite = opportuniteRepository.save(opportunite);
        return opportuniteMapper.toDto(opportunite);
    }

    /**
     * Update a opportunite.
     *
     * @param opportuniteDTO the entity to save.
     * @return the persisted entity.
     */
    public OpportuniteDTO update(OpportuniteDTO opportuniteDTO) {
        LOG.debug("Request to update Opportunite : {}", opportuniteDTO);
        Opportunite opportunite = opportuniteMapper.toEntity(opportuniteDTO);
        opportunite = opportuniteRepository.save(opportunite);
        return opportuniteMapper.toDto(opportunite);
    }

    /**
     * Partially update a opportunite.
     *
     * @param opportuniteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OpportuniteDTO> partialUpdate(OpportuniteDTO opportuniteDTO) {
        LOG.debug("Request to partially update Opportunite : {}", opportuniteDTO);

        return opportuniteRepository
            .findById(opportuniteDTO.getId())
            .map(existingOpportunite -> {
                opportuniteMapper.partialUpdate(existingOpportunite, opportuniteDTO);

                return existingOpportunite;
            })
            .map(opportuniteRepository::save)
            .map(opportuniteMapper::toDto);
    }

    /**
     * Get all the opportunites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OpportuniteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Opportunites");
        return opportuniteRepository.findAll(pageable).map(opportuniteMapper::toDto);
    }

    /**
     * Get one opportunite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OpportuniteDTO> findOne(Long id) {
        LOG.debug("Request to get Opportunite : {}", id);
        return opportuniteRepository.findById(id).map(opportuniteMapper::toDto);
    }

    /**
     * Delete the opportunite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Opportunite : {}", id);
        opportuniteRepository.deleteById(id);
    }
}
