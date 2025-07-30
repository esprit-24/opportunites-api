package esprit.opportunites.service.mapper;

import static esprit.opportunites.domain.VilleAsserts.*;
import static esprit.opportunites.domain.VilleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VilleMapperTest {

    private VilleMapper villeMapper;

    @BeforeEach
    void setUp() {
        villeMapper = new VilleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVilleSample1();
        var actual = villeMapper.toEntity(villeMapper.toDto(expected));
        assertVilleAllPropertiesEquals(expected, actual);
    }
}
