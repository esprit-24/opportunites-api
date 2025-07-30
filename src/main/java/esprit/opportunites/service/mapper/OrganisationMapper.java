package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Organisation;
import esprit.opportunites.domain.Ville;
import esprit.opportunites.service.dto.OrganisationDTO;
import esprit.opportunites.service.dto.VilleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organisation} and its DTO {@link OrganisationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganisationMapper extends EntityMapper<OrganisationDTO, Organisation> {
    @Mapping(target = "ville", source = "ville", qualifiedByName = "villeId")
    OrganisationDTO toDto(Organisation s);

    @Named("villeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VilleDTO toDtoVilleId(Ville ville);
}
