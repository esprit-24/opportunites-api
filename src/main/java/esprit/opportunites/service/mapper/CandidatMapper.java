package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.Domaine;
import esprit.opportunites.domain.Profil;
import esprit.opportunites.domain.User;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.dto.DomaineDTO;
import esprit.opportunites.service.dto.ProfilDTO;
import esprit.opportunites.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Candidat} and its DTO {@link CandidatDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidatMapper extends EntityMapper<CandidatDTO, Candidat> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "profil", source = "profil", qualifiedByName = "profilId")
    @Mapping(target = "domaine", source = "domaine", qualifiedByName = "domaineId")
    CandidatDTO toDto(Candidat s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("profilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfilDTO toDtoProfilId(Profil profil);

    @Named("domaineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DomaineDTO toDtoDomaineId(Domaine domaine);
}
