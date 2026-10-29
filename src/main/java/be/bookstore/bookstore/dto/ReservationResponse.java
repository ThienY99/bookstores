package be.bookstore.bookstore.dto;

public record ReservationResponse (
        int id,
        String isbn,
        String title,
        int quantity,
        boolean confirmed
){
}
