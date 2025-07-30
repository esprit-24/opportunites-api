package esprit.opportunites.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Ville} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VilleDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private DepartementDTO departement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public DepartementDTO getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDTO departement) {
        this.departement = departement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VilleDTO)) {
            return false;
        }

        VilleDTO villeDTO = (VilleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, villeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VilleDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", departement=" + getDepartement() +
            "}";
    }
}
