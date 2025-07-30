package esprit.opportunites.service.mapper;

import static esprit.opportunites.domain.RecruteurAsserts.*;
import static esprit.opportunites.domain.RecruteurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruteurMapperTest {

    private RecruteurMapper recruteurMapper;

    @BeforeEach
    void setUp() {
        recruteurMapper = new RecruteurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecruteurSample1();
        var actual = recruteurMapper.toEntity(recruteurMapper.toDto(expected));
        assertRecruteurAllPropertiesEquals(expected, actual);
    }
}
