package esprit.opportunites.web.rest;

import static esprit.opportunites.domain.VilleAsserts.*;
import static esprit.opportunites.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.opportunites.IntegrationTest;
import esprit.opportunites.domain.Ville;
import esprit.opportunites.repository.VilleRepository;
import esprit.opportunites.service.dto.VilleDTO;
import esprit.opportunites.service.mapper.VilleMapper;
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
 * Integration tests for the {@link VilleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VilleResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/villes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private VilleMapper villeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVilleMockMvc;

    private Ville ville;

    private Ville insertedVille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ville createEntity() {
        return new Ville().nom(DEFAULT_NOM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ville createUpdatedEntity() {
        return new Ville().nom(UPDATED_NOM);
    }

    @BeforeEach
    void initTest() {
        ville = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVille != null) {
            villeRepository.delete(insertedVille);
            insertedVille = null;
        }
    }

    @Test
    @Transactional
    void createVille() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);
        var returnedVilleDTO = om.readValue(
            restVilleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(villeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VilleDTO.class
        );

        // Validate the Ville in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVille = villeMapper.toEntity(returnedVilleDTO);
        assertVilleUpdatableFieldsEquals(returnedVille, getPersistedVille(returnedVille));

        insertedVille = returnedVille;
    }

    @Test
    @Transactional
    void createVilleWithExistingId() throws Exception {
        // Create the Ville with an existing ID
        ville.setId(1L);
        VilleDTO villeDTO = villeMapper.toDto(ville);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ville.setNom(null);

        // Create the Ville, which fails.
        VilleDTO villeDTO = villeMapper.toDto(ville);

        restVilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVilles() throws Exception {
        // Initialize the database
        insertedVille = villeRepository.saveAndFlush(ville);

        // Get all the villeList
        restVilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getVille() throws Exception {
        // Initialize the database
        insertedVille = villeRepository.saveAndFlush(ville);

        // Get the ville
        restVilleMockMvc
            .perform(get(ENTITY_API_URL_ID, ville.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ville.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingVille() throws Exception {
        // Get the ville
        restVilleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVille() throws Exception {
        // Initialize the database
        insertedVille = villeRepository.saveAndFlush(ville);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ville
        Ville updatedVille = villeRepository.findById(ville.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVille are not directly saved in db
        em.detach(updatedVille);
        updatedVille.nom(UPDATED_NOM);
        VilleDTO villeDTO = villeMapper.toDto(updatedVille);

        restVilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(villeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVilleToMatchAllProperties(updatedVille);
    }

    @Test
    @Transactional
    void putNonExistingVille() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ville.setId(longCount.incrementAndGet());

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, villeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(villeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVille() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ville.setId(longCount.incrementAndGet());

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(villeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVille() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ville.setId(longCount.incrementAndGet());

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVilleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(villeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVilleWithPatch() throws Exception {
        // Initialize the database
        insertedVille = villeRepository.saveAndFlush(ville);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ville using partial update
        Ville partialUpdatedVille = new Ville();
        partialUpdatedVille.setId(ville.getId());

        partialUpdatedVille.nom(UPDATED_NOM);

        restVilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVille.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVille))
            )
            .andExpect(status().isOk());

        // Validate the Ville in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVilleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVille, ville), getPersistedVille(ville));
    }

    @Test
    @Transactional
    void fullUpdateVilleWithPatch() throws Exception {
        // Initialize the database
        insertedVille = villeRepository.saveAndFlush(ville);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ville using partial update
        Ville partialUpdatedVille = new Ville();
        partialUpdatedVille.setId(ville.getId());

        partialUpdatedVille.nom(UPDATED_NOM);

        restVilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVille.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVille))
            )
            .andExpect(status().isOk());

        // Validate the Ville in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVilleUpdatableFieldsEquals(partialUpdatedVille, getPersistedVille(partialUpdatedVille));
    }

    @Test
    @Transactional
    void patchNonExistingVille() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ville.setId(longCount.incrementAndGet());

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, villeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(villeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVille() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ville.setId(longCount.incrementAndGet());

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(villeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVille() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ville.setId(longCount.incrementAndGet());

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVilleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(villeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ville in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVille() throws Exception {
        // Initialize the database
        insertedVille = villeRepository.saveAndFlush(ville);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ville
        restVilleMockMvc
            .perform(delete(ENTITY_API_URL_ID, ville.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return villeRepository.count();
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

    protected Ville getPersistedVille(Ville ville) {
        return villeRepository.findById(ville.getId()).orElseThrow();
    }

    protected void assertPersistedVilleToMatchAllProperties(Ville expectedVille) {
        assertVilleAllPropertiesEquals(expectedVille, getPersistedVille(expectedVille));
    }

    protected void assertPersistedVilleToMatchUpdatableProperties(Ville expectedVille) {
        assertVilleAllUpdatablePropertiesEquals(expectedVille, getPersistedVille(expectedVille));
    }
}
