package esprit.opportunites.web.rest;

import static esprit.opportunites.domain.DomaineAsserts.*;
import static esprit.opportunites.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.opportunites.IntegrationTest;
import esprit.opportunites.domain.Domaine;
import esprit.opportunites.repository.DomaineRepository;
import esprit.opportunites.service.dto.DomaineDTO;
import esprit.opportunites.service.mapper.DomaineMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DomaineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomaineResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domaines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private DomaineMapper domaineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomaineMockMvc;

    private Domaine domaine;

    private Domaine insertedDomaine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createEntity() {
        return new Domaine().intitule(DEFAULT_INTITULE).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createUpdatedEntity() {
        return new Domaine().intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        domaine = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDomaine != null) {
            domaineRepository.delete(insertedDomaine);
            insertedDomaine = null;
        }
    }

    @Test
    @Transactional
    void createDomaine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);
        var returnedDomaineDTO = om.readValue(
            restDomaineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domaineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DomaineDTO.class
        );

        // Validate the Domaine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDomaine = domaineMapper.toEntity(returnedDomaineDTO);
        assertDomaineUpdatableFieldsEquals(returnedDomaine, getPersistedDomaine(returnedDomaine));

        insertedDomaine = returnedDomaine;
    }

    @Test
    @Transactional
    void createDomaineWithExistingId() throws Exception {
        // Create the Domaine with an existing ID
        domaine.setId(1L);
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domaineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        domaine.setIntitule(null);

        // Create the Domaine, which fails.
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        restDomaineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domaineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDomaines() throws Exception {
        // Initialize the database
        insertedDomaine = domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaine.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDomaine() throws Exception {
        // Initialize the database
        insertedDomaine = domaineRepository.saveAndFlush(domaine);

        // Get the domaine
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL_ID, domaine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domaine.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDomaine() throws Exception {
        // Get the domaine
        restDomaineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDomaine() throws Exception {
        // Initialize the database
        insertedDomaine = domaineRepository.saveAndFlush(domaine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the domaine
        Domaine updatedDomaine = domaineRepository.findById(domaine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDomaine are not directly saved in db
        em.detach(updatedDomaine);
        updatedDomaine.intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION);
        DomaineDTO domaineDTO = domaineMapper.toDto(updatedDomaine);

        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaineDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domaineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDomaineToMatchAllProperties(updatedDomaine);
    }

    @Test
    @Transactional
    void putNonExistingDomaine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaineDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomaine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomaine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(domaineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomaineWithPatch() throws Exception {
        // Initialize the database
        insertedDomaine = domaineRepository.saveAndFlush(domaine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the domaine using partial update
        Domaine partialUpdatedDomaine = new Domaine();
        partialUpdatedDomaine.setId(domaine.getId());

        partialUpdatedDomaine.intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION);

        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDomaineUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDomaine, domaine), getPersistedDomaine(domaine));
    }

    @Test
    @Transactional
    void fullUpdateDomaineWithPatch() throws Exception {
        // Initialize the database
        insertedDomaine = domaineRepository.saveAndFlush(domaine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the domaine using partial update
        Domaine partialUpdatedDomaine = new Domaine();
        partialUpdatedDomaine.setId(domaine.getId());

        partialUpdatedDomaine.intitule(UPDATED_INTITULE).description(UPDATED_DESCRIPTION);

        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDomaineUpdatableFieldsEquals(partialUpdatedDomaine, getPersistedDomaine(partialUpdatedDomaine));
    }

    @Test
    @Transactional
    void patchNonExistingDomaine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domaineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomaine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomaine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(domaineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domaine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomaine() throws Exception {
        // Initialize the database
        insertedDomaine = domaineRepository.saveAndFlush(domaine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the domaine
        restDomaineMockMvc
            .perform(delete(ENTITY_API_URL_ID, domaine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return domaineRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Domaine getPersistedDomaine(Domaine domaine) {
        return domaineRepository.findById(domaine.getId()).orElseThrow();
    }

    protected void assertPersistedDomaineToMatchAllProperties(Domaine expectedDomaine) {
        assertDomaineAllPropertiesEquals(expectedDomaine, getPersistedDomaine(expectedDomaine));
    }

    protected void assertPersistedDomaineToMatchUpdatableProperties(Domaine expectedDomaine) {
        assertDomaineAllUpdatablePropertiesEquals(expectedDomaine, getPersistedDomaine(expectedDomaine));
    }
}
