package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Domaine;
import esprit.opportunites.domain.Profil;
import esprit.opportunites.service.dto.DomaineDTO;
import esprit.opportunites.service.dto.ProfilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {
    @Mapping(target = "domaine", source = "domaine", qualifiedByName = "domaineId")
    ProfilDTO toDto(Profil s);

    @Named("domaineId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "id", source = "id"), @Mapping(target = "intitule", source = "intitule") })
    DomaineDTO toDtoDomaineId(Domaine domaine);
}
