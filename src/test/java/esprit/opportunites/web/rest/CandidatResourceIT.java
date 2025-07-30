package esprit.opportunites.web.rest;

import static esprit.opportunites.domain.CandidatAsserts.*;
import static esprit.opportunites.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.opportunites.IntegrationTest;
import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.User;
import esprit.opportunites.domain.enumeration.NiveauEtude;
import esprit.opportunites.repository.CandidatRepository;
import esprit.opportunites.repository.UserRepository;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.mapper.CandidatMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CandidatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatResourceIT {

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NiveauEtude DEFAULT_NIVEAU_ETUDE = NiveauEtude.CFEE;
    private static final NiveauEtude UPDATED_NIVEAU_ETUDE = NiveauEtude.BFEM;

    private static final String DEFAULT_CV_URL = "AAAAAAAAAA";
    private static final String UPDATED_CV_URL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUT_ACTUEL = "AAAAAAAAAA";
    private static final String UPDATED_STATUT_ACTUEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/candidats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidatMapper candidatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatMockMvc;

    private Candidat candidat;

    private Candidat insertedCandidat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidat createEntity(EntityManager em) {
        Candidat candidat = new Candidat()
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .niveauEtude(DEFAULT_NIVEAU_ETUDE)
            .cvUrl(DEFAULT_CV_URL)
            .statutActuel(DEFAULT_STATUT_ACTUEL);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        candidat.setUser(user);
        return candidat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidat createUpdatedEntity(EntityManager em) {
        Candidat updatedCandidat = new Candidat()
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cvUrl(UPDATED_CV_URL)
            .statutActuel(UPDATED_STATUT_ACTUEL);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCandidat.setUser(user);
        return updatedCandidat;
    }

    @BeforeEach
    void initTest() {
        candidat = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCandidat != null) {
            candidatRepository.delete(insertedCandidat);
            insertedCandidat = null;
        }
    }

    @Test
    @Transactional
    void createCandidat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);
        var returnedCandidatDTO = om.readValue(
            restCandidatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidatDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CandidatDTO.class
        );

        // Validate the Candidat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCandidat = candidatMapper.toEntity(returnedCandidatDTO);
        assertCandidatUpdatableFieldsEquals(returnedCandidat, getPersistedCandidat(returnedCandidat));

        assertCandidatMapsIdRelationshipPersistedValue(candidat, returnedCandidat);

        insertedCandidat = returnedCandidat;
    }

    @Test
    @Transactional
    void createCandidatWithExistingId() throws Exception {
        // Create the Candidat with an existing ID
        candidat.setId(1L);
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateCandidatMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();

        // Load the candidat
        Candidat updatedCandidat = candidatRepository.findById(candidat.getId()).orElseThrow();
        assertThat(updatedCandidat).isNotNull();
        // Disconnect from session so that the updates on updatedCandidat are not directly saved in db
        em.detach(updatedCandidat);

        // Update the User with new association value
        updatedCandidat.setUser(user);
        CandidatDTO updatedCandidatDTO = candidatMapper.toDto(updatedCandidat);
        assertThat(updatedCandidatDTO).isNotNull();

        // Update the entity
        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCandidatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        /**
         * Validate the id for MapsId, the ids must be same
         * Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
         * Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
         * assertThat(testCandidat.getId()).isEqualTo(testCandidat.getUser().getId());
         */
    }

    @Test
    @Transactional
    void checkNiveauEtudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        candidat.setNiveauEtude(null);

        // Create the Candidat, which fails.
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        restCandidatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCandidats() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);

        // Get all the candidatList
        restCandidatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidat.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].niveauEtude").value(hasItem(DEFAULT_NIVEAU_ETUDE.toString())))
            .andExpect(jsonPath("$.[*].cvUrl").value(hasItem(DEFAULT_CV_URL)))
            .andExpect(jsonPath("$.[*].statutActuel").value(hasItem(DEFAULT_STATUT_ACTUEL)));
    }

    @Test
    @Transactional
    void getCandidat() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);

        // Get the candidat
        restCandidatMockMvc
            .perform(get(ENTITY_API_URL_ID, candidat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidat.getId().intValue()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.niveauEtude").value(DEFAULT_NIVEAU_ETUDE.toString()))
            .andExpect(jsonPath("$.cvUrl").value(DEFAULT_CV_URL))
            .andExpect(jsonPath("$.statutActuel").value(DEFAULT_STATUT_ACTUEL));
    }

    @Test
    @Transactional
    void getNonExistingCandidat() throws Exception {
        // Get the candidat
        restCandidatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCandidat() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidat
        Candidat updatedCandidat = candidatRepository.findById(candidat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCandidat are not directly saved in db
        em.detach(updatedCandidat);
        updatedCandidat
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cvUrl(UPDATED_CV_URL)
            .statutActuel(UPDATED_STATUT_ACTUEL);
        CandidatDTO candidatDTO = candidatMapper.toDto(updatedCandidat);

        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCandidatToMatchAllProperties(updatedCandidat);
    }

    @Test
    @Transactional
    void putNonExistingCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(candidatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatWithPatch() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidat using partial update
        Candidat partialUpdatedCandidat = new Candidat();
        partialUpdatedCandidat.setId(candidat.getId());

        partialUpdatedCandidat.dateNaissance(UPDATED_DATE_NAISSANCE).niveauEtude(UPDATED_NIVEAU_ETUDE).statutActuel(UPDATED_STATUT_ACTUEL);

        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidat))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidatUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCandidat, candidat), getPersistedCandidat(candidat));
    }

    @Test
    @Transactional
    void fullUpdateCandidatWithPatch() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the candidat using partial update
        Candidat partialUpdatedCandidat = new Candidat();
        partialUpdatedCandidat.setId(candidat.getId());

        partialUpdatedCandidat
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cvUrl(UPDATED_CV_URL)
            .statutActuel(UPDATED_STATUT_ACTUEL);

        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCandidat))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCandidatUpdatableFieldsEquals(partialUpdatedCandidat, getPersistedCandidat(partialUpdatedCandidat));
    }

    @Test
    @Transactional
    void patchNonExistingCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(candidatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidat() throws Exception {
        // Initialize the database
        insertedCandidat = candidatRepository.saveAndFlush(candidat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the candidat
        restCandidatMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return candidatRepository.count();
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

    protected Candidat getPersistedCandidat(Candidat candidat) {
        return candidatRepository.findById(candidat.getId()).orElseThrow();
    }

    protected void assertPersistedCandidatToMatchAllProperties(Candidat expectedCandidat) {
        assertCandidatAllPropertiesEquals(expectedCandidat, getPersistedCandidat(expectedCandidat));
    }

    protected void assertPersistedCandidatToMatchUpdatableProperties(Candidat expectedCandidat) {
        assertCandidatAllUpdatablePropertiesEquals(expectedCandidat, getPersistedCandidat(expectedCandidat));
    }
}
