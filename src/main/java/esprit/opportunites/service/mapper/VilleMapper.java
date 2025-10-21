package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Departement;
import esprit.opportunites.domain.Ville;
import esprit.opportunites.service.dto.DepartementDTO;
import esprit.opportunites.service.dto.VilleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ville} and its DTO {@link VilleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VilleMapper extends EntityMapper<VilleDTO, Ville> {
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementId")
    VilleDTO toDto(Ville s);

    @Named("departementId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "id", source = "id"), @Mapping(target = "nom", source = "nom") })
    DepartementDTO toDtoDepartementId(Departement departement);
}
