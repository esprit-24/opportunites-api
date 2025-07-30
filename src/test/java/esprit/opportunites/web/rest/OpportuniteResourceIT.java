package esprit.opportunites.web.rest;

import static esprit.opportunites.domain.OpportuniteAsserts.*;
import static esprit.opportunites.web.rest.TestUtil.createUpdateProxyForBean;
import static esprit.opportunites.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import esprit.opportunites.IntegrationTest;
import esprit.opportunites.domain.Opportunite;
import esprit.opportunites.domain.enumeration.NiveauEtude;
import esprit.opportunites.domain.enumeration.Statut;
import esprit.opportunites.domain.enumeration.TypeContrat;
import esprit.opportunites.repository.OpportuniteRepository;
import esprit.opportunites.service.dto.OpportuniteDTO;
import esprit.opportunites.service.mapper.OpportuniteMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link OpportuniteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpportuniteResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final NiveauEtude DEFAULT_NIVEAU_ETUDE_REQUIS = NiveauEtude.CFEE;
    private static final NiveauEtude UPDATED_NIVEAU_ETUDE_REQUIS = NiveauEtude.BFEM;

    private static final Integer DEFAULT_NOMBRE_POSTES = 1;
    private static final Integer UPDATED_NOMBRE_POSTES = 2;

    private static final BigDecimal DEFAULT_SALAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALAIRE = new BigDecimal(2);

    private static final Statut DEFAULT_STATUT = Statut.ACTIVE;
    private static final Statut UPDATED_STATUT = Statut.EXPIREE;

    private static final TypeContrat DEFAULT_TYPE_CONTRAT = TypeContrat.CDI;
    private static final TypeContrat UPDATED_TYPE_CONTRAT = TypeContrat.CDD;

    private static final String ENTITY_API_URL = "/api/opportunites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OpportuniteRepository opportuniteRepository;

    @Autowired
    private OpportuniteMapper opportuniteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpportuniteMockMvc;

    private Opportunite opportunite;

    private Opportunite insertedOpportunite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunite createEntity() {
        return new Opportunite()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .adresse(DEFAULT_ADRESSE)
            .niveauEtudeRequis(DEFAULT_NIVEAU_ETUDE_REQUIS)
            .nombrePostes(DEFAULT_NOMBRE_POSTES)
            .salaire(DEFAULT_SALAIRE)
            .statut(DEFAULT_STATUT)
            .typeContrat(DEFAULT_TYPE_CONTRAT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunite createUpdatedEntity() {
        return new Opportunite()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .adresse(UPDATED_ADRESSE)
            .niveauEtudeRequis(UPDATED_NIVEAU_ETUDE_REQUIS)
            .nombrePostes(UPDATED_NOMBRE_POSTES)
            .salaire(UPDATED_SALAIRE)
            .statut(UPDATED_STATUT)
            .typeContrat(UPDATED_TYPE_CONTRAT);
    }

    @BeforeEach
    void initTest() {
        opportunite = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOpportunite != null) {
            opportuniteRepository.delete(insertedOpportunite);
            insertedOpportunite = null;
        }
    }

    @Test
    @Transactional
    void createOpportunite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);
        var returnedOpportuniteDTO = om.readValue(
            restOpportuniteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opportuniteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OpportuniteDTO.class
        );

        // Validate the Opportunite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOpportunite = opportuniteMapper.toEntity(returnedOpportuniteDTO);
        assertOpportuniteUpdatableFieldsEquals(returnedOpportunite, getPersistedOpportunite(returnedOpportunite));

        insertedOpportunite = returnedOpportunite;
    }

    @Test
    @Transactional
    void createOpportuniteWithExistingId() throws Exception {
        // Create the Opportunite with an existing ID
        opportunite.setId(1L);
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportuniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opportuniteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        opportunite.setTitre(null);

        // Create the Opportunite, which fails.
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        restOpportuniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opportuniteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        opportunite.setDateDebut(null);

        // Create the Opportunite, which fails.
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        restOpportuniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opportuniteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombrePostesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        opportunite.setNombrePostes(null);

        // Create the Opportunite, which fails.
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        restOpportuniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opportuniteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOpportunites() throws Exception {
        // Initialize the database
        insertedOpportunite = opportuniteRepository.saveAndFlush(opportunite);

        // Get all the opportuniteList
        restOpportuniteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunite.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].niveauEtudeRequis").value(hasItem(DEFAULT_NIVEAU_ETUDE_REQUIS.toString())))
            .andExpect(jsonPath("$.[*].nombrePostes").value(hasItem(DEFAULT_NOMBRE_POSTES)))
            .andExpect(jsonPath("$.[*].salaire").value(hasItem(sameNumber(DEFAULT_SALAIRE))))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].typeContrat").value(hasItem(DEFAULT_TYPE_CONTRAT.toString())));
    }

    @Test
    @Transactional
    void getOpportunite() throws Exception {
        // Initialize the database
        insertedOpportunite = opportuniteRepository.saveAndFlush(opportunite);

        // Get the opportunite
        restOpportuniteMockMvc
            .perform(get(ENTITY_API_URL_ID, opportunite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opportunite.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.niveauEtudeRequis").value(DEFAULT_NIVEAU_ETUDE_REQUIS.toString()))
            .andExpect(jsonPath("$.nombrePostes").value(DEFAULT_NOMBRE_POSTES))
            .andExpect(jsonPath("$.salaire").value(sameNumber(DEFAULT_SALAIRE)))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.typeContrat").value(DEFAULT_TYPE_CONTRAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOpportunite() throws Exception {
        // Get the opportunite
        restOpportuniteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOpportunite() throws Exception {
        // Initialize the database
        insertedOpportunite = opportuniteRepository.saveAndFlush(opportunite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the opportunite
        Opportunite updatedOpportunite = opportuniteRepository.findById(opportunite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOpportunite are not directly saved in db
        em.detach(updatedOpportunite);
        updatedOpportunite
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .adresse(UPDATED_ADRESSE)
            .niveauEtudeRequis(UPDATED_NIVEAU_ETUDE_REQUIS)
            .nombrePostes(UPDATED_NOMBRE_POSTES)
            .salaire(UPDATED_SALAIRE)
            .statut(UPDATED_STATUT)
            .typeContrat(UPDATED_TYPE_CONTRAT);
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(updatedOpportunite);

        restOpportuniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opportuniteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(opportuniteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOpportuniteToMatchAllProperties(updatedOpportunite);
    }

    @Test
    @Transactional
    void putNonExistingOpportunite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opportunite.setId(longCount.incrementAndGet());

        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportuniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opportuniteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(opportuniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpportunite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opportunite.setId(longCount.incrementAndGet());

        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportuniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(opportuniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpportunite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opportunite.setId(longCount.incrementAndGet());

        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportuniteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(opportuniteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpportuniteWithPatch() throws Exception {
        // Initialize the database
        insertedOpportunite = opportuniteRepository.saveAndFlush(opportunite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the opportunite using partial update
        Opportunite partialUpdatedOpportunite = new Opportunite();
        partialUpdatedOpportunite.setId(opportunite.getId());

        partialUpdatedOpportunite
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .niveauEtudeRequis(UPDATED_NIVEAU_ETUDE_REQUIS)
            .salaire(UPDATED_SALAIRE)
            .typeContrat(UPDATED_TYPE_CONTRAT);

        restOpportuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpportunite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOpportunite))
            )
            .andExpect(status().isOk());

        // Validate the Opportunite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOpportuniteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOpportunite, opportunite),
            getPersistedOpportunite(opportunite)
        );
    }

    @Test
    @Transactional
    void fullUpdateOpportuniteWithPatch() throws Exception {
        // Initialize the database
        insertedOpportunite = opportuniteRepository.saveAndFlush(opportunite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the opportunite using partial update
        Opportunite partialUpdatedOpportunite = new Opportunite();
        partialUpdatedOpportunite.setId(opportunite.getId());

        partialUpdatedOpportunite
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .adresse(UPDATED_ADRESSE)
            .niveauEtudeRequis(UPDATED_NIVEAU_ETUDE_REQUIS)
            .nombrePostes(UPDATED_NOMBRE_POSTES)
            .salaire(UPDATED_SALAIRE)
            .statut(UPDATED_STATUT)
            .typeContrat(UPDATED_TYPE_CONTRAT);

        restOpportuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpportunite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOpportunite))
            )
            .andExpect(status().isOk());

        // Validate the Opportunite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOpportuniteUpdatableFieldsEquals(partialUpdatedOpportunite, getPersistedOpportunite(partialUpdatedOpportunite));
    }

    @Test
    @Transactional
    void patchNonExistingOpportunite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opportunite.setId(longCount.incrementAndGet());

        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opportuniteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(opportuniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpportunite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opportunite.setId(longCount.incrementAndGet());

        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(opportuniteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpportunite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        opportunite.setId(longCount.incrementAndGet());

        // Create the Opportunite
        OpportuniteDTO opportuniteDTO = opportuniteMapper.toDto(opportunite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportuniteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(opportuniteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opportunite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpportunite() throws Exception {
        // Initialize the database
        insertedOpportunite = opportuniteRepository.saveAndFlush(opportunite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the opportunite
        restOpportuniteMockMvc
            .perform(delete(ENTITY_API_URL_ID, opportunite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return opportuniteRepository.count();
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

    protected Opportunite getPersistedOpportunite(Opportunite opportunite) {
        return opportuniteRepository.findById(opportunite.getId()).orElseThrow();
    }

    protected void assertPersistedOpportuniteToMatchAllProperties(Opportunite expectedOpportunite) {
        assertOpportuniteAllPropertiesEquals(expectedOpportunite, getPersistedOpportunite(expectedOpportunite));
    }

    protected void assertPersistedOpportuniteToMatchUpdatableProperties(Opportunite expectedOpportunite) {
        assertOpportuniteAllUpdatablePropertiesEquals(expectedOpportunite, getPersistedOpportunite(expectedOpportunite));
    }
}
