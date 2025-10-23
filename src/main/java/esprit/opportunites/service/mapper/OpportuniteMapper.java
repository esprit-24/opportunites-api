package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.domain.Opportunite;
import esprit.opportunites.domain.Organisation;
import esprit.opportunites.domain.Ville;
import esprit.opportunites.service.dto.DomaineDTO;
import esprit.opportunites.service.dto.OpportuniteDTO;
import esprit.opportunites.service.dto.OrganisationDTO;
import esprit.opportunites.service.dto.VilleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Opportunite} and its DTO {@link OpportuniteDTO}.
 */
@Mapper(componentModel = "spring")
public interface OpportuniteMapper extends EntityMapper<OpportuniteDTO, Opportunite> {
    @Mapping(target = "domaine", source = "domaine", qualifiedByName = "domaineId")
    @Mapping(target = "organisation", source = "organisation", qualifiedByName = "organisationId")
    @Mapping(target = "ville", source = "ville", qualifiedByName = "villeId")
    OpportuniteDTO toDto(Opportunite s);

    @Named("domaineId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "id", source = "id"), @Mapping(target = "intitule", source = "intitule") })
    DomaineDTO toDtoDomaineId(Domaine domaine);

    @Named("organisationId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "nom", source = "nom"),
            @Mapping(target = "secteurActivite", source = "secteurActivite"),
        }
    )
    OrganisationDTO toDtoOrganisationId(Organisation organisation);

    @Named("villeId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "id", source = "id"), @Mapping(target = "nom", source = "nom") })
    VilleDTO toDtoVilleId(Ville ville);
}
