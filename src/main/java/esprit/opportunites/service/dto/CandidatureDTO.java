package esprit.opportunites.service.dto;

import esprit.opportunites.domain.enumeration.StatutCandidature;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Candidature} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidatureDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant datePostulation;

    @NotNull
    private StatutCandidature statutCandidature;

    private String lettreMotivation;

    private OpportuniteDTO opportunite;

    private CandidatDTO candidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatePostulation() {
        return datePostulation;
    }

    public void setDatePostulation(Instant datePostulation) {
        this.datePostulation = datePostulation;
    }

    public StatutCandidature getStatutCandidature() {
        return statutCandidature;
    }

    public void setStatutCandidature(StatutCandidature statutCandidature) {
        this.statutCandidature = statutCandidature;
    }

    public String getLettreMotivation() {
        return lettreMotivation;
    }

    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }

    public OpportuniteDTO getOpportunite() {
        return opportunite;
    }

    public void setOpportunite(OpportuniteDTO opportunite) {
        this.opportunite = opportunite;
    }

    public CandidatDTO getCandidat() {
        return candidat;
    }

    public void setCandidat(CandidatDTO candidat) {
        this.candidat = candidat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidatureDTO)) {
            return false;
        }

        CandidatureDTO candidatureDTO = (CandidatureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidatureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidatureDTO{" +
            "id=" + getId() +
            ", datePostulation='" + getDatePostulation() + "'" +
            ", statutCandidature='" + getStatutCandidature() + "'" +
            ", lettreMotivation='" + getLettreMotivation() + "'" +
            ", opportunite=" + getOpportunite() +
            ", candidat=" + getCandidat() +
            "}";
    }
}
