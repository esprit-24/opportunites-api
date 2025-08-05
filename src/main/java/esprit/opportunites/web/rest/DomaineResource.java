package esprit.opportunites.web.rest;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.service.DomaineService;
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
 * REST controller for managing {@link Domaine}.
 */
@RestController
@RequestMapping("/api/domaines")
public class DomaineResource {

    private final Logger log = LoggerFactory.getLogger(DomaineResource.class);

    private static final String ENTITY_NAME = "domaine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomaineService domaineService;

    public DomaineResource(DomaineService domaineService) {
        this.domaineService = domaineService;
    }

    /**
     * {@code POST  /domaines} : Create a new domaine.
     *
     * @param domaine the domaine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domaine, or with status {@code 400 (Bad Request)} if the domaine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Domaine> createDomaine(@Valid @RequestBody Domaine domaine) throws URISyntaxException {
        log.debug("REST request to save Domaine : {}", domaine);

        if (domaine.getId() != null) {
            return ResponseEntity.badRequest()
                .headers(
                    HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "idexists", "A new domaine cannot already have an ID")
                )
                .body(null);
        }

        try {
            Domaine result = domaineService.save(domaine);
            return ResponseEntity.created(new URI("/api/domaines/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "validation", e.getMessage()))
                .body(null);
        }
    }

    /**
     * {@code PUT  /domaines/:id} : Updates an existing domaine.
     *
     * @param id the id of the domaine to save.
     * @param domaine the domaine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaine,
     * or with status {@code 400 (Bad Request)} if the domaine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domaine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Domaine> updateDomaine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Domaine domaine
    ) throws URISyntaxException {
        log.debug("REST request to update Domaine : {}, {}", id, domaine);

        if (domaine.getId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "idnull", "Invalid id"))
                .body(null);
        }

        if (!Objects.equals(id, domaine.getId())) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "idinvalid", "Invalid id"))
                .body(null);
        }

        try {
            Domaine result = domaineService.update(domaine);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaine.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "validation", e.getMessage()))
                .body(null);
        }
    }

    /**
     * {@code PATCH  /domaines/:id} : Partial updates given fields of an existing domaine, field will ignore if it is null
     *
     * @param id the id of the domaine to save.
     * @param domaine the domaine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaine,
     * or with status {@code 400 (Bad Request)} if the domaine is not valid,
     * or with status {@code 404 (Not Found)} if the domaine is not found,
     * or with status {@code 500 (Internal Server Error)} if the domaine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Domaine> partialUpdateDomaine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Domaine domaine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Domaine partially : {}, {}", id, domaine);

        if (domaine.getId() == null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "idnull", "Invalid id"))
                .body(null);
        }

        if (!Objects.equals(id, domaine.getId())) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "idinvalid", "Invalid id"))
                .body(null);
        }

        Optional<Domaine> result = domaineService.partialUpdate(domaine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaine.getId().toString())
        );
    }

    /**
     * {@code GET  /domaines} : get all the domaines.
     *
     * @param pageable the pagination information.
     * @param search the search term.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domaines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Domaine>> getAllDomaines(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "search", required = false) String search
    ) {
        log.debug("REST request to get a page of Domaines with search: {}", search);

        if (search != null && !search.trim().isEmpty()) {
            // Recherche sans pagination pour simplifier
            List<Domaine> domaines = domaineService.search(search.trim());
            return ResponseEntity.ok().body(domaines);
        } else {
            // Pagination normale
            Page<Domaine> page = domaineService.findAll(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    /**
     * {@code GET  /domaines/count} : count all the domaines.
     *
     * @param search the search term.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDomaines(@RequestParam(value = "search", required = false) String search) {
        log.debug("REST request to count Domaines with search: {}", search);
        long count = domaineService.count(search);
        return ResponseEntity.ok().body(count);
    }

    /**
     * {@code GET  /domaines/:id} : get the "id" domaine.
     *
     * @param id the id of the domaine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Domaine> getDomaine(@PathVariable("id") Long id) {
        log.debug("REST request to get Domaine : {}", id);
        Optional<Domaine> domaine = domaineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domaine);
    }

    /**
     * {@code GET  /domaines/by-intitule/:intitule} : get domaine by intitule.
     *
     * @param intitule the intitule of the domaine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/by-intitule/{intitule}")
    public ResponseEntity<Domaine> getDomaineByIntitule(@PathVariable("intitule") String intitule) {
        log.debug("REST request to get Domaine by intitule : {}", intitule);
        Optional<Domaine> domaine = domaineService.findByIntitule(intitule);
        return ResponseUtil.wrapOrNotFound(domaine);
    }

    /**
     * {@code GET  /domaines/exists/:intitule} : check if domaine exists by intitule.
     *
     * @param intitule the intitule to check.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body true/false.
     */
    @GetMapping("/exists/{intitule}")
    public ResponseEntity<Boolean> existsByIntitule(@PathVariable("intitule") String intitule) {
        log.debug("REST request to check if Domaine exists by intitule : {}", intitule);
        boolean exists = domaineService.existsByIntitule(intitule);
        return ResponseEntity.ok().body(exists);
    }

    /**
     * {@code DELETE  /domaines/:id} : delete the "id" domaine.
     *
     * @param id the id of the domaine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDomaine(@PathVariable("id") Long id) {
        log.debug("REST request to delete Domaine : {}", id);

        try {
            domaineService.delete(id);
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "notfound", e.getMessage()))
                .build();
        }
    }
}
