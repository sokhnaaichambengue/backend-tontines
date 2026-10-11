package backend.com.backend.controller;

import backend.com.backend.entities.Membre;

import backend.com.backend.repository.MembreRepository;

import backend.com.backend.service.CodeService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/membre")
@CrossOrigin("*")
public class MembreController {

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private CodeService codeService;

    /*
    ==================================================
    CREER MEMBRE + GENERER CODE (Admin Only)
    ==================================================
    
    L'administrateur crée un nouveau membre en fournissant
    les informations du membre. Un code unique est 
    généré automatiquement et assigné au membre.
    
    Le numéro de téléphone du membre doit être unique
    et unique dans la table des membres pour permettre
    l'identification du rôle lors de la connexion.
    */

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody Membre membre
    ) {

        // Validation des champs requis
        if(membre.getNom() == null || membre.getNom().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(createErrorResponse("Le nom du membre est requis"));
        }

        if(membre.getTelephone() == null || membre.getTelephone().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(createErrorResponse("Le numéro de téléphone est requis"));
        }

        // Vérifier si le numéro de téléphone existe déjà
        if(membreRepository.findFirstByTelephoneAndActifTrue(membre.getTelephone()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(createErrorResponse("Ce numéro de téléphone est déjà utilisé"));
        }

        /*
        ==========================================
        GENERATION AUTOMATIQUE DU CODE
        ==========================================
        */

        String generatedCode = codeService.generateCode();
        membre.setCode(generatedCode);
        membre.setActif(true);

        /*
        ==========================================
        SAVE DATABASE
        ==========================================
        */

        Membre membreSaved = membreRepository.save(membre);

        // Retourner une réponse avec le code généré
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", membreSaved.getId());
        response.put("nom", membreSaved.getNom());
        response.put("telephone", membreSaved.getTelephone());
        response.put("code", membreSaved.getCode());
        response.put("type", membreSaved.getType());
        response.put("actif", membreSaved.isActif());
        response.put("message", "Membre créé avec succès. Code: " + membreSaved.getCode());

        return ResponseEntity.ok(response);
    }

    /*
    ==================================================
    HELPER METHOD - Create Error Response
    ==================================================
    */

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        return error;
    }

}