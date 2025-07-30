package esprit.opportunites.service.dto;

import esprit.opportunites.domain.enumeration.NiveauEtude;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Candidat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidatDTO implements Serializable {

    private Long id;

    private Instant dateNaissance;

    @NotNull
    private NiveauEtude niveauEtude;

    private String cvUrl;

    private String statutActuel;

    private UserDTO user;

    private ProfilDTO profil;

    private DomaineDTO domaine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public NiveauEtude getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(NiveauEtude niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getStatutActuel() {
        return statutActuel;
    }

    public void setStatutActuel(String statutActuel) {
        this.statutActuel = statutActuel;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ProfilDTO getProfil() {
        return profil;
    }

    public void setProfil(ProfilDTO profil) {
        this.profil = profil;
    }

    public DomaineDTO getDomaine() {
        return domaine;
    }

    public void setDomaine(DomaineDTO domaine) {
        this.domaine = domaine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidatDTO)) {
            return false;
        }

        CandidatDTO candidatDTO = (CandidatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidatDTO{" +
            "id=" + getId() +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", niveauEtude='" + getNiveauEtude() + "'" +
            ", cvUrl='" + getCvUrl() + "'" +
            ", statutActuel='" + getStatutActuel() + "'" +
            ", user=" + getUser() +
            ", profil=" + getProfil() +
            ", domaine=" + getDomaine() +
            "}";
    }
}
