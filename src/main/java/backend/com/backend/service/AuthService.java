package backend.com.backend.service;

import backend.com.backend.entities.Membre;
import backend.com.backend.repository.MembreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private MembreRepository membreRepository;

    // MEMBRE login par téléphone
    public Optional<Membre> loginAsMembre(String phone) {
        return membreRepository.findFirstByTelephoneAndActifTrue(phone);
    }

    // verification code membre
    public Optional<Membre> verifyCode(String code) {
        return membreRepository.findFirstByCodeAndActifTrue(code);
    }
}