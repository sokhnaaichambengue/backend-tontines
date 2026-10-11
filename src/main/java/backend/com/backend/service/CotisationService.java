package backend.com.backend.service;

import backend.com.backend.dto.CotisationRequest;
import backend.com.backend.entities.Cotisation;
import backend.com.backend.entities.Membre;
import backend.com.backend.repository.CotisationRepository;
import backend.com.backend.repository.MembreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CotisationService {

    private final CotisationRepository repo;
    private final MembreRepository membreRepository;

    public CotisationService(CotisationRepository repo, MembreRepository membreRepository) {
        this.repo = repo;
        this.membreRepository = membreRepository;
    }

    @Transactional
    public Cotisation enregistrerVersement(CotisationRequest request) {
        if (request == null || request.getMembreId() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un membre");
        }

        if (request.getMontant() == null || request.getMontant() <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        Membre membre = membreRepository.findById(request.getMembreId())
                .orElseThrow(() -> new IllegalArgumentException("Membre introuvable"));

        double montantVerse = request.getMontant();
        double montantCotise = membre.getMontantCotisation() == null ? 0.0 : membre.getMontantCotisation();
        double totalVerse = membre.getTotalVerse() == null ? 0.0 : membre.getTotalVerse();
        double reste = montantCotise - totalVerse;

        if (montantVerse > reste) {
            throw new IllegalArgumentException("Le montant dépasse le reste à payer");
        }

        Cotisation cotisation = new Cotisation();
        cotisation.setMembre(membre);
        cotisation.setMontant(montantVerse);
        cotisation.setDateVersement(request.getDateVersement() != null ? request.getDateVersement() : LocalDateTime.now());
        cotisation.setRetard(false);
        cotisation.setAmende(0.0);

        membre.setTotalVerse(totalVerse + montantVerse);
        membreRepository.save(membre);

        return repo.save(cotisation);
    }

    public Cotisation save(Cotisation c) {
        return repo.save(c);
    }

    public List<Cotisation> getAll() {
        return repo.findAll();
    }
}