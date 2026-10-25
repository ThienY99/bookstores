package be.bookstore.bookstore.controller;

import be.bookstore.bookstore.model.AppUser;
import be.bookstore.bookstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        try {
            userService.registerUser(name, email, password);

            return new ResponseEntity<>(
                    "Gebruiker geregistreerd.",
                    HttpStatus.CREATED
            );
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(
                    exception.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } catch (DataIntegrityViolationException exception) {
            return new ResponseEntity<>(
                    "Gebruiker kon niet worden opgeslagen. "
                            + "Mogelijk is het e-mailadres al geregistreerd.",
                    HttpStatus.CONFLICT
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpServletRequest request) {

        try {
            AppUser user = userService.checkLogin(email, password);

            HttpSession session = request.getSession();
            request.changeSessionId();

            session.setAttribute("userId", user.getId());

            return new ResponseEntity<>(
                    "Je bent ingelogd.",
                    HttpStatus.OK
            );

        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(
                    "E-mailadres of wachtwoord is onjuist.",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}
