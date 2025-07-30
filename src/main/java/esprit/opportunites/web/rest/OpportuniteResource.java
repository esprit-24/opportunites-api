package esprit.opportunites.web.rest;

import esprit.opportunites.repository.OpportuniteRepository;
import esprit.opportunites.service.OpportuniteService;
import esprit.opportunites.service.dto.OpportuniteDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link esprit.opportunites.domain.Opportunite}.
 */
@RestController
@RequestMapping("/api/opportunites")
public class OpportuniteResource {

    private static final Logger LOG = LoggerFactory.getLogger(OpportuniteResource.class);

    private static final String ENTITY_NAME = "opportunite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpportuniteService opportuniteService;

    private final OpportuniteRepository opportuniteRepository;

    public OpportuniteResource(OpportuniteService opportuniteService, OpportuniteRepository opportuniteRepository) {
        this.opportuniteService = opportuniteService;
        this.opportuniteRepository = opportuniteRepository;
    }

    /**
     * {@code POST  /opportunites} : Create a new opportunite.
     *
     * @param opportuniteDTO the opportuniteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opportuniteDTO, or with status {@code 400 (Bad Request)} if the opportunite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OpportuniteDTO> createOpportunite(@Valid @RequestBody OpportuniteDTO opportuniteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Opportunite : {}", opportuniteDTO);
        if (opportuniteDTO.getId() != null) {
            throw new BadRequestAlertException("A new opportunite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        opportuniteDTO = opportuniteService.save(opportuniteDTO);
        return ResponseEntity.created(new URI("/api/opportunites/" + opportuniteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, opportuniteDTO.getId().toString()))
            .body(opportuniteDTO);
    }

    /**
     * {@code PUT  /opportunites/:id} : Updates an existing opportunite.
     *
     * @param id the id of the opportuniteDTO to save.
     * @param opportuniteDTO the opportuniteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opportuniteDTO,
     * or with status {@code 400 (Bad Request)} if the opportuniteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opportuniteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OpportuniteDTO> updateOpportunite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OpportuniteDTO opportuniteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Opportunite : {}, {}", id, opportuniteDTO);
        if (opportuniteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opportuniteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opportuniteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        opportuniteDTO = opportuniteService.update(opportuniteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opportuniteDTO.getId().toString()))
            .body(opportuniteDTO);
    }

    /**
     * {@code PATCH  /opportunites/:id} : Partial updates given fields of an existing opportunite, field will ignore if it is null
     *
     * @param id the id of the opportuniteDTO to save.
     * @param opportuniteDTO the opportuniteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opportuniteDTO,
     * or with status {@code 400 (Bad Request)} if the opportuniteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the opportuniteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the opportuniteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OpportuniteDTO> partialUpdateOpportunite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OpportuniteDTO opportuniteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Opportunite partially : {}, {}", id, opportuniteDTO);
        if (opportuniteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opportuniteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opportuniteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OpportuniteDTO> result = opportuniteService.partialUpdate(opportuniteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opportuniteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /opportunites} : get all the opportunites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opportunites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OpportuniteDTO>> getAllOpportunites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Opportunites");
        Page<OpportuniteDTO> page = opportuniteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /opportunites/:id} : get the "id" opportunite.
     *
     * @param id the id of the opportuniteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opportuniteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OpportuniteDTO> getOpportunite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Opportunite : {}", id);
        Optional<OpportuniteDTO> opportuniteDTO = opportuniteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opportuniteDTO);
    }

    /**
     * {@code DELETE  /opportunites/:id} : delete the "id" opportunite.
     *
     * @param id the id of the opportuniteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpportunite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Opportunite : {}", id);
        opportuniteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
