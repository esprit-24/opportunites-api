package esprit.opportunites.domain;

import static esprit.opportunites.domain.DomaineTestSamples.*;
import static esprit.opportunites.domain.OpportuniteTestSamples.*;
import static esprit.opportunites.domain.OrganisationTestSamples.*;
import static esprit.opportunites.domain.VilleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpportuniteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opportunite.class);
        Opportunite opportunite1 = getOpportuniteSample1();
        Opportunite opportunite2 = new Opportunite();
        assertThat(opportunite1).isNotEqualTo(opportunite2);

        opportunite2.setId(opportunite1.getId());
        assertThat(opportunite1).isEqualTo(opportunite2);

        opportunite2 = getOpportuniteSample2();
        assertThat(opportunite1).isNotEqualTo(opportunite2);
    }

    @Test
    void domaineTest() {
        Opportunite opportunite = getOpportuniteRandomSampleGenerator();
        Domaine domaineBack = getDomaineRandomSampleGenerator();

        opportunite.setDomaine(domaineBack);
        assertThat(opportunite.getDomaine()).isEqualTo(domaineBack);

        opportunite.domaine(null);
        assertThat(opportunite.getDomaine()).isNull();
    }

    @Test
    void organisationTest() {
        Opportunite opportunite = getOpportuniteRandomSampleGenerator();
        Organisation organisationBack = getOrganisationRandomSampleGenerator();

        opportunite.setOrganisation(organisationBack);
        assertThat(opportunite.getOrganisation()).isEqualTo(organisationBack);

        opportunite.organisation(null);
        assertThat(opportunite.getOrganisation()).isNull();
    }

    @Test
    void villeTest() {
        Opportunite opportunite = getOpportuniteRandomSampleGenerator();
        Ville villeBack = getVilleRandomSampleGenerator();

        opportunite.setVille(villeBack);
        assertThat(opportunite.getVille()).isEqualTo(villeBack);

        opportunite.ville(null);
        assertThat(opportunite.getVille()).isNull();
    }
}
