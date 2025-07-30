package esprit.opportunites.domain;

import static esprit.opportunites.domain.CandidatTestSamples.*;
import static esprit.opportunites.domain.DomaineTestSamples.*;
import static esprit.opportunites.domain.ProfilTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidat.class);
        Candidat candidat1 = getCandidatSample1();
        Candidat candidat2 = new Candidat();
        assertThat(candidat1).isNotEqualTo(candidat2);

        candidat2.setId(candidat1.getId());
        assertThat(candidat1).isEqualTo(candidat2);

        candidat2 = getCandidatSample2();
        assertThat(candidat1).isNotEqualTo(candidat2);
    }

    @Test
    void profilTest() {
        Candidat candidat = getCandidatRandomSampleGenerator();
        Profil profilBack = getProfilRandomSampleGenerator();

        candidat.setProfil(profilBack);
        assertThat(candidat.getProfil()).isEqualTo(profilBack);

        candidat.profil(null);
        assertThat(candidat.getProfil()).isNull();
    }

    @Test
    void domaineTest() {
        Candidat candidat = getCandidatRandomSampleGenerator();
        Domaine domaineBack = getDomaineRandomSampleGenerator();

        candidat.setDomaine(domaineBack);
        assertThat(candidat.getDomaine()).isEqualTo(domaineBack);

        candidat.domaine(null);
        assertThat(candidat.getDomaine()).isNull();
    }
}
