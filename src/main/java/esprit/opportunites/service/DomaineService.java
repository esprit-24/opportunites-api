package esprit.opportunites.service;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.repository.DomaineRepository;
import esprit.opportunites.service.dto.DomaineDTO;
import esprit.opportunites.service.mapper.DomaineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Domaine}.
 */
@Service
@Transactional
public class DomaineService {

    private static final Logger LOG = LoggerFactory.getLogger(DomaineService.class);

    private final DomaineRepository domaineRepository;

    private final DomaineMapper domaineMapper;

    public DomaineService(DomaineRepository domaineRepository, DomaineMapper domaineMapper) {
        this.domaineRepository = domaineRepository;
        this.domaineMapper = domaineMapper;
    }

    /**
     * Save a domaine.
     *
     * @param domaineDTO the entity to save.
     * @return the persisted entity.
     */
    public DomaineDTO save(DomaineDTO domaineDTO) {
        LOG.debug("Request to save Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    /**
     * Update a domaine.
     *
     * @param domaineDTO the entity to save.
     * @return the persisted entity.
     */
    public DomaineDTO update(DomaineDTO domaineDTO) {
        LOG.debug("Request to update Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    /**
     * Partially update a domaine.
     *
     * @param domaineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DomaineDTO> partialUpdate(DomaineDTO domaineDTO) {
        LOG.debug("Request to partially update Domaine : {}", domaineDTO);

        return domaineRepository
            .findById(domaineDTO.getId())
            .map(existingDomaine -> {
                domaineMapper.partialUpdate(existingDomaine, domaineDTO);

                return existingDomaine;
            })
            .map(domaineRepository::save)
            .map(domaineMapper::toDto);
    }

    /**
     * Get all the domaines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DomaineDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Domaines");
        return domaineRepository.findAll(pageable).map(domaineMapper::toDto);
    }

    /**
     * Get one domaine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DomaineDTO> findOne(Long id) {
        LOG.debug("Request to get Domaine : {}", id);
        return domaineRepository.findById(id).map(domaineMapper::toDto);
    }

    /**
     * Delete the domaine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Domaine : {}", id);
        domaineRepository.deleteById(id);
    }
}
