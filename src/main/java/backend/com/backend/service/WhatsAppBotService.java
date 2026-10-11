package backend.com.backend.service;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppBotService {

    public String handleMessage(String message) {

        message = message.toLowerCase();

        // 💰 COTISATION
        if (message.contains("cotisation")) {
            return "La cotisation est le montant payé par chaque membre pour participer à la tontine.";
        }

        // 💸 RETARD
        if (message.contains("retard")) {
            return "En cas de retard, une amende peut être appliquée automatiquement.";
        }

        // 🤝 SOLIDARITÉ
        if (message.contains("solidarité")) {
            return "La solidarité permet d'aider un membre en difficulté financière.";
        }

        // ❗ RÉCLAMATION
        if (message.contains("réclamation") || message.contains("probleme")) {
            return "Veuillez envoyer votre réclamation via l'application ou contacter l'administrateur.";
        }

        // 💳 PAIEMENT
        if (message.contains("payer") || message.contains("paiement")) {
            return "Vous pouvez payer votre cotisation via Orange Money ou Wave.";
        }

        // default
        return "Bonjour 👋 je suis le bot de la tontine. Pose une question sur cotisation, paiement ou solidarité.";
    }
}
