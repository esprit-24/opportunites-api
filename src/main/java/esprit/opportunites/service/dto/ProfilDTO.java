package esprit.opportunites.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link esprit.opportunites.domain.Profil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfilDTO implements Serializable {

    private Long id;

    @NotNull
    private String intitule;

    private DomaineDTO domaine;

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
        if (!(o instanceof ProfilDTO)) {
            return false;
        }

        ProfilDTO profilDTO = (ProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", domaine=" + getDomaine() +
            "}";
    }
}
