package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.service.dto.DomaineDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomaineMapper extends EntityMapper<DomaineDTO, Domaine> {}
