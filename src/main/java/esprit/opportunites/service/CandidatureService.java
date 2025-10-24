package esprit.opportunites.service;

import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.Candidature;
import esprit.opportunites.domain.Opportunite;
import esprit.opportunites.repository.CandidatureRepository;
import esprit.opportunites.service.dto.CandidatureDTO;
import esprit.opportunites.service.mapper.CandidatureMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     */
    public CandidatureDTO save(CandidatureDTO candidatureDTO) {
        LOG.debug("Request to save Candidature : {}", candidatureDTO);
        Candidature candidature = candidatureMapper.toEntity(candidatureDTO);

        // Vérifier si le candidat a déjà postulé à la même opportunité
        Candidat candidat = candidature.getCandidat();
        Opportunite opportunite = candidature.getOpportunite();

        if (candidat != null && opportunite != null && candidatureRepository.existsByCandidatAndOpportunite(candidat, opportunite)) {
            throw new IllegalStateException("Vous avez déjà postulé à cette opportunité.");
        }

        candidature = candidatureRepository.save(candidature);
        return candidatureMapper.toDto(candidature);
    }

    /**
     * Update a candidature.
     */
    public CandidatureDTO update(CandidatureDTO candidatureDTO) {
        LOG.debug("Request to update Candidature : {}", candidatureDTO);
        Candidature candidature = candidatureMapper.toEntity(candidatureDTO);
        candidature = candidatureRepository.save(candidature);
        return candidatureMapper.toDto(candidature);
    }

    /**
     * Get all the candidatures.
     */
    @Transactional(readOnly = true)
    public List<CandidatureDTO> findAll() {
        LOG.debug("Request to get all Candidatures");
        return candidatureRepository.findAll().stream().map(candidatureMapper::toDto).toList();
    }

    /**
     * Get all candidatures for a given candidat.
     */
    @Transactional(readOnly = true)
    public List<CandidatureDTO> findByCandidat(Long candidatId) {
        LOG.debug("Request to get Candidatures by Candidat ID : {}", candidatId);
        return candidatureRepository.findByCandidatId(candidatId).stream().map(candidatureMapper::toDto).toList();
    }

    /**
     * Get one candidature by id.
     */
    @Transactional(readOnly = true)
    public Optional<CandidatureDTO> findOne(Long id) {
        LOG.debug("Request to get Candidature : {}", id);
        return candidatureRepository.findById(id).map(candidatureMapper::toDto);
    }

    /**
     * Delete the candidature by id.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Candidature : {}", id);
        candidatureRepository.deleteById(id);
    }
}
