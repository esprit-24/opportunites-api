package esprit.opportunites.web.rest;

import esprit.opportunites.repository.DomaineRepository;
import esprit.opportunites.service.DomaineService;
import esprit.opportunites.service.dto.DomaineDTO;
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
 * REST controller for managing {@link esprit.opportunites.domain.Domaine}.
 */
@RestController
@RequestMapping("/api/domaines")
public class DomaineResource {

    private static final Logger LOG = LoggerFactory.getLogger(DomaineResource.class);

    private static final String ENTITY_NAME = "domaine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomaineService domaineService;

    private final DomaineRepository domaineRepository;

    public DomaineResource(DomaineService domaineService, DomaineRepository domaineRepository) {
        this.domaineService = domaineService;
        this.domaineRepository = domaineRepository;
    }

    /**
     * {@code POST  /domaines} : Create a new domaine.
     *
     * @param domaineDTO the domaineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domaineDTO, or with status {@code 400 (Bad Request)} if the domaine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DomaineDTO> createDomaine(@Valid @RequestBody DomaineDTO domaineDTO) throws URISyntaxException {
        LOG.debug("REST request to save Domaine : {}", domaineDTO);
        if (domaineDTO.getId() != null) {
            throw new BadRequestAlertException("A new domaine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        domaineDTO = domaineService.save(domaineDTO);
        return ResponseEntity.created(new URI("/api/domaines/" + domaineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, domaineDTO.getId().toString()))
            .body(domaineDTO);
    }

    /**
     * {@code PUT  /domaines/:id} : Updates an existing domaine.
     *
     * @param id the id of the domaineDTO to save.
     * @param domaineDTO the domaineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineDTO,
     * or with status {@code 400 (Bad Request)} if the domaineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domaineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DomaineDTO> updateDomaine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DomaineDTO domaineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Domaine : {}, {}", id, domaineDTO);
        if (domaineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        domaineDTO = domaineService.update(domaineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaineDTO.getId().toString()))
            .body(domaineDTO);
    }

    /**
     * {@code PATCH  /domaines/:id} : Partial updates given fields of an existing domaine, field will ignore if it is null
     *
     * @param id the id of the domaineDTO to save.
     * @param domaineDTO the domaineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineDTO,
     * or with status {@code 400 (Bad Request)} if the domaineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the domaineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the domaineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DomaineDTO> partialUpdateDomaine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DomaineDTO domaineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Domaine partially : {}, {}", id, domaineDTO);
        if (domaineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DomaineDTO> result = domaineService.partialUpdate(domaineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /domaines} : get all the domaines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domaines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DomaineDTO>> getAllDomaines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Domaines");
        Page<DomaineDTO> page = domaineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /domaines/:id} : get the "id" domaine.
     *
     * @param id the id of the domaineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DomaineDTO> getDomaine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Domaine : {}", id);
        Optional<DomaineDTO> domaineDTO = domaineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domaineDTO);
    }

    /**
     * {@code DELETE  /domaines/:id} : delete the "id" domaine.
     *
     * @param id the id of the domaineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Domaine : {}", id);
        domaineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
