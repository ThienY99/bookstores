package be.bookstore.bookstore.controller;

import be.bookstore.bookstore.model.Reservation;
import be.bookstore.bookstore.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(
            @RequestParam("isbn") String isbn,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Je bent niet ingelogd.");
        }

        try {
            Reservation reservation = reservationService.createReservation(
                    userId, isbn, quantity
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Reservering " + reservation.getId()
                            + " is aangemaakt en nog niet bevestigd.");

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }
}
