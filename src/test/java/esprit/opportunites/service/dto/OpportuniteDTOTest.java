package esprit.opportunites.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import esprit.opportunites.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpportuniteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpportuniteDTO.class);
        OpportuniteDTO opportuniteDTO1 = new OpportuniteDTO();
        opportuniteDTO1.setId(1L);
        OpportuniteDTO opportuniteDTO2 = new OpportuniteDTO();
        assertThat(opportuniteDTO1).isNotEqualTo(opportuniteDTO2);
        opportuniteDTO2.setId(opportuniteDTO1.getId());
        assertThat(opportuniteDTO1).isEqualTo(opportuniteDTO2);
        opportuniteDTO2.setId(2L);
        assertThat(opportuniteDTO1).isNotEqualTo(opportuniteDTO2);
        opportuniteDTO1.setId(null);
        assertThat(opportuniteDTO1).isNotEqualTo(opportuniteDTO2);
    }
}
