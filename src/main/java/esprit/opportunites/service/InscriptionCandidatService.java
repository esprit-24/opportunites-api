package esprit.opportunites.service;

import esprit.opportunites.domain.Authority;
import esprit.opportunites.domain.Candidat;
import esprit.opportunites.domain.User;
import esprit.opportunites.repository.AuthorityRepository;
import esprit.opportunites.repository.CandidatRepository;
import esprit.opportunites.repository.DomaineRepository;
import esprit.opportunites.repository.ProfilRepository;
import esprit.opportunites.repository.UserRepository;
import esprit.opportunites.service.dto.CandidatDTO;
import esprit.opportunites.service.dto.RegisterCandidatDTO;
import esprit.opportunites.service.mapper.CandidatMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InscriptionCandidatService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final CandidatRepository candidatRepository;
    private final ProfilRepository profilRepository;
    private final CandidatMapper candidatMapper;

    public InscriptionCandidatService(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        CandidatRepository candidatRepository,
        DomaineRepository domaineRepository,
        ProfilRepository profilRepository,
        CandidatMapper candidatMapper
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.candidatRepository = candidatRepository;
        this.profilRepository = profilRepository;
        this.candidatMapper = candidatMapper;
    }

    public CandidatDTO register(RegisterCandidatDTO dto) {
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("Login déjà utilisé");
        }
        if (dto.getEmail() != null && userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // Create a new user
        User user = new User();

        user.setLogin(dto.getLogin().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setLangKey("fr");
        user.setActivated(false);

        // Supprimer tous les rôles existants
        user.getAuthorities().clear();

        // Récupérer le rôle candidat
        Authority candidatRole = authorityRepository
            .findById("ROLE_CANDIDAT")
            .orElseThrow(() -> new IllegalStateException("Role ROLE_CANDIDAT non trouvé"));

        // Ajouter uniquement le rôle candidat
        user.getAuthorities().add(candidatRole);

        user = userRepository.save(user);

        // Create a new candidat
        Candidat candidat = new Candidat();

        candidat.setUser(user);
        candidat.setDateNaissance(dto.getDateNaissance());
        candidat.setNiveauEtude(dto.getNiveauEtude());
        candidat.setStatutActuel(dto.getStatutActuel());
        candidat.setCvUrl(dto.getCvUrl());

        if (dto.getProfilId() != null) {
            profilRepository.findById(dto.getProfilId()).ifPresent(candidat::setProfil);
        }

        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }
}
