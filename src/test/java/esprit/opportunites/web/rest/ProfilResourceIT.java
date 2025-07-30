package esprit.opportunites.web.rest;

import static esprit.opportunites.domain.ProfilAsserts.*;
import static esprit.opportunites.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.opportunites.IntegrationTest;
import esprit.opportunites.domain.Profil;
import esprit.opportunites.repository.ProfilRepository;
import esprit.opportunites.service.dto.ProfilDTO;
import esprit.opportunites.service.mapper.ProfilMapper;
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
 * Integration tests for the {@link ProfilResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfilResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilMapper profilMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilMockMvc;

    private Profil profil;

    private Profil insertedProfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createEntity() {
        return new Profil().intitule(DEFAULT_INTITULE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createUpdatedEntity() {
        return new Profil().intitule(UPDATED_INTITULE);
    }

    @BeforeEach
    void initTest() {
        profil = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfil != null) {
            profilRepository.delete(insertedProfil);
            insertedProfil = null;
        }
    }

    @Test
    @Transactional
    void createProfil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);
        var returnedProfilDTO = om.readValue(
            restProfilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfilDTO.class
        );

        // Validate the Profil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfil = profilMapper.toEntity(returnedProfilDTO);
        assertProfilUpdatableFieldsEquals(returnedProfil, getPersistedProfil(returnedProfil));

        insertedProfil = returnedProfil;
    }

    @Test
    @Transactional
    void createProfilWithExistingId() throws Exception {
        // Create the Profil with an existing ID
        profil.setId(1L);
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profil.setIntitule(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        restProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfils() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        // Get all the profilList
        restProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profil.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)));
    }

    @Test
    @Transactional
    void getProfil() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        // Get the profil
        restProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, profil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profil.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE));
    }

    @Test
    @Transactional
    void getNonExistingProfil() throws Exception {
        // Get the profil
        restProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfil() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profil
        Profil updatedProfil = profilRepository.findById(profil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfil are not directly saved in db
        em.detach(updatedProfil);
        updatedProfil.intitule(UPDATED_INTITULE);
        ProfilDTO profilDTO = profilMapper.toDto(updatedProfil);

        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfilToMatchAllProperties(updatedProfil);
    }

    @Test
    @Transactional
    void putNonExistingProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfil))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfilUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProfil, profil), getPersistedProfil(profil));
    }

    @Test
    @Transactional
    void fullUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil.intitule(UPDATED_INTITULE);

        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfil))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfilUpdatableFieldsEquals(partialUpdatedProfil, getPersistedProfil(partialUpdatedProfil));
    }

    @Test
    @Transactional
    void patchNonExistingProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfil() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profil
        restProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, profil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profilRepository.count();
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

    protected Profil getPersistedProfil(Profil profil) {
        return profilRepository.findById(profil.getId()).orElseThrow();
    }

    protected void assertPersistedProfilToMatchAllProperties(Profil expectedProfil) {
        assertProfilAllPropertiesEquals(expectedProfil, getPersistedProfil(expectedProfil));
    }

    protected void assertPersistedProfilToMatchUpdatableProperties(Profil expectedProfil) {
        assertProfilAllUpdatablePropertiesEquals(expectedProfil, getPersistedProfil(expectedProfil));
    }
}
