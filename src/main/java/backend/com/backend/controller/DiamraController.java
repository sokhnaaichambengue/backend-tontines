package backend.com.backend.controller;

import backend.com.backend.dto.DiamraParticipationRequest;
import backend.com.backend.dto.DiamraResult;
import backend.com.backend.entities.Diamra;
import backend.com.backend.entities.DiamraParticipation;
import backend.com.backend.service.DiamraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/diamra")
public class DiamraController {

    private final DiamraService diamraService;

    public DiamraController(DiamraService diamraService) {
        this.diamraService = diamraService;
    }

    @GetMapping("/prochain")
    public ResponseEntity<Diamra> prochainDiamra() {
        return ResponseEntity.ok(diamraService.getProchainDiamra());
    }

    @GetMapping("/participants")
    public ResponseEntity<List<DiamraParticipation>> participants() {
        return ResponseEntity.ok(diamraService.getParticipants());
    }

    @PostMapping("/participer")
    public ResponseEntity<DiamraParticipation> participer(@RequestBody DiamraParticipationRequest request) {
        return ResponseEntity.ok(diamraService.participer(request));
    }

    @PostMapping("/tirage")
    public ResponseEntity<DiamraResult> lancerTirage() {
        return ResponseEntity.ok(diamraService.lancerTirage());
    }
}
