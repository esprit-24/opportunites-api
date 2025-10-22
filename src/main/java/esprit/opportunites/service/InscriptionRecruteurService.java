package esprit.opportunites.service;

import esprit.opportunites.domain.Authority;
import esprit.opportunites.domain.Recruteur;
import esprit.opportunites.domain.User;
import esprit.opportunites.repository.AuthorityRepository;
import esprit.opportunites.repository.OrganisationRepository;
import esprit.opportunites.repository.RecruteurRepository;
import esprit.opportunites.repository.UserRepository;
import esprit.opportunites.service.dto.RecruteurDTO;
import esprit.opportunites.service.dto.RegisterRecruteurDTO;
import esprit.opportunites.service.mapper.RecruteurMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InscriptionRecruteurService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecruteurRepository recruteurRepository;
    private final OrganisationRepository organisationRepository;
    private final RecruteurMapper recruteurMapper;

    public InscriptionRecruteurService(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        RecruteurRepository recruteurRepository,
        OrganisationRepository organisationRepository,
        RecruteurMapper recruteurMapper
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.recruteurRepository = recruteurRepository;
        this.organisationRepository = organisationRepository;
        this.recruteurMapper = recruteurMapper;
    }

    public RecruteurDTO register(RegisterRecruteurDTO dto) {
        // Vérifier l'unicité du login et de l'email
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("Login déjà utilisé");
        }
        if (dto.getEmail() != null && userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // Créer le User
        User user = new User();

        user.setLogin(dto.getLogin().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setLangKey("fr");
        user.setActivated(true);

        // Supprimer tous les rôles existants
        user.getAuthorities().clear();

        // Récupérer le rôle Recruteur
        Authority roleRecruteur = authorityRepository
            .findById("ROLE_RECRUTEUR")
            .orElseThrow(() -> new IllegalStateException("Rôle ROLE_RECRUTEUR non trouvé"));

        // Ajouter le rôle Recruteur
        user.getAuthorities().add(roleRecruteur);

        // Sauvegarde du User
        user = userRepository.save(user);

        // Créer le Recruteur
        Recruteur recruteur = new Recruteur();

        recruteur.setUser(user);
        recruteur.setTitreProfessionnel(dto.getTitreProfessionnel());
        recruteur.setBiographie(dto.getBiographie());

        if (dto.getOrganisationId() != null) {
            organisationRepository.findById(dto.getOrganisationId()).ifPresent(recruteur::setOrganisation);
        }

        // Sauvegarder le Recruteur
        recruteur = recruteurRepository.save(recruteur);

        return recruteurMapper.toDto(recruteur);
    }
}
