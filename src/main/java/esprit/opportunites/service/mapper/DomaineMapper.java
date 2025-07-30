package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.service.dto.DomaineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Domaine} and its DTO {@link DomaineDTO}.
 */
@Mapper(componentModel = "spring")
public interface DomaineMapper extends EntityMapper<DomaineDTO, Domaine> {}
