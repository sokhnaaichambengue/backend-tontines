package backend.com.backend.service;

import backend.com.backend.entities.Payement;
import backend.com.backend.repository.PayementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayementService {

    @Autowired
    private PayementRepository paymentRepository;

    // 👉 créer un paiement
    public Map<String, Object> createPayment(Payement payement) {

        payement.setStatus("PENDING");
        Payement saved = paymentRepository.save(payement);

        // 👉 simulation appel API CinetPay / PayDunya
        String paymentUrl = "https://secure-payment.com/pay/" + saved.getId();

        Map<String, Object> response = new HashMap<>();
        response.put("payment_url", paymentUrl);
        response.put("transaction_id", saved.getId());

        return response;
    }

    // 👉 valider paiement (appelé par webhook)
    public void confirmPayment(String transactionId) {

        Payement payment = paymentRepository.findAll()
                .stream()
                .filter(p -> p.getId().toString().equals(transactionId))
                .findFirst()
                .orElse(null);

        if (payment != null) {
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);
        }
    }
}