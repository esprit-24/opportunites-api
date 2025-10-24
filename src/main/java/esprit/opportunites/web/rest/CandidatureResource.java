package esprit.opportunites.web.rest;

import esprit.opportunites.repository.CandidatureRepository;
import esprit.opportunites.service.CandidatureService;
import esprit.opportunites.service.dto.CandidatureDTO;
import esprit.opportunites.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link esprit.opportunites.domain.Candidature}.
 */
@RestController
@RequestMapping("/api/candidatures")
public class CandidatureResource {

    private static final Logger LOG = LoggerFactory.getLogger(CandidatureResource.class);
    private static final String ENTITY_NAME = "candidature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidatureService candidatureService;
    private final CandidatureRepository candidatureRepository;

    public CandidatureResource(CandidatureService candidatureService, CandidatureRepository candidatureRepository) {
        this.candidatureService = candidatureService;
        this.candidatureRepository = candidatureRepository;
    }

    // Étudiant crée une candidature
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_CANDIDAT')")
    public ResponseEntity<CandidatureDTO> createCandidature(@Valid @RequestBody CandidatureDTO candidatureDTO) throws URISyntaxException {
        LOG.debug("REST request to save Candidature : {}", candidatureDTO);
        if (candidatureDTO.getId() != null) {
            throw new BadRequestAlertException("A new candidature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        candidatureDTO = candidatureService.save(candidatureDTO);
        return ResponseEntity.created(new URI("/api/candidatures/" + candidatureDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, candidatureDTO.getId().toString()))
            .body(candidatureDTO);
    }

    // Admin / Recruteur peuvent modifier une candidature
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECRUTEUR')")
    public ResponseEntity<CandidatureDTO> updateCandidature(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CandidatureDTO candidatureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Candidature : {}, {}", id, candidatureDTO);
        if (candidatureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidatureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        candidatureDTO = candidatureService.update(candidatureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, candidatureDTO.getId().toString()))
            .body(candidatureDTO);
    }

    // Récupérer toutes les candidatures (Admin/Recruteur uniquement)
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECRUTEUR')")
    public ResponseEntity<List<CandidatureDTO>> getAllCandidatures() {
        LOG.debug("REST request to get all Candidatures");
        List<CandidatureDTO> list = candidatureService.findAll();
        return ResponseEntity.ok().body(list);
    }

    // Récupérer les candidatures du candidat connecté (Étudiant)
    @GetMapping("/candidat/{id}")
    @PreAuthorize("hasAuthority('ROLE_CANDIDAT')")
    public ResponseEntity<List<CandidatureDTO>> getCandidaturesByCandidat(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Candidatures by Candidat ID : {}", id);
        List<CandidatureDTO> list = candidatureService.findByCandidat(id);
        return ResponseEntity.ok().body(list);
    }

    // Récupérer une candidature par ID
    @GetMapping("/{id}")
    public ResponseEntity<CandidatureDTO> getCandidature(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Candidature : {}", id);
        Optional<CandidatureDTO> candidatureDTO = candidatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(candidatureDTO);
    }

    // Supprimer une candidature
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECRUTEUR')")
    public ResponseEntity<Void> deleteCandidature(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Candidature : {}", id);
        candidatureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
