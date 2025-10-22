package esprit.opportunites.web.rest;

import esprit.opportunites.domain.enumeration.NiveauEtude;
import esprit.opportunites.service.InscriptionCandidatService;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.dto.RegisterCandidatDTO;
import java.nio.file.*;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class InscriptionCandidatResource {

    private static final Logger log = LoggerFactory.getLogger(InscriptionCandidatResource.class);
    private static final String UPLOAD_DIR = "uploads/cvs";

    private final InscriptionCandidatService inscriptionCandidatService;

    public InscriptionCandidatResource(InscriptionCandidatService inscriptionCandidatService) {
        this.inscriptionCandidatService = inscriptionCandidatService;
    }

    @PostMapping(value = "/register-candidat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
        @RequestParam("login") String login,
        @RequestParam("password") String password,
        @RequestParam("email") String email,
        @RequestParam(value = "firstName", required = false) String firstName,
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "dateNaissance", required = false) String dateNaissance,
        @RequestParam(value = "niveauEtude", required = false) String niveauEtude,
        @RequestParam(value = "statutActuel", required = false) String statutActuel,
        @RequestParam(value = "cvUrl", required = false) MultipartFile cvUrl,
        @RequestParam("profilId") Long profilId
    ) {
        try {
            // Champs essentiels uniquement
            if (
                login == null ||
                login.isBlank() ||
                password == null ||
                password.isBlank() ||
                email == null ||
                email.isBlank() ||
                profilId == null
            ) {
                return ResponseEntity.badRequest().body("Champs obligatoires manquants : login, password, email, profilId.");
            }

            // Conversion optionnelle de la date
            Instant dateNaissanceInstant = null;
            if (dateNaissance != null && !dateNaissance.isBlank()) {
                try {
                    dateNaissanceInstant = Instant.parse(dateNaissance);
                } catch (Exception e) {
                    log.warn("Date de naissance invalide : {}", dateNaissance);
                }
            }

            // Conversion optionnelle du niveau d’étude
            NiveauEtude niveauEtudeEnum = null;
            if (niveauEtude != null && !niveauEtude.isBlank()) {
                try {
                    niveauEtudeEnum = NiveauEtude.valueOf(niveauEtude.toUpperCase());
                } catch (Exception e) {
                    log.warn("Niveau d'étude invalide : {}", niveauEtude);
                }
            }

            // Gestion optionnelle du fichier CV
            String savedCvUrl = null;
            if (cvUrl != null && !cvUrl.isEmpty()) {
                savedCvUrl = "/" + UPLOAD_DIR + "/" + saveCvFile(cvUrl);
            }

            // Construction du DTO
            RegisterCandidatDTO dto = new RegisterCandidatDTO();
            dto.setLogin(login);
            dto.setEmail(email);
            dto.setPassword(password);
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setDateNaissance(dateNaissanceInstant);
            dto.setNiveauEtude(niveauEtudeEnum);
            dto.setStatutActuel(statutActuel);
            dto.setProfilId(profilId);
            dto.setCvUrl(savedCvUrl);

            // Appel du service
            CandidatDTO result = inscriptionCandidatService.register(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Erreur lors de l'inscription du candidat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    private String saveCvFile(MultipartFile cvFile) throws Exception {
        String originalFilename = cvFile.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (!originalFilename.toLowerCase().matches(".*\\.(pdf|doc|docx)$")) {
            throw new IllegalArgumentException("Le CV doit être au format PDF ou Word.");
        }

        if (cvFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Le fichier CV ne doit pas dépasser 5 Mo.");
        }

        Files.copy(cvFile.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
