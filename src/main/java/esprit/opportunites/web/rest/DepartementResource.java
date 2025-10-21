package esprit.opportunites.web.rest;

import esprit.opportunites.repository.DepartementRepository;
import esprit.opportunites.service.DepartementService;
import esprit.opportunites.service.dto.DepartementDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link esprit.opportunites.domain.Departement}.
 */
@RestController
@RequestMapping("/api/departements")
public class DepartementResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "departement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementService departementService;

    private final DepartementRepository departementRepository;

    public DepartementResource(DepartementService departementService, DepartementRepository departementRepository) {
        this.departementService = departementService;
        this.departementRepository = departementRepository;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departementDTO the departementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departementDTO, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DepartementDTO> createDepartement(@Valid @RequestBody DepartementDTO departementDTO) throws URISyntaxException {
        LOG.debug("REST request to save Departement : {}", departementDTO);
        if (departementDTO.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        departementDTO = departementService.save(departementDTO);
        return ResponseEntity.created(new URI("/api/departements/" + departementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, departementDTO.getId().toString()))
            .body(departementDTO);
    }

    /**
     * {@code PUT  /departements/:id} : Updates an existing departement.
     *
     * @param id the id of the departementDTO to save.
     * @param departementDTO the departementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departementDTO,
     * or with status {@code 400 (Bad Request)} if the departementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DepartementDTO> updateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepartementDTO departementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Departement : {}, {}", id, departementDTO);
        if (departementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        departementDTO = departementService.update(departementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departementDTO.getId().toString()))
            .body(departementDTO);
    }

    /**
     * {@code PATCH  /departements/:id} : Partial updates given fields of an existing departement, field will ignore if it is null
     *
     * @param id the id of the departementDTO to save.
     * @param departementDTO the departementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departementDTO,
     * or with status {@code 400 (Bad Request)} if the departementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the departementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the departementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DepartementDTO> partialUpdateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepartementDTO departementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Departement partially : {}, {}", id, departementDTO);
        if (departementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartementDTO> result = departementService.partialUpdate(departementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /departements} : get all the departements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DepartementDTO>> getAllDepartements(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Departements");
        Page<DepartementDTO> page = departementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartementDTO> getDepartement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Departement : {}", id);
        Optional<DepartementDTO> departementDTO = departementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departementDTO);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDepartement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Departement : {}", id);
        departementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
