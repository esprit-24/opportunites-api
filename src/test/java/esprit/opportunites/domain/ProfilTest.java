package esprit.opportunites.domain;

import static esprit.opportunites.domain.DomaineTestSamples.*;
import static esprit.opportunites.domain.ProfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profil.class);
        Profil profil1 = getProfilSample1();
        Profil profil2 = new Profil();
        assertThat(profil1).isNotEqualTo(profil2);

        profil2.setId(profil1.getId());
        assertThat(profil1).isEqualTo(profil2);

        profil2 = getProfilSample2();
        assertThat(profil1).isNotEqualTo(profil2);
    }

    @Test
    void domaineTest() {
        Profil profil = getProfilRandomSampleGenerator();
        Domaine domaineBack = getDomaineRandomSampleGenerator();

        profil.setDomaine(domaineBack);
        assertThat(profil.getDomaine()).isEqualTo(domaineBack);

        profil.domaine(null);
        assertThat(profil.getDomaine()).isNull();
    }
}
