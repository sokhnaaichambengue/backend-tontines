package backend.com.backend.service;

import backend.com.backend.dto.VersementRequest;
import backend.com.backend.entities.Membre;
import backend.com.backend.entities.Versement;
import backend.com.backend.repository.MembreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TirageService {

    private final MembreRepository membreRepository;

    public TirageService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    // Récupérer tous les membres (pour le frontend)
    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

    // Logique de versement
    @Transactional
    public Versement effectuerVersement(VersementRequest request) {
        // 1. Vérifier si le membre existe
        Membre membre = membreRepository.findById(request.getMembre_id())
                .orElseThrow(() -> new RuntimeException("Membre introuvable"));

        // 2. Créer le versement
        Versement versement = new Versement();
        versement.setMembre(membre);
        versement.setMontant(request.getMontant());
        versement.setMotif(request.getMotif());

        // 3. Mettre à jour le solde du membre
        membre.setSoldeTotal(membre.getSoldeTotal() + request.getMontant());
        membreRepository.save(membre);

        // 4. Sauvegarder et retourner le versement
        return versement; // Spring Boot va le sauvegarder automatiquement grâce à @Transactional si on le persiste, 
                          // mais faisons le explicitement pour être propre :
    }
}