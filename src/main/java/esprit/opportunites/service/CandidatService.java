package esprit.opportunites.service;

import esprit.opportunites.domain.Candidat;
import esprit.opportunites.repository.CandidatRepository;
import esprit.opportunites.repository.UserRepository;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.mapper.CandidatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link esprit.opportunites.domain.Candidat}.
 */
@Service
@Transactional
public class CandidatService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidatService.class);

    private final CandidatRepository candidatRepository;

    private final CandidatMapper candidatMapper;

    private final UserRepository userRepository;

    public CandidatService(CandidatRepository candidatRepository, CandidatMapper candidatMapper, UserRepository userRepository) {
        this.candidatRepository = candidatRepository;
        this.candidatMapper = candidatMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a candidat.
     *
     * @param candidatDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidatDTO save(CandidatDTO candidatDTO) {
        LOG.debug("Request to save Candidat : {}", candidatDTO);
        Candidat candidat = candidatMapper.toEntity(candidatDTO);
        Long userId = candidat.getUser().getId();
        userRepository.findById(userId).ifPresent(candidat::user);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }

    /**
     * Update a candidat.
     *
     * @param candidatDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidatDTO update(CandidatDTO candidatDTO) {
        LOG.debug("Request to update Candidat : {}", candidatDTO);
        Candidat candidat = candidatMapper.toEntity(candidatDTO);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }

    /**
     * Partially update a candidat.
     *
     * @param candidatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CandidatDTO> partialUpdate(CandidatDTO candidatDTO) {
        LOG.debug("Request to partially update Candidat : {}", candidatDTO);

        return candidatRepository
            .findById(candidatDTO.getId())
            .map(existingCandidat -> {
                candidatMapper.partialUpdate(existingCandidat, candidatDTO);

                return existingCandidat;
            })
            .map(candidatRepository::save)
            .map(candidatMapper::toDto);
    }

    /**
     * Get all the candidats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CandidatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Candidats");
        return candidatRepository.findAll(pageable).map(candidatMapper::toDto);
    }

    /**
     * Get one candidat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CandidatDTO> findOne(Long id) {
        LOG.debug("Request to get Candidat : {}", id);
        return candidatRepository.findById(id).map(candidatMapper::toDto);
    }

    /**
     * Delete the candidat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Candidat : {}", id);
        candidatRepository.deleteById(id);
    }
}
