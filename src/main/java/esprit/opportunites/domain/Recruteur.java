package esprit.opportunites.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recruteur.
 */
@Entity
@Table(name = "recruteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recruteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "titre_professionnel")
    private String titreProfessionnel;

    @Lob
    @Column(name = "biographie")
    private String biographie;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ville" }, allowSetters = true)
    private Organisation organisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recruteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreProfessionnel() {
        return this.titreProfessionnel;
    }

    public Recruteur titreProfessionnel(String titreProfessionnel) {
        this.setTitreProfessionnel(titreProfessionnel);
        return this;
    }

    public void setTitreProfessionnel(String titreProfessionnel) {
        this.titreProfessionnel = titreProfessionnel;
    }

    public String getBiographie() {
        return this.biographie;
    }

    public Recruteur biographie(String biographie) {
        this.setBiographie(biographie);
        return this;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recruteur user(User user) {
        this.setUser(user);
        return this;
    }

    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Recruteur organisation(Organisation organisation) {
        this.setOrganisation(organisation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recruteur)) {
            return false;
        }
        return getId() != null && getId().equals(((Recruteur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recruteur{" +
            "id=" + getId() +
            ", titreProfessionnel='" + getTitreProfessionnel() + "'" +
            ", biographie='" + getBiographie() + "'" +
            "}";
    }
}
