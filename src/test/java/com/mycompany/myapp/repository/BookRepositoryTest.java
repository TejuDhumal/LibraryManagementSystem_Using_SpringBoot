package com.mycompany.myapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.dto.BookDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        String bookName = "The Great Gatsby";
        Book book = new Book();
        book.setBookName(bookName);
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        bookRepository.save(book);
    }

    @Test
    void findAllBybookName() {
        // Act
        Book result = bookRepository.findAllBybookName("The Great Gatsby");

        // Assert
        //        assertNotNull(result);
        assertEquals("The Great Gatsby", result.getBookName());
    }

    @Test
    void findByauthor() {
        List<Book> books = new ArrayList<>();

        String bookName = "demo3";
        Book book = new Book();
        book.setBookName(bookName);
        book.setCategory("category2");
        book.setAuthor("author2");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        books.add(book);

        Book book1 = new Book();
        book.setBookName("The Great Gatsby");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        books.add(book1);

        System.out.println(books);
        // Act
        List<Book> result = bookRepository.findByauthor("author");

        // Assert
        //        assertNotNull(result);
        assertAll(
            () -> assertEquals(books.get(0).getBookName(), result.get(0).getBookName()),
            () -> assertEquals(books.get(0).getPrice(), result.get(0).getPrice()),
            () -> assertEquals(books.get(0).getQuantity(), result.get(0).getQuantity()),
            () -> assertEquals(books.get(0).getCategory(), result.get(0).getCategory()),
            () -> assertEquals(books.get(0).getAuthor(), result.get(0).getAuthor())
        );
    }

    @Test
    void findBycategory() {
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setBookName("demo1");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        books.add(book);

        Book book1 = new Book();
        book.setBookName("The Great Gatsby");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        books.add(book1);

        // Act
        List<Book> result = bookRepository.findBycategory("category");

        // Assert
        //        assertNotNull(result);
        assertAll(
            () -> assertEquals(books.get(0).getBookName(), result.get(0).getBookName()),
            () -> assertEquals(books.get(0).getPrice(), result.get(0).getPrice()),
            () -> assertEquals(books.get(0).getQuantity(), result.get(0).getQuantity()),
            () -> assertEquals(books.get(0).getCategory(), result.get(0).getCategory()),
            () -> assertEquals(books.get(0).getAuthor(), result.get(0).getAuthor())
        );
    }

    @Test
    void findByAvailableTrue() {
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setBookName("demo1");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        books.add(book);

        Book book1 = new Book();
        book.setBookName("The Great Gatsby");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        books.add(book1);

        // Act
        List<Book> result = bookRepository.findByAvailableTrue();

        // Assert
        //        assertNotNull(result);
        assertAll(
            () -> assertEquals(books.get(0).getBookName(), result.get(0).getBookName()),
            () -> assertEquals(books.get(0).getPrice(), result.get(0).getPrice()),
            () -> assertEquals(books.get(0).getQuantity(), result.get(0).getQuantity()),
            () -> assertEquals(books.get(0).getCategory(), result.get(0).getCategory()),
            () -> assertEquals(books.get(0).getAuthor(), result.get(0).getAuthor()),
            () -> assertEquals(books.get(0).isAvailable(), result.get(0).isAvailable())
        );
    }

    @Test
    void findBybookNameContainingIgnoreCase() {
        List<BookDTO> bookDTOS = new ArrayList<>();

        BookDTO book = new BookDTO();
        book.setBookName("bookName");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        bookDTOS.add(book);

        BookDTO book2 = new BookDTO();
        book.setBookName("The Great Gatsby");
        book.setCategory("category");
        book.setAuthor("author");
        book.setQuantity(10);
        book.setPrice(10.0);
        book.setAvailable(true);
        bookDTOS.add(book2);

        // Act
        List<BookDTO> result = bookRepository.findBybookNameContainingIgnoreCase("Great");

        // Assert
        //        assertNotNull(result);

        assertAll(
            () -> assertEquals(bookDTOS.get(0).getBookName(), result.get(0).getBookName()),
            () -> assertEquals(bookDTOS.get(0).getPrice(), result.get(0).getPrice()),
            () -> assertEquals(bookDTOS.get(0).getQuantity(), result.get(0).getQuantity()),
            () -> assertEquals(bookDTOS.get(0).getCategory(), result.get(0).getCategory()),
            () -> assertEquals(bookDTOS.get(0).getAuthor(), result.get(0).getAuthor()),
            () -> assertEquals(bookDTOS.get(0).isAvailable(), result.get(0).isAvailable())
        );
    }

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
    }
}
