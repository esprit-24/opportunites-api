package esprit.opportunites.service.mapper;

import static esprit.opportunites.domain.DomaineAsserts.*;
import static esprit.opportunites.domain.DomaineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DomaineMapperTest {

    private DomaineMapper domaineMapper;

    @BeforeEach
    void setUp() {
        domaineMapper = new DomaineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDomaineSample1();
        var actual = domaineMapper.toEntity(domaineMapper.toDto(expected));
        assertDomaineAllPropertiesEquals(expected, actual);
    }
}
