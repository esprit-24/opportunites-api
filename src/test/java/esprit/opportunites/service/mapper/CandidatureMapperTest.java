package esprit.opportunites.service.mapper;

import static esprit.opportunites.domain.CandidatureAsserts.*;
import static esprit.opportunites.domain.CandidatureTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CandidatureMapperTest {

    private CandidatureMapper candidatureMapper;

    @BeforeEach
    void setUp() {
        candidatureMapper = new CandidatureMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCandidatureSample1();
        var actual = candidatureMapper.toEntity(candidatureMapper.toDto(expected));
        assertCandidatureAllPropertiesEquals(expected, actual);
    }
}
