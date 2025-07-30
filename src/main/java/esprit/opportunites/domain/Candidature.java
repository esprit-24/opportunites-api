package esprit.opportunites.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import esprit.opportunites.domain.enumeration.StatutCandidature;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Candidature.
 */
@Entity
@Table(name = "candidature")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Candidature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_postulation", nullable = false)
    private Instant datePostulation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_candidature")
    private StatutCandidature statutCandidature;

    @Lob
    @Column(name = "lettre_motivation")
    private String lettreMotivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "domaine", "organisation", "ville" }, allowSetters = true)
    private Opportunite opportunite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "profil", "domaine" }, allowSetters = true)
    private Candidat candidat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidature id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatePostulation() {
        return this.datePostulation;
    }

    public Candidature datePostulation(Instant datePostulation) {
        this.setDatePostulation(datePostulation);
        return this;
    }

    public void setDatePostulation(Instant datePostulation) {
        this.datePostulation = datePostulation;
    }

    public StatutCandidature getStatutCandidature() {
        return this.statutCandidature;
    }

    public Candidature statutCandidature(StatutCandidature statutCandidature) {
        this.setStatutCandidature(statutCandidature);
        return this;
    }

    public void setStatutCandidature(StatutCandidature statutCandidature) {
        this.statutCandidature = statutCandidature;
    }

    public String getLettreMotivation() {
        return this.lettreMotivation;
    }

    public Candidature lettreMotivation(String lettreMotivation) {
        this.setLettreMotivation(lettreMotivation);
        return this;
    }

    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }

    public Opportunite getOpportunite() {
        return this.opportunite;
    }

    public void setOpportunite(Opportunite opportunite) {
        this.opportunite = opportunite;
    }

    public Candidature opportunite(Opportunite opportunite) {
        this.setOpportunite(opportunite);
        return this;
    }

    public Candidat getCandidat() {
        return this.candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public Candidature candidat(Candidat candidat) {
        this.setCandidat(candidat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidature)) {
            return false;
        }
        return getId() != null && getId().equals(((Candidature) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidature{" +
            "id=" + getId() +
            ", datePostulation='" + getDatePostulation() + "'" +
            ", statutCandidature='" + getStatutCandidature() + "'" +
            ", lettreMotivation='" + getLettreMotivation() + "'" +
            "}";
    }
}
