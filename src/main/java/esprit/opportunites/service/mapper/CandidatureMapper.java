package esprit.opportunites.service.mapper;

import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.Candidature;
import esprit.opportunites.domain.Opportunite;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.dto.CandidatureDTO;
import esprit.opportunites.service.dto.OpportuniteDTO;
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
    @Mapping(target = "id", source = "id")
    OpportuniteDTO toDtoOpportuniteId(Opportunite opportunite);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CandidatDTO toDtoCandidatId(Candidat candidat);
}
