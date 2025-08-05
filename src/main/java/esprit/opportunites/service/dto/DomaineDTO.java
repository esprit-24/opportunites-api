package esprit.opportunites.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class DomaineDTO implements Serializable {

    private Long id;

    @NotNull
    private String intitule;

    private String description;

    // Getters et setters

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

    // equals et hashCode sur id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomaineDTO)) return false;
        DomaineDTO that = (DomaineDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
