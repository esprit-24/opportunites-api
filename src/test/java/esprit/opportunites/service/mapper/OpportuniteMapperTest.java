package esprit.opportunites.service.mapper;

import static esprit.opportunites.domain.OpportuniteAsserts.*;
import static esprit.opportunites.domain.OpportuniteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpportuniteMapperTest {

    private OpportuniteMapper opportuniteMapper;

    @BeforeEach
    void setUp() {
        opportuniteMapper = new OpportuniteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOpportuniteSample1();
        var actual = opportuniteMapper.toEntity(opportuniteMapper.toDto(expected));
        assertOpportuniteAllPropertiesEquals(expected, actual);
    }
}
