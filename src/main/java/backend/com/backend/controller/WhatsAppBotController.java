package backend.com.backend.controller;

import backend.com.backend.dto.WhatsAppMessage;
import backend.com.backend.service.WhatsAppBotService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
@CrossOrigin("*")
public class WhatsAppBotController {

    private final WhatsAppBotService botService;

    public WhatsAppBotController(WhatsAppBotService botService) {
        this.botService = botService;
    }

    // 📩 RECEPTION MESSAGE WHATSAPP
    @PostMapping("/webhook")
    public Map<String, String> receive(@RequestBody WhatsAppMessage msg) {

        String reply = botService.handleMessage(msg.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("reply", reply);

        return response;
    }
}