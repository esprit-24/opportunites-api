package esprit.opportunites.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Organisation.
 */
@Entity
@Table(name = "organisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Lob
    @Column(name = "presentation", nullable = false)
    private String presentation;

    @Column(name = "secteur_activite")
    private String secteurActivite;

    @Column(name = "logo_url")
    private String logoUrl;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "site_web")
    private String siteWeb;

    @NotNull
    @Column(name = "email_contact", nullable = false)
    private String emailContact;

    @Column(name = "telephone")
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departement" }, allowSetters = true)
    private Ville ville;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Organisation nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPresentation() {
        return this.presentation;
    }

    public Organisation presentation(String presentation) {
        this.setPresentation(presentation);
        return this;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getSecteurActivite() {
        return this.secteurActivite;
    }

    public Organisation secteurActivite(String secteurActivite) {
        this.setSecteurActivite(secteurActivite);
        return this;
    }

    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public Organisation logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Organisation adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSiteWeb() {
        return this.siteWeb;
    }

    public Organisation siteWeb(String siteWeb) {
        this.setSiteWeb(siteWeb);
        return this;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getEmailContact() {
        return this.emailContact;
    }

    public Organisation emailContact(String emailContact) {
        this.setEmailContact(emailContact);
        return this;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Organisation telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Ville getVille() {
        return this.ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Organisation ville(Ville ville) {
        this.setVille(ville);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organisation)) {
            return false;
        }
        return getId() != null && getId().equals(((Organisation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", presentation='" + getPresentation() + "'" +
            ", secteurActivite='" + getSecteurActivite() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", siteWeb='" + getSiteWeb() + "'" +
            ", emailContact='" + getEmailContact() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
