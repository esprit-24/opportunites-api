package esprit.opportunites.domain;

import static esprit.opportunites.domain.CandidatTestSamples.*;
import static esprit.opportunites.domain.CandidatureTestSamples.*;
import static esprit.opportunites.domain.OpportuniteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidature.class);
        Candidature candidature1 = getCandidatureSample1();
        Candidature candidature2 = new Candidature();
        assertThat(candidature1).isNotEqualTo(candidature2);

        candidature2.setId(candidature1.getId());
        assertThat(candidature1).isEqualTo(candidature2);

        candidature2 = getCandidatureSample2();
        assertThat(candidature1).isNotEqualTo(candidature2);
    }

    @Test
    void opportuniteTest() {
        Candidature candidature = getCandidatureRandomSampleGenerator();
        Opportunite opportuniteBack = getOpportuniteRandomSampleGenerator();

        candidature.setOpportunite(opportuniteBack);
        assertThat(candidature.getOpportunite()).isEqualTo(opportuniteBack);

        candidature.opportunite(null);
        assertThat(candidature.getOpportunite()).isNull();
    }

    @Test
    void candidatTest() {
        Candidature candidature = getCandidatureRandomSampleGenerator();
        Candidat candidatBack = getCandidatRandomSampleGenerator();

        candidature.setCandidat(candidatBack);
        assertThat(candidature.getCandidat()).isEqualTo(candidatBack);

        candidature.candidat(null);
        assertThat(candidature.getCandidat()).isNull();
    }
}
