package be.bookstore.bookstore.repositories;

import be.bookstore.bookstore.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, String> {
}
