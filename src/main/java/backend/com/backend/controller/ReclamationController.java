package backend.com.backend.controller;

import backend.com.backend.entities.Reclamation;
import backend.com.backend.service.ReclamationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@CrossOrigin("*")
public class ReclamationController {

    private final ReclamationService service;

    public ReclamationController(ReclamationService service) {
        this.service = service;
    }

    // 📩 ENVOYER RÉCLAMATION
    @PostMapping
    public Reclamation create(@RequestBody Reclamation r) {
        return service.save(r);
    }

    // 📋 LISTE DES RÉCLAMATIONS
    @GetMapping
    public List<Reclamation> getAll() {
        return service.getAll();
    }
}