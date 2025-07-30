package esprit.opportunites.domain;

import static esprit.opportunites.domain.OrganisationTestSamples.*;
import static esprit.opportunites.domain.RecruteurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecruteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recruteur.class);
        Recruteur recruteur1 = getRecruteurSample1();
        Recruteur recruteur2 = new Recruteur();
        assertThat(recruteur1).isNotEqualTo(recruteur2);

        recruteur2.setId(recruteur1.getId());
        assertThat(recruteur1).isEqualTo(recruteur2);

        recruteur2 = getRecruteurSample2();
        assertThat(recruteur1).isNotEqualTo(recruteur2);
    }

    @Test
    void organisationTest() {
        Recruteur recruteur = getRecruteurRandomSampleGenerator();
        Organisation organisationBack = getOrganisationRandomSampleGenerator();

        recruteur.setOrganisation(organisationBack);
        assertThat(recruteur.getOrganisation()).isEqualTo(organisationBack);

        recruteur.organisation(null);
        assertThat(recruteur.getOrganisation()).isNull();
    }
}
