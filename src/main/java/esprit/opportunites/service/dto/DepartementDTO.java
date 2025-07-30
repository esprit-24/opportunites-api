package esprit.opportunites.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Departement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartementDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private RegionDTO region;

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

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        DepartementDTO departementDTO = (DepartementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", region=" + getRegion() +
            "}";
    }
}
