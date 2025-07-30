package esprit.opportunites.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import esprit.opportunites.domain.enumeration.NiveauEtude;
import esprit.opportunites.domain.enumeration.Statut;
import esprit.opportunites.domain.enumeration.TypeContrat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Opportunite.
 */
@Entity
@Table(name = "opportunite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Opportunite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @Column(name = "adresse")
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_etude_requis")
    private NiveauEtude niveauEtudeRequis;

    @NotNull
    @Column(name = "nombre_postes", nullable = false)
    private Integer nombrePostes;

    @Column(name = "salaire", precision = 21, scale = 2)
    private BigDecimal salaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_contrat")
    private TypeContrat typeContrat;

    @ManyToOne(fetch = FetchType.LAZY)
    private Domaine domaine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ville" }, allowSetters = true)
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departement" }, allowSetters = true)
    private Ville ville;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Opportunite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Opportunite titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Opportunite description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Opportunite dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Opportunite dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Opportunite adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public NiveauEtude getNiveauEtudeRequis() {
        return this.niveauEtudeRequis;
    }

    public Opportunite niveauEtudeRequis(NiveauEtude niveauEtudeRequis) {
        this.setNiveauEtudeRequis(niveauEtudeRequis);
        return this;
    }

    public void setNiveauEtudeRequis(NiveauEtude niveauEtudeRequis) {
        this.niveauEtudeRequis = niveauEtudeRequis;
    }

    public Integer getNombrePostes() {
        return this.nombrePostes;
    }

    public Opportunite nombrePostes(Integer nombrePostes) {
        this.setNombrePostes(nombrePostes);
        return this;
    }

    public void setNombrePostes(Integer nombrePostes) {
        this.nombrePostes = nombrePostes;
    }

    public BigDecimal getSalaire() {
        return this.salaire;
    }

    public Opportunite salaire(BigDecimal salaire) {
        this.setSalaire(salaire);
        return this;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public Statut getStatut() {
        return this.statut;
    }

    public Opportunite statut(Statut statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public TypeContrat getTypeContrat() {
        return this.typeContrat;
    }

    public Opportunite typeContrat(TypeContrat typeContrat) {
        this.setTypeContrat(typeContrat);
        return this;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public Opportunite domaine(Domaine domaine) {
        this.setDomaine(domaine);
        return this;
    }

    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Opportunite organisation(Organisation organisation) {
        this.setOrganisation(organisation);
        return this;
    }

    public Ville getVille() {
        return this.ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Opportunite ville(Ville ville) {
        this.setVille(ville);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opportunite)) {
            return false;
        }
        return getId() != null && getId().equals(((Opportunite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Opportunite{" +
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
            "}";
    }
}
