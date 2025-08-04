package esprit.opportunites.service.dto;

import esprit.opportunites.domain.enumeration.NiveauEtude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class RegisterCandidatDTO {

    // Infos User
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @Email
    private String email;

    private String firstName;
    private String lastName;

    // Infos Candidat
    private Instant dateNaissance;
    private NiveauEtude niveauEtude;
    private String statutActuel;
    private String cvUrl;
    private Long profilId;

    // Getters and Setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public NiveauEtude getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(NiveauEtude niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getStatutActuel() {
        return statutActuel;
    }

    public void setStatutActuel(String statutActuel) {
        this.statutActuel = statutActuel;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public Long getProfilId() {
        return profilId;
    }

    public void setProfilId(Long profilId) {
        this.profilId = profilId;
    }
}
