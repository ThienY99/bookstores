package be.bookstore.bookstore.service;

import be.bookstore.bookstore.model.AppUser;
import be.bookstore.bookstore.model.Book;
import be.bookstore.bookstore.model.Reservation;
import be.bookstore.bookstore.repositories.AppUserRepository;
import be.bookstore.bookstore.repositories.BookRepository;
import be.bookstore.bookstore.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.bookstore.bookstore.dto.ReservationResponse;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            AppUserRepository appUserRepository,
            BookRepository bookRepository) {

        this.reservationRepository = reservationRepository;
        this.appUserRepository = appUserRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Reservation createReservation(int userId, String isbn, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Het aantal moet minstens 1 zijn."
            );
        }

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException(
                    "Een ISBN is verplicht."
            );
        }

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Gebruiker niet gevonden."
                ));

        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Boek niet gevonden."
                ));

        if (quantity > book.getStock()) {
            throw new IllegalArgumentException(
                    "Er zijn niet genoeg exemplaren op voorraad."
            );
        }

        Reservation reservation = new Reservation(user, book, quantity);

        return reservationRepository.save(reservation);
    }

    
}
