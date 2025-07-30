package esprit.opportunites.domain;

import static esprit.opportunites.domain.DepartementTestSamples.*;
import static esprit.opportunites.domain.VilleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VilleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ville.class);
        Ville ville1 = getVilleSample1();
        Ville ville2 = new Ville();
        assertThat(ville1).isNotEqualTo(ville2);

        ville2.setId(ville1.getId());
        assertThat(ville1).isEqualTo(ville2);

        ville2 = getVilleSample2();
        assertThat(ville1).isNotEqualTo(ville2);
    }

    @Test
    void departementTest() {
        Ville ville = getVilleRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        ville.setDepartement(departementBack);
        assertThat(ville.getDepartement()).isEqualTo(departementBack);

        ville.departement(null);
        assertThat(ville.getDepartement()).isNull();
    }
}
