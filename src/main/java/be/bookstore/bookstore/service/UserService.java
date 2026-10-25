package be.bookstore.bookstore.service;


import be.bookstore.bookstore.model.AppUser;
import be.bookstore.bookstore.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String name, String email, String password) {
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
        appUserRepository.save(user);
    }

    public AppUser checkLogin(String email, String password) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "E-mailadres en wachtwoord zijn verplicht."
            );
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "E-mailadres en wachtwoord zijn verplicht."
            );
        }

        String cleanEmail = email.trim().toLowerCase(Locale.ROOT);

        Optional<AppUser> result =
                appUserRepository.findByEmail(cleanEmail);

        if (result.isEmpty()) {
            throw new IllegalArgumentException(
                    "E-mailadres of wachtwoord is onjuist."
            );
        }

        AppUser user = result.get();

        boolean correctPassword = passwordEncoder.matches(
                password,
                user.getPasswordHash()
        );
        if (!correctPassword) {
            throw new IllegalArgumentException(
                    "E-mailadres of wachtwoord is onjuist."
            );
        }
        return user;
    }
}
