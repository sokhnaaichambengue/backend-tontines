package backend.com.backend.controller;

import backend.com.backend.dto.CotisationRequest;
import backend.com.backend.entities.Cotisation;
import backend.com.backend.service.CotisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/cotisations", "/api/cotisations"})
@CrossOrigin("*")
public class CotisationController {

    private final CotisationService service;

    public CotisationController(CotisationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> ajouter(@RequestBody CotisationRequest request) {
        try {
            Cotisation cotisation = service.enregistrerVersement(request);
            return ResponseEntity.ok(cotisation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Cotisation> all() {
        return service.getAll();
    }
}