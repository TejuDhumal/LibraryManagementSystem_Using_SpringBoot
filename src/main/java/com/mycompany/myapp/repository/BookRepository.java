package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.dto.BookDTO;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    Book findAllBybookName(String bookName);

    List<Book> findByauthor(String author);

    List<Book> findBycategory(String category);

    List<Book> findByAvailableTrue();

    List<BookDTO> findBybookNameContainingIgnoreCase(String key);

    List<Book> findAllByIssueddateBetween(Date dayStart, Date dayEnd);

    List<Book> findAllByReturneddateBetween(Date dayStart, Date dayEnd);
}
