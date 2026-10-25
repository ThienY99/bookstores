package be.bookstore.bookstore.service;


import be.bookstore.bookstore.model.AppUser;
import be.bookstore.bookstore.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerUser(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Naam mag niet leeg zijn.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mailadres mag niet leeg zijn.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Wachtwoord mag niet leeg zijn.");
        }

        String cleanName = name.trim();
        String cleanEmail = email.trim().toLowerCase(Locale.ROOT);

        if (appUserRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("Dit e-mailadres is al geregistreerd.");
        }

        String passwordHash = passwordEncoder.encode(password);

        AppUser user = new AppUser(cleanName, cleanEmail, passwordHash);
            return appUserRepository.save(user);
    }
}
