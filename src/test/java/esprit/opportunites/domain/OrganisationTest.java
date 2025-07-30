package esprit.opportunites.domain;

import static esprit.opportunites.domain.OrganisationTestSamples.*;
import static esprit.opportunites.domain.VilleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = getOrganisationSample1();
        Organisation organisation2 = new Organisation();
        assertThat(organisation1).isNotEqualTo(organisation2);

        organisation2.setId(organisation1.getId());
        assertThat(organisation1).isEqualTo(organisation2);

        organisation2 = getOrganisationSample2();
        assertThat(organisation1).isNotEqualTo(organisation2);
    }

    @Test
    void villeTest() {
        Organisation organisation = getOrganisationRandomSampleGenerator();
        Ville villeBack = getVilleRandomSampleGenerator();

        organisation.setVille(villeBack);
        assertThat(organisation.getVille()).isEqualTo(villeBack);

        organisation.ville(null);
        assertThat(organisation.getVille()).isNull();
    }
}
