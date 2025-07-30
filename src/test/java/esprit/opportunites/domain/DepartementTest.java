package esprit.opportunites.domain;

import static esprit.opportunites.domain.DepartementTestSamples.*;
import static esprit.opportunites.domain.RegionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departement.class);
        Departement departement1 = getDepartementSample1();
        Departement departement2 = new Departement();
        assertThat(departement1).isNotEqualTo(departement2);

        departement2.setId(departement1.getId());
        assertThat(departement1).isEqualTo(departement2);

        departement2 = getDepartementSample2();
        assertThat(departement1).isNotEqualTo(departement2);
    }

    @Test
    void regionTest() {
        Departement departement = getDepartementRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        departement.setRegion(regionBack);
        assertThat(departement.getRegion()).isEqualTo(regionBack);

        departement.region(null);
        assertThat(departement.getRegion()).isNull();
    }
}
