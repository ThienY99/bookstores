package be.bookstore.bookstore.controller;

import be.bookstore.bookstore.model.Book;
import be.bookstore.bookstore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/books")
public class BookController {
    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addBook(
            @RequestParam("isbn") String isbn,
            @RequestParam("title") String title,
            @RequestParam("authors") String authors,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") int stock,
            @RequestParam("genre") String genre) {

        if (bookRepository.existsById(isbn)) {
            return new ResponseEntity<>(
                    "Een boek met dit ISBN bestaat al.",
                    HttpStatus.CONFLICT
            );
        }
        if (price.compareTo(BigDecimal.ZERO) < 0 || stock < 0) {
            return new ResponseEntity<>(
                    "Prijs en stock mogen niet negatief zijn.",
                    HttpStatus.BAD_REQUEST
            );
        }
        Book book = new Book(
                isbn,
                title,
                authors,
                description,
                price,
                stock,
                genre
        );
        bookRepository.save(book);
        return new ResponseEntity<>(
                "Boek toegevoegd",
                HttpStatus.CREATED
        );
    }

    @GetMapping("/id")
    public ResponseEntity<Book> getBookByIsbn(
            @RequestParam("isbn") String isbn) {
        Optional<Book> result = bookRepository.findById(isbn);

        if (result.isPresent()) {
            Book book = result.get();
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam("title") String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }


}
