package esprit.opportunites.service;

import esprit.opportunites.domain.Profil;
import esprit.opportunites.repository.ProfilRepository;
import esprit.opportunites.service.dto.ProfilDTO;
import esprit.opportunites.service.mapper.ProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Profil}.
 */
@Service
@Transactional
public class ProfilService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilService.class);

    private final ProfilRepository profilRepository;

    private final ProfilMapper profilMapper;

    public ProfilService(ProfilRepository profilRepository, ProfilMapper profilMapper) {
        this.profilRepository = profilRepository;
        this.profilMapper = profilMapper;
    }

    /**
     * Save a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfilDTO save(ProfilDTO profilDTO) {
        LOG.debug("Request to save Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    /**
     * Update a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfilDTO update(ProfilDTO profilDTO) {
        LOG.debug("Request to update Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    /**
     * Partially update a profil.
     *
     * @param profilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO) {
        LOG.debug("Request to partially update Profil : {}", profilDTO);

        return profilRepository
            .findById(profilDTO.getId())
            .map(existingProfil -> {
                profilMapper.partialUpdate(existingProfil, profilDTO);

                return existingProfil;
            })
            .map(profilRepository::save)
            .map(profilMapper::toDto);
    }

    /**
     * Get all the profils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfilDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Profils");
        return profilRepository.findAll(pageable).map(profilMapper::toDto);
    }

    /**
     * Get one profil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfilDTO> findOne(Long id) {
        LOG.debug("Request to get Profil : {}", id);
        return profilRepository.findById(id).map(profilMapper::toDto);
    }

    /**
     * Delete the profil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Profil : {}", id);
        profilRepository.deleteById(id);
    }
}
