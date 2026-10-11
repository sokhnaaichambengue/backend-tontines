package backend.com.backend.service;

import backend.com.backend.entities.Reclamation;
import backend.com.backend.repository.ReclamationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamationService {

    private final ReclamationRepository repo;

    public ReclamationService(ReclamationRepository repo) {
        this.repo = repo;
    }

    // 💾 enregistrer réclamation
    public Reclamation save(Reclamation r) {
        return repo.save(r);
    }

    // 📋 liste réclamations
    public List<Reclamation> getAll() {
        return repo.findAll();
    }
}