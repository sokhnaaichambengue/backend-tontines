package backend.com.backend.service;

import backend.com.backend.entities.Solidarite;
import backend.com.backend.repository.SolidariteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolidariteService {

    private final SolidariteRepository repo;

    public SolidariteService(SolidariteRepository repo) {
        this.repo = repo;
    }

    // 💾 enregistrer demande solidarité
    public Solidarite save(Solidarite s) {
        return repo.save(s);
    }

    // 📋 liste des demandes
    public List<Solidarite> getAll() {
        return repo.findAll();
    }
}