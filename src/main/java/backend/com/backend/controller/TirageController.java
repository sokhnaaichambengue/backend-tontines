package backend.com.backend.controller;

import backend.com.backend.dto.VersementRequest;
import backend.com.backend.entities.Membre;
import backend.com.backend.entities.Versement;
import backend.com.backend.service.TirageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // Autorise les requêtes depuis ton application mobile (IP:8081)
public class TirageController {

    private final TirageService tirageService;

    public TirageController(TirageService tirageService) {
        this.tirageService = tirageService;
    }

    // Route appelée par : fetch(`${API}/membres`)
    @GetMapping("/membres")
    public ResponseEntity<List<Membre>> getMembres() {
        return ResponseEntity.ok(tirageService.getAllMembres());
    }

    // Route appelée par : fetch(`${API}/versements`, { method: "POST", ... })
    @PostMapping("/versements")
    public ResponseEntity<?> createVersement(@RequestBody VersementRequest request) {
        try {
            Versement versement = tirageService.effectuerVersement(request);
            // Renvoie un 200 OK (comme attendu par le frontend : if (response.ok))
            return ResponseEntity.ok(versement); 
        } catch (RuntimeException e) {
            // Renvoie une erreur 400 si le membre n'existe pas
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}