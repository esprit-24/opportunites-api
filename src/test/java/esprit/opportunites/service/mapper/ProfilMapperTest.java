package esprit.opportunites.service.mapper;

import static esprit.opportunites.domain.ProfilAsserts.*;
import static esprit.opportunites.domain.ProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfilMapperTest {

    private ProfilMapper profilMapper;

    @BeforeEach
    void setUp() {
        profilMapper = new ProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfilSample1();
        var actual = profilMapper.toEntity(profilMapper.toDto(expected));
        assertProfilAllPropertiesEquals(expected, actual);
    }
}
