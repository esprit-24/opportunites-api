package esprit.opportunites.web.rest;

import esprit.opportunites.service.InscriptionRecruteurService;
import esprit.opportunites.service.dto.RecruteurDTO;
import esprit.opportunites.service.dto.RegisterRecruteurDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the registration of a Recruteur.
 */
@RestController
@RequestMapping("/api")
public class InscriptionRecruteurResource {

    private static final Logger log = LoggerFactory.getLogger(InscriptionRecruteurResource.class);

    private final InscriptionRecruteurService inscriptionRecruteurService;

    public InscriptionRecruteurResource(InscriptionRecruteurService inscriptionRecruteurService) {
        this.inscriptionRecruteurService = inscriptionRecruteurService;
    }

    /**
     * {@code POST  /register-recruteur} : Register a new recruteur.
     *
     * @param login             le login du recruteur
     * @param password          le mot de passe
     * @param email             l'adresse email
     * @param firstName         prénom du recruteur (optionnel)
     * @param lastName          nom du recruteur (optionnel)
     * @param titreProfessionnel titre professionnel (optionnel)
     * @param biographie        biographie (optionnelle)
     * @param organisationId    identifiant de l'organisation (optionnel)
     * @return le Recruteur créé
     */
    @PostMapping("/register-recruteur")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> registerRecruteur(
        @RequestParam("login") String login,
        @RequestParam("password") String password,
        @RequestParam("email") String email,
        @RequestParam(value = "firstName", required = false) String firstName,
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "titreProfessionnel", required = false) String titreProfessionnel,
        @RequestParam(value = "biographie", required = false) String biographie,
        @RequestParam(value = "organisationId", required = false) Long organisationId
    ) {
        log.debug("REST request to register a new Recruteur: {}", login);

        try {
            // Validation minimale
            if (login == null || login.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body("Champs obligatoires manquants : login, password, email.");
            }

            // Construction du DTO
            RegisterRecruteurDTO dto = new RegisterRecruteurDTO();
            dto.setLogin(login);
            dto.setPassword(password);
            dto.setEmail(email);
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setTitreProfessionnel(titreProfessionnel);
            dto.setBiographie(biographie);
            dto.setOrganisationId(organisationId);

            // Appel du service
            RecruteurDTO result = inscriptionRecruteurService.register(dto);

            // Retour de la réponse
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            log.warn("Erreur de validation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de l'inscription du recruteur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }
}
