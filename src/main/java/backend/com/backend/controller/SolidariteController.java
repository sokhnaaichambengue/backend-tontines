package backend.com.backend.controller;

import backend.com.backend.entities.Solidarite;
import backend.com.backend.service.SolidariteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solidarite")
@CrossOrigin("*")
public class SolidariteController {

    private final SolidariteService service;

    public SolidariteController(SolidariteService service) {
        this.service = service;
    }

    // 📩 ENVOYER DEMANDE
    @PostMapping
    public Solidarite create(@RequestBody Solidarite s) {
        return service.save(s);
    }

    // 📋 HISTORIQUE
    @GetMapping
    public List<Solidarite> getAll() {
        return service.getAll();
    }
}