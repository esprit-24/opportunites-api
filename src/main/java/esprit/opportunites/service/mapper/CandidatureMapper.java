package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.Candidature;
import esprit.opportunites.domain.Opportunite;
import esprit.opportunites.domain.User;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.dto.CandidatureDTO;
import esprit.opportunites.service.dto.OpportuniteDTO;
import esprit.opportunites.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Candidature} and its DTO {@link CandidatureDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidatureMapper extends EntityMapper<CandidatureDTO, Candidature> {
    @Mapping(target = "opportunite", source = "opportunite", qualifiedByName = "opportuniteId")
    @Mapping(target = "candidat", source = "candidat", qualifiedByName = "candidatId")
    CandidatureDTO toDto(Candidature s);

    @Named("opportuniteId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "id", source = "id"), @Mapping(target = "titre", source = "titre") })
    OpportuniteDTO toDtoOpportuniteId(Opportunite opportunite);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mappings({ @Mapping(target = "id", source = "id"), @Mapping(target = "user", source = "user", qualifiedByName = "userSimple") })
    CandidatDTO toDtoCandidatId(Candidat candidat);

    @Named("userSimple")
    @BeanMapping(ignoreByDefault = true)
    @Mappings(
        {
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "login", source = "login"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
        }
    )
    UserDTO toDtoUserSimple(User user);
}
