package be.bookstore.bookstore.repositories;

import be.bookstore.bookstore.model.AppUser;
import be.bookstore.bookstore.model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    
    List<Reservation> findByUser(AppUser user);

    List<Reservation> findByUserAndConfirmed(AppUser user, boolean confirmed);
}
