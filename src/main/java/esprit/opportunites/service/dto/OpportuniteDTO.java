package esprit.opportunites.service.dto;

import esprit.opportunites.domain.enumeration.NiveauEtude;
import esprit.opportunites.domain.enumeration.Statut;
import esprit.opportunites.domain.enumeration.TypeContrat;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Opportunite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OpportuniteDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    private String description;

    @NotNull
    private Instant dateDebut;

    private Instant dateFin;

    private String adresse;

    private NiveauEtude niveauEtudeRequis;

    @NotNull
    private Integer nombrePostes;

    private BigDecimal salaire;

    private Statut statut;

    private TypeContrat typeContrat;

    private DomaineDTO domaine;

    private OrganisationDTO organisation;

    private VilleDTO ville;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public NiveauEtude getNiveauEtudeRequis() {
        return niveauEtudeRequis;
    }

    public void setNiveauEtudeRequis(NiveauEtude niveauEtudeRequis) {
        this.niveauEtudeRequis = niveauEtudeRequis;
    }

    public Integer getNombrePostes() {
        return nombrePostes;
    }

    public void setNombrePostes(Integer nombrePostes) {
        this.nombrePostes = nombrePostes;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public DomaineDTO getDomaine() {
        return domaine;
    }

    public void setDomaine(DomaineDTO domaine) {
        this.domaine = domaine;
    }

    public OrganisationDTO getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationDTO organisation) {
        this.organisation = organisation;
    }

    public VilleDTO getVille() {
        return ville;
    }

    public void setVille(VilleDTO ville) {
        this.ville = ville;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpportuniteDTO)) {
            return false;
        }

        OpportuniteDTO opportuniteDTO = (OpportuniteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, opportuniteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpportuniteDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", niveauEtudeRequis='" + getNiveauEtudeRequis() + "'" +
            ", nombrePostes=" + getNombrePostes() +
            ", salaire=" + getSalaire() +
            ", statut='" + getStatut() + "'" +
            ", typeContrat='" + getTypeContrat() + "'" +
            ", domaine=" + getDomaine() +
            ", organisation=" + getOrganisation() +
            ", ville=" + getVille() +
            "}";
    }
}
