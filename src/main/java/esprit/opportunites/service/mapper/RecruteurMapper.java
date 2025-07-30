package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Organisation;
import esprit.opportunites.domain.Recruteur;
import esprit.opportunites.domain.User;
import esprit.opportunites.service.dto.OrganisationDTO;
import esprit.opportunites.service.dto.RecruteurDTO;
import esprit.opportunites.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recruteur} and its DTO {@link RecruteurDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruteurMapper extends EntityMapper<RecruteurDTO, Recruteur> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "organisation", source = "organisation", qualifiedByName = "organisationId")
    RecruteurDTO toDto(Recruteur s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("organisationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganisationDTO toDtoOrganisationId(Organisation organisation);
}
