package esprit.opportunites.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import esprit.opportunites.domain.enumeration.NiveauEtude;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Candidat.
 */
@Entity
@Table(name = "candidat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Candidat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "date_naissance")
    private Instant dateNaissance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_etude", nullable = false)
    private NiveauEtude niveauEtude;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "statut_actuel")
    private String statutActuel;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "domaine" }, allowSetters = true)
    private Profil profil;

    @ManyToOne(fetch = FetchType.LAZY)
    private Domaine domaine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Candidat dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public NiveauEtude getNiveauEtude() {
        return this.niveauEtude;
    }

    public Candidat niveauEtude(NiveauEtude niveauEtude) {
        this.setNiveauEtude(niveauEtude);
        return this;
    }

    public void setNiveauEtude(NiveauEtude niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getCvUrl() {
        return this.cvUrl;
    }

    public Candidat cvUrl(String cvUrl) {
        this.setCvUrl(cvUrl);
        return this;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getStatutActuel() {
        return this.statutActuel;
    }

    public Candidat statutActuel(String statutActuel) {
        this.setStatutActuel(statutActuel);
        return this;
    }

    public void setStatutActuel(String statutActuel) {
        this.statutActuel = statutActuel;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Candidat user(User user) {
        this.setUser(user);
        return this;
    }

    public Profil getProfil() {
        return this.profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public Candidat profil(Profil profil) {
        this.setProfil(profil);
        return this;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public Candidat domaine(Domaine domaine) {
        this.setDomaine(domaine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidat)) {
            return false;
        }
        return getId() != null && getId().equals(((Candidat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidat{" +
            "id=" + getId() +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", niveauEtude='" + getNiveauEtude() + "'" +
            ", cvUrl='" + getCvUrl() + "'" +
            ", statutActuel='" + getStatutActuel() + "'" +
            "}";
    }
}
