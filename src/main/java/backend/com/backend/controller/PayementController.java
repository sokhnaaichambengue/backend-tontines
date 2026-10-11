package backend.com.backend.controller;

import backend.com.backend.entities.Payement;
import backend.com.backend.service.PayementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PayementController {

    @Autowired
    private PayementService payementService;

    // 💳 créer paiement
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Payement payement) {

        Map<String, Object> response = payementService.createPayment(payement);

        return ResponseEntity.ok(response);
    }

    // 🔔 webhook (validation paiement réel)
    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody Map<String, Object> data) {

        String transactionId = String.valueOf(data.get("transaction_id"));
        String status = String.valueOf(data.get("status"));

        if ("SUCCESS".equals(status)) {
            payementService.confirmPayment(transactionId);
        }

        return ResponseEntity.ok("OK");
    }
}