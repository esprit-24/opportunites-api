package esprit.opportunites.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomaineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomaineDTO.class);
        DomaineDTO domaineDTO1 = new DomaineDTO();
        domaineDTO1.setId(1L);
        DomaineDTO domaineDTO2 = new DomaineDTO();
        assertThat(domaineDTO1).isNotEqualTo(domaineDTO2);
        domaineDTO2.setId(domaineDTO1.getId());
        assertThat(domaineDTO1).isEqualTo(domaineDTO2);
        domaineDTO2.setId(2L);
        assertThat(domaineDTO1).isNotEqualTo(domaineDTO2);
        domaineDTO1.setId(null);
        assertThat(domaineDTO1).isNotEqualTo(domaineDTO2);
    }
}
