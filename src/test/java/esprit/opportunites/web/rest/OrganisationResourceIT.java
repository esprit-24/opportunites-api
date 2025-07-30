package esprit.opportunites.web.rest;

import static esprit.opportunites.domain.OrganisationAsserts.*;
import static esprit.opportunites.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.opportunites.IntegrationTest;
import esprit.opportunites.domain.Organisation;
import esprit.opportunites.repository.OrganisationRepository;
import esprit.opportunites.service.dto.OrganisationDTO;
import esprit.opportunites.service.mapper.OrganisationMapper;
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
 * Integration tests for the {@link OrganisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganisationResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENTATION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_SECTEUR_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_SECTEUR_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_WEB = "AAAAAAAAAA";
    private static final String UPDATED_SITE_WEB = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private OrganisationMapper organisationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationMockMvc;

    private Organisation organisation;

    private Organisation insertedOrganisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createEntity() {
        return new Organisation()
            .nom(DEFAULT_NOM)
            .presentation(DEFAULT_PRESENTATION)
            .secteurActivite(DEFAULT_SECTEUR_ACTIVITE)
            .logoUrl(DEFAULT_LOGO_URL)
            .adresse(DEFAULT_ADRESSE)
            .siteWeb(DEFAULT_SITE_WEB)
            .emailContact(DEFAULT_EMAIL_CONTACT)
            .telephone(DEFAULT_TELEPHONE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createUpdatedEntity() {
        return new Organisation()
            .nom(UPDATED_NOM)
            .presentation(UPDATED_PRESENTATION)
            .secteurActivite(UPDATED_SECTEUR_ACTIVITE)
            .logoUrl(UPDATED_LOGO_URL)
            .adresse(UPDATED_ADRESSE)
            .siteWeb(UPDATED_SITE_WEB)
            .emailContact(UPDATED_EMAIL_CONTACT)
            .telephone(UPDATED_TELEPHONE);
    }

    @BeforeEach
    void initTest() {
        organisation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrganisation != null) {
            organisationRepository.delete(insertedOrganisation);
            insertedOrganisation = null;
        }
    }

    @Test
    @Transactional
    void createOrganisation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);
        var returnedOrganisationDTO = om.readValue(
            restOrganisationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrganisationDTO.class
        );

        // Validate the Organisation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrganisation = organisationMapper.toEntity(returnedOrganisationDTO);
        assertOrganisationUpdatableFieldsEquals(returnedOrganisation, getPersistedOrganisation(returnedOrganisation));

        insertedOrganisation = returnedOrganisation;
    }

    @Test
    @Transactional
    void createOrganisationWithExistingId() throws Exception {
        // Create the Organisation with an existing ID
        organisation.setId(1L);
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        organisation.setNom(null);

        // Create the Organisation, which fails.
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        organisation.setAdresse(null);

        // Create the Organisation, which fails.
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailContactIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        organisation.setEmailContact(null);

        // Create the Organisation, which fails.
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganisations() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION)))
            .andExpect(jsonPath("$.[*].secteurActivite").value(hasItem(DEFAULT_SECTEUR_ACTIVITE)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].siteWeb").value(hasItem(DEFAULT_SITE_WEB)))
            .andExpect(jsonPath("$.[*].emailContact").value(hasItem(DEFAULT_EMAIL_CONTACT)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get the organisation
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL_ID, organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisation.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.presentation").value(DEFAULT_PRESENTATION))
            .andExpect(jsonPath("$.secteurActivite").value(DEFAULT_SECTEUR_ACTIVITE))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.siteWeb").value(DEFAULT_SITE_WEB))
            .andExpect(jsonPath("$.emailContact").value(DEFAULT_EMAIL_CONTACT))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingOrganisation() throws Exception {
        // Get the organisation
        restOrganisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findById(organisation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganisation are not directly saved in db
        em.detach(updatedOrganisation);
        updatedOrganisation
            .nom(UPDATED_NOM)
            .presentation(UPDATED_PRESENTATION)
            .secteurActivite(UPDATED_SECTEUR_ACTIVITE)
            .logoUrl(UPDATED_LOGO_URL)
            .adresse(UPDATED_ADRESSE)
            .siteWeb(UPDATED_SITE_WEB)
            .emailContact(UPDATED_EMAIL_CONTACT)
            .telephone(UPDATED_TELEPHONE);
        OrganisationDTO organisationDTO = organisationMapper.toDto(updatedOrganisation);

        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrganisationToMatchAllProperties(updatedOrganisation);
    }

    @Test
    @Transactional
    void putNonExistingOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation.nom(UPDATED_NOM).logoUrl(UPDATED_LOGO_URL).adresse(UPDATED_ADRESSE).siteWeb(UPDATED_SITE_WEB);

        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganisationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrganisation, organisation),
            getPersistedOrganisation(organisation)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation
            .nom(UPDATED_NOM)
            .presentation(UPDATED_PRESENTATION)
            .secteurActivite(UPDATED_SECTEUR_ACTIVITE)
            .logoUrl(UPDATED_LOGO_URL)
            .adresse(UPDATED_ADRESSE)
            .siteWeb(UPDATED_SITE_WEB)
            .emailContact(UPDATED_EMAIL_CONTACT)
            .telephone(UPDATED_TELEPHONE);

        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganisationUpdatableFieldsEquals(partialUpdatedOrganisation, getPersistedOrganisation(partialUpdatedOrganisation));
    }

    @Test
    @Transactional
    void patchNonExistingOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the organisation
        restOrganisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return organisationRepository.count();
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

    protected Organisation getPersistedOrganisation(Organisation organisation) {
        return organisationRepository.findById(organisation.getId()).orElseThrow();
    }

    protected void assertPersistedOrganisationToMatchAllProperties(Organisation expectedOrganisation) {
        assertOrganisationAllPropertiesEquals(expectedOrganisation, getPersistedOrganisation(expectedOrganisation));
    }

    protected void assertPersistedOrganisationToMatchUpdatableProperties(Organisation expectedOrganisation) {
        assertOrganisationAllUpdatablePropertiesEquals(expectedOrganisation, getPersistedOrganisation(expectedOrganisation));
    }
}
