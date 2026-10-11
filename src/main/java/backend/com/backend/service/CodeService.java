package backend.com.backend.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeService {

    /*
    ============================================
    GENERER CODE SMS
    ============================================
    */

    public String generateCode() {

        Random random = new Random();

        int number =
                100000 + random.nextInt(900000);

        return String.valueOf(number);

    }

}