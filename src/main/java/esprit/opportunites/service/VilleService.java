package esprit.opportunites.service;

import esprit.opportunites.domain.Ville;
import esprit.opportunites.repository.VilleRepository;
import esprit.opportunites.service.dto.VilleDTO;
import esprit.opportunites.service.mapper.VilleMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Ville}.
 */
@Service
@Transactional
public class VilleService {

    private static final Logger LOG = LoggerFactory.getLogger(VilleService.class);

    private final VilleRepository villeRepository;

    private final VilleMapper villeMapper;

    public VilleService(VilleRepository villeRepository, VilleMapper villeMapper) {
        this.villeRepository = villeRepository;
        this.villeMapper = villeMapper;
    }

    /**
     * Save a ville.
     *
     * @param villeDTO the entity to save.
     * @return the persisted entity.
     */
    public VilleDTO save(VilleDTO villeDTO) {
        LOG.debug("Request to save Ville : {}", villeDTO);
        Ville ville = villeMapper.toEntity(villeDTO);
        ville = villeRepository.save(ville);
        return villeMapper.toDto(ville);
    }

    /**
     * Update a ville.
     *
     * @param villeDTO the entity to save.
     * @return the persisted entity.
     */
    public VilleDTO update(VilleDTO villeDTO) {
        LOG.debug("Request to update Ville : {}", villeDTO);
        Ville ville = villeMapper.toEntity(villeDTO);
        ville = villeRepository.save(ville);
        return villeMapper.toDto(ville);
    }

    /**
     * Partially update a ville.
     *
     * @param villeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VilleDTO> partialUpdate(VilleDTO villeDTO) {
        LOG.debug("Request to partially update Ville : {}", villeDTO);

        return villeRepository
            .findById(villeDTO.getId())
            .map(existingVille -> {
                villeMapper.partialUpdate(existingVille, villeDTO);

                return existingVille;
            })
            .map(villeRepository::save)
            .map(villeMapper::toDto);
    }

    /**
     * Get all the villes.
     *
     */
    @Transactional(readOnly = true)
    public List<VilleDTO> findAll() {
        return villeRepository.findAll().stream().map(villeMapper::toDto).toList();
    }

    /**
     * Get one ville by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VilleDTO> findOne(Long id) {
        LOG.debug("Request to get Ville : {}", id);
        return villeRepository.findById(id).map(villeMapper::toDto);
    }

    /**
     * Delete the ville by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Ville : {}", id);
        villeRepository.deleteById(id);
    }
}
