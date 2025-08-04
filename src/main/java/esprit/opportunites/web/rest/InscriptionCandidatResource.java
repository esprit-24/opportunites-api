package esprit.opportunites.web.rest;

import esprit.opportunites.domain.enumeration.NiveauEtude;
import esprit.opportunites.service.InscriptionCandidatService;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.dto.RegisterCandidatDTO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class InscriptionCandidatResource {

    private static final Logger log = LoggerFactory.getLogger(InscriptionCandidatResource.class);
    private static final String UPLOAD_DIR = "uploads/cvs"; // À externaliser dans les properties

    private final InscriptionCandidatService inscriptionCandidatService;

    public InscriptionCandidatResource(InscriptionCandidatService inscriptionCandidatService) {
        this.inscriptionCandidatService = inscriptionCandidatService;
    }

    @PostMapping(value = "/register-candidat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
        @RequestParam("login") String login,
        @RequestParam("password") String password,
        @RequestParam("email") String email,
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("dateNaissance") String dateNaissance,
        @RequestParam("niveauEtude") String niveauEtude,
        @RequestParam("statutActuel") String statutActuel,
        @RequestParam("cvUrl") MultipartFile cvUrl,
        @RequestParam("profilId") Long profilId
    ) {
        try {
            // Validation basique
            if (
                login == null ||
                login.isBlank() ||
                password == null ||
                password.isBlank() ||
                email == null ||
                email.isBlank() ||
                firstName == null ||
                firstName.isBlank() ||
                lastName == null ||
                lastName.isBlank() ||
                dateNaissance == null ||
                dateNaissance.isBlank() ||
                niveauEtude == null ||
                niveauEtude.isBlank() ||
                statutActuel == null ||
                statutActuel.isBlank() ||
                profilId == null ||
                cvUrl == null ||
                cvUrl.isEmpty()
            ) {
                return ResponseEntity.badRequest().body("Tous les champs sont obligatoires.");
            }

            // 1. Enregistrer le fichier CV sur disque
            String filename = saveCvFile(cvUrl);

            // 2. Convertir la dateNaissance en Instant
            Instant dateNaissanceInstant = Instant.parse(dateNaissance);

            // 3. Convertir le niveauEtude en Enum
            NiveauEtude niveauEtudeEnum = NiveauEtude.valueOf(niveauEtude);

            // 4. Construire le DTO avec le chemin du fichier
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
            dto.setCvUrl("/" + UPLOAD_DIR + "/" + filename);

            // 5. Appeler le service
            CandidatDTO result = inscriptionCandidatService.register(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Erreur lors de l'inscription du candidat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Erreur lors de l'inscription du candidat : " + e.getMessage()
            );
        }
    }

    /**
     * Sauvegarde le fichier CV sur disque et retourne le nom du fichier sauvegardé.
     */
    private String saveCvFile(MultipartFile cvFile) throws Exception {
        String originalFilename = cvFile.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(filename);
        // Sécurité basique : vérifier l'extension
        if (
            !originalFilename.toLowerCase().endsWith(".pdf") &&
            !originalFilename.toLowerCase().endsWith(".doc") &&
            !originalFilename.toLowerCase().endsWith(".docx")
        ) {
            throw new IllegalArgumentException("Le CV doit être au format PDF ou Word.");
        }
        // Limite de taille (exemple : 5 Mo)
        if (cvFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Le fichier CV ne doit pas dépasser 5 Mo.");
        }
        Files.copy(cvFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
