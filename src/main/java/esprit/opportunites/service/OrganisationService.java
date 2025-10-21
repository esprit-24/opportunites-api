package esprit.opportunites.service;

import esprit.opportunites.domain.Organisation;
import esprit.opportunites.repository.OrganisationRepository;
import esprit.opportunites.service.dto.OrganisationDTO;
import esprit.opportunites.service.mapper.OrganisationMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Organisation}.
 */
@Service
@Transactional
public class OrganisationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationService.class);

    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    public OrganisationService(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
    }

    /**
     * Save a organisation.
     *
     * @param organisationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganisationDTO save(OrganisationDTO organisationDTO) {
        LOG.debug("Request to save Organisation : {}", organisationDTO);
        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation = organisationRepository.save(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Update a organisation.
     *
     * @param organisationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganisationDTO update(OrganisationDTO organisationDTO) {
        LOG.debug("Request to update Organisation : {}", organisationDTO);
        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation = organisationRepository.save(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Partially update a organisation.
     *
     * @param organisationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganisationDTO> partialUpdate(OrganisationDTO organisationDTO) {
        LOG.debug("Request to partially update Organisation : {}", organisationDTO);

        return organisationRepository
            .findById(organisationDTO.getId())
            .map(existingOrganisation -> {
                organisationMapper.partialUpdate(existingOrganisation, organisationDTO);

                return existingOrganisation;
            })
            .map(organisationRepository::save)
            .map(organisationMapper::toDto);
    }

    /**
     * Get all the organisations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrganisationDTO> findAll() {
        LOG.debug("Request to get all Organisations");
        return organisationRepository.findAll().stream().map(organisationMapper::toDto).toList();
    }

    /**
     * Get one organisation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganisationDTO> findOne(Long id) {
        LOG.debug("Request to get Organisation : {}", id);
        return organisationRepository.findById(id).map(organisationMapper::toDto);
    }

    /**
     * Delete the organisation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Organisation : {}", id);
        organisationRepository.deleteById(id);
    }
}
