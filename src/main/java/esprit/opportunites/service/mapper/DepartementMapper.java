package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Departement;
import esprit.opportunites.domain.Region;
import esprit.opportunites.service.dto.DepartementDTO;
import esprit.opportunites.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    DepartementDTO toDto(Departement s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);
}
