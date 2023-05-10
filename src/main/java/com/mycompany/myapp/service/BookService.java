package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.dto.BookDTO;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Book}.
 */
public interface BookService {
    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    Book save(Book book);

    /**
     * Updates a book.
     *
     * @param book the entity to update.
     * @return the persisted entity.
     */
    Book update(Book book);

    /**
     * Partially updates a book.
     *
     * @return the persisted entity.
     */
    Optional<Book> partialUpdate(BookDTO bookDTO, String id);

    /**
     * Get all the books.
     *
     * @return the list of entities.
     */
    List<Book> findAll(Integer pageNo, Integer pageSize, String sortBy);

    List<BookDTO> searchBooksByKey(String key);

    /**
     * Get the "id" book.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    BookDTO findOne(String id);

    /**
     * Delete the "id" book.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    List<Book> getByCriteria(String author, String category);

    //    List<Book> getByCategory(String category);

    List<Book> getAvailableBooks();

    Book getUsersByBookId(String bookId);

    HashMap<String, Object> createReport(String startDate, String endDate) throws ParseException;
}
