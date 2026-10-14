package be.bookstore.bookstore.repositories;

import be.bookstore.bookstore.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {

    List<Book> findByTitleContainingIgnoreCase(String title);
}
