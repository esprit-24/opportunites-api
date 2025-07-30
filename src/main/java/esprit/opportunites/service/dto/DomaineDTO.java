package esprit.opportunites.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Domaine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DomaineDTO implements Serializable {

    private Long id;

    @NotNull
    private String intitule;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomaineDTO)) {
            return false;
        }

        DomaineDTO domaineDTO = (DomaineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, domaineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DomaineDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
