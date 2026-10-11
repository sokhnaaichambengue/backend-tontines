package backend.com.backend.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PushService {

    public void sendPush(String token, String title, String body) {

        String url = "https://exp.host/--/api/v2/push/send";

        Map<String, Object> message = new HashMap<>();
        message.put("to", token);
        message.put("title", title);
        message.put("body", body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(message, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, request, String.class);
    }
}