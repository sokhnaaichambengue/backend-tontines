package backend.com.backend.controller;

import backend.com.backend.entities.Membre;
import backend.com.backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /*
    ===================================================
    LOGIN
    ===================================================
    ADMIN = libre
    MEMBRE = téléphone
    ===================================================
    */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String phone = body.get("phone");

        Map<String, Object> response = new HashMap<>();

        /*
        =========================
        MEMBRE
        =========================
        */
        Optional<Membre> membre = authService.loginAsMembre(phone);

        if (membre.isPresent()) {

            Membre m = membre.get();

            response.put("success", true);
            response.put("id", m.getId());
            response.put("nom", m.getNom());
            response.put("phone", m.getTelephone());
            response.put("role", "MEMBRE");
            response.put("nextPage", "ENTER_CODE");
            response.put("memberId", m.getId());

            return ResponseEntity.ok(response);
        }

        /*
        =========================
        ADMIN (LIBRE)
        =========================
        */
        response.put("success", true);
        response.put("id", 1);
        response.put("nom", "ADMIN");
        response.put("phone", phone);
        response.put("role", "ADMIN");
        response.put("nextPage", "DASHBOARD_ADMIN");

        return ResponseEntity.ok(response);
    }

    /*
    ===================================================
    VERIFY CODE
    ===================================================
    */

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {

        String code = body.get("code");

        if (code == null || code.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(error("Code requis"));
        }

        Optional<Membre> membre = authService.verifyCode(code);

        if (membre.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(error("Code invalide"));
        }

        Membre m = membre.get();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", m.getId());
        response.put("nom", m.getNom());
        response.put("phone", m.getTelephone());
        response.put("role", "MEMBRE");
        response.put("nextPage", "DASHBOARD_MEMBRE");

        return ResponseEntity.ok(response);
    }

    /*
    =========================
    ERROR HELPER
    =========================
    */
    private Map<String, Object> error(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", msg);
        return map;
    }
}