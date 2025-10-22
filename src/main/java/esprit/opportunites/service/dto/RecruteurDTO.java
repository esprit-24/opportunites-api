package esprit.opportunites.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Recruteur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruteurDTO implements Serializable {

    private Long id;

    private String titreProfessionnel;

    private String biographie;

    private UserDTO user;

    private OrganisationDTO organisation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreProfessionnel() {
        return titreProfessionnel;
    }

    public void setTitreProfessionnel(String titreProfessionnel) {
        this.titreProfessionnel = titreProfessionnel;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public OrganisationDTO getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationDTO organisation) {
        this.organisation = organisation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruteurDTO)) {
            return false;
        }

        RecruteurDTO recruteurDTO = (RecruteurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruteurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruteurDTO{" +
            "id=" + getId() +
            ", titreProfessionnel='" + getTitreProfessionnel() + "'" +
            ", biographie='" + getBiographie() + "'" +
            ", user=" + getUser() +
            ", organisation=" + getOrganisation() +
            "}";
    }
}
