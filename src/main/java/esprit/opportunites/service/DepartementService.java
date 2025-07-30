package esprit.opportunites.service;

import esprit.opportunites.domain.Departement;
import esprit.opportunites.repository.DepartementRepository;
import esprit.opportunites.service.dto.DepartementDTO;
import esprit.opportunites.service.mapper.DepartementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Departement}.
 */
@Service
@Transactional
public class DepartementService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartementService.class);

    private final DepartementRepository departementRepository;

    private final DepartementMapper departementMapper;

    public DepartementService(DepartementRepository departementRepository, DepartementMapper departementMapper) {
        this.departementRepository = departementRepository;
        this.departementMapper = departementMapper;
    }

    /**
     * Save a departement.
     *
     * @param departementDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartementDTO save(DepartementDTO departementDTO) {
        LOG.debug("Request to save Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);
        departement = departementRepository.save(departement);
        return departementMapper.toDto(departement);
    }

    /**
     * Update a departement.
     *
     * @param departementDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartementDTO update(DepartementDTO departementDTO) {
        LOG.debug("Request to update Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);
        departement = departementRepository.save(departement);
        return departementMapper.toDto(departement);
    }

    /**
     * Partially update a departement.
     *
     * @param departementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartementDTO> partialUpdate(DepartementDTO departementDTO) {
        LOG.debug("Request to partially update Departement : {}", departementDTO);

        return departementRepository
            .findById(departementDTO.getId())
            .map(existingDepartement -> {
                departementMapper.partialUpdate(existingDepartement, departementDTO);

                return existingDepartement;
            })
            .map(departementRepository::save)
            .map(departementMapper::toDto);
    }

    /**
     * Get all the departements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartementDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Departements");
        return departementRepository.findAll(pageable).map(departementMapper::toDto);
    }

    /**
     * Get one departement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findOne(Long id) {
        LOG.debug("Request to get Departement : {}", id);
        return departementRepository.findById(id).map(departementMapper::toDto);
    }

    /**
     * Delete the departement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
    }
}
