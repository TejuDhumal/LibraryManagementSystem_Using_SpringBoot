package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.bookEvent.*;
import com.mycompany.myapp.domain.dto.BookDTO;
import com.mycompany.myapp.domain.dto.UserDTO;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.service.impl.BookServiceImpl;
import java.io.InvalidClassException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BookResourceTest {

    @Mock
    private BookServiceImpl bookServiceIml;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookResource bookResource;

    @Test
    void createBook() throws URISyntaxException {
        // Arrange
        Book book = new Book();
        book.setId("1");
        book.setBookName("Test Book");

        when(bookServiceIml.save(book)).thenReturn(book);

        // Act
        ResponseEntity response = bookResource.createBook(book);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof Event);
        Event responseBody = (Event) response.getBody();
        Assertions.assertEquals(201, responseBody.getStatusCode());
        Assertions.assertEquals("Book created successfully", responseBody.getMessage());
        Assertions.assertEquals(book, responseBody.getResult());
        Assertions.assertEquals("/api/books/1", response.getHeaders().getLocation().toString());
        verify(bookServiceIml, times(1)).save(book);
    }

    @Test
    void updateBook() throws InvalidClassException, URISyntaxException {
        // Arrange
        String bookId = "1";
        Book book = new Book();
        book.setId(bookId);
        book.setBookName("Book 1");

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookServiceIml.update(book)).thenReturn(book);

        // Act
        ResponseEntity response = bookResource.updateBook(bookId, book);

        EventBookDTO responseBody = (EventBookDTO) response.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventBookDTO);

        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("Book retreived successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getResult());
        Assertions.assertEquals(bookId, responseBody.getResult().getId());
        Assertions.assertEquals("Book 1", responseBody.getResult().getBookName());
        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookServiceIml, times(1)).update(book);
    }

    @Test
    void partialUpdateBook() throws URISyntaxException {
        // Arrange
        String bookId = "1";
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(bookId);
        bookDTO.setBookName("Updated Book");

        Book book = new Book();
        book.setId(bookId);
        book.setBookName("Original Book");

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookServiceIml.partialUpdate(bookDTO, bookId)).thenReturn(Optional.of(book));

        // Act
        ResponseEntity response = bookResource.partialUpdateBook(bookId, bookDTO);

        EventBookDTO responseBody = (EventBookDTO) response.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventBookDTO);
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("Book updated successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getResult());
        Assertions.assertEquals(bookId, responseBody.getResult().getId());
        Assertions.assertEquals("Original Book", responseBody.getResult().getBookName());
        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookServiceIml, times(1)).partialUpdate(bookDTO, bookId);
    }

    @Test
    void getAllBooks() {
        Book book1 = new Book();
        book1.setBookName("demo1");
        book1.setAuthor("author");
        book1.setCategory("category");
        book1.setQuantity(10);
        book1.setPrice(10.0);
        book1.setAvailable(true);

        Book book2 = new Book();
        book2.setBookName("demo2");
        book2.setAuthor("author");
        book2.setCategory("category");
        book2.setQuantity(10);
        book2.setPrice(10.0);
        book2.setAvailable(false);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";

        when(bookServiceIml.findAll(anyInt(), anyInt(), anyString())).thenReturn(books);

        // Act
        ResponseEntity response = bookResource.getAllBooks(pageNo, pageSize, sortBy);

        EventBookDTOList responseBody = (EventBookDTOList) response.getBody();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(200, responseBody.getStatusCode()),
            () -> assertEquals("Books retrieved successfully", responseBody.getMessage()),
            () -> assertEquals(2, responseBody.getResult().size())
        );
        verify(bookServiceIml, times(1)).findAll(pageNo, pageSize, sortBy);
    }

    @Test
    void getBook() {
        BookDTO expectedBook = new BookDTO();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        when(bookServiceIml.findOne(anyString())).thenReturn(expectedBook);

        ResponseEntity response = bookResource.getBook("1");

        EventBookDTO actualBook = (EventBookDTO) response.getBody();
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals("Book getting successfully", actualBook.getMessage()),
            () -> assertEquals("demo", actualBook.getResult().getBookName()),
            () -> assertEquals("author", actualBook.getResult().getAuthor()),
            () -> assertEquals("category", actualBook.getResult().getCategory()),
            () -> assertEquals(10, actualBook.getResult().getQuantity()),
            () -> assertEquals(10.0, actualBook.getResult().getPrice()),
            () -> assertEquals(true, actualBook.getResult().isAvailable())
        );
    }

    @Test
    void deleteBook() {
        Book expectedBook = new Book();
        expectedBook.setId("id1");
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        when(bookRepository.findById(anyString())).thenReturn(Optional.of(expectedBook));

        // Act
        ResponseEntity response = bookResource.deleteBook("id1");

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        EventOptionalBook responseBody = (EventOptionalBook) response.getBody();
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("Book deleted successfully", responseBody.getMessage());

        //       Assertions.assertNotNull(deletedBook);
        Assertions.assertEquals("demo", responseBody.getResult().get().getBookName());
        Assertions.assertEquals("author", responseBody.getResult().get().getAuthor());

        verify(bookServiceIml, times(1)).delete("id1");
    }

    @Test
    void getBookByCriteria() {
        Book expectedBook = new Book();
        expectedBook.setId("id1");
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        Book expectedBook2 = new Book();
        expectedBook2.setId("id1");
        expectedBook2.setBookName("demo");
        expectedBook2.setAuthor("author");
        expectedBook2.setCategory("category");
        expectedBook2.setQuantity(10);
        expectedBook2.setPrice(10.0);
        expectedBook2.setAvailable(true);

        List<Book> bookList = new ArrayList<>();
        bookList.add(expectedBook);
        bookList.add(expectedBook2);
        when(bookServiceIml.getByCriteria(anyString(), anyString())).thenReturn(bookList);

        ResponseEntity response = bookResource.getBookByCriteria("author", "category");

        EventBookDTOList actualBook = (EventBookDTOList) response.getBody();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals("Books retrieved successfully", actualBook.getMessage()),
            () -> assertEquals("demo", actualBook.getResult().get(0).getBookName()),
            () -> assertEquals("author", actualBook.getResult().get(0).getAuthor()),
            () -> assertEquals("category", actualBook.getResult().get(0).getCategory()),
            () -> assertEquals(10, actualBook.getResult().get(0).getQuantity()),
            () -> assertEquals(10.0, actualBook.getResult().get(0).getPrice()),
            () -> assertEquals(true, actualBook.getResult().get(0).isAvailable())
        );
        verify(bookServiceIml, times(1)).getByCriteria("author", "category");
    }

    @Test
    public void testGetBookByCriteriaWithEmptyList() {
        // Call the method being tested with a valid criteria and value, but an empty list
        ResponseEntity response = bookResource.getBookByCriteria("title", "value");

        EventBookDTOList eventBookDTOList = (EventBookDTOList) response.getBody();

        // Verify the response status code and message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("List is empty", eventBookDTOList.getMessage());

        // Verify that the list of books in the response is empty
        List<BookDTO> books = (List<BookDTO>) eventBookDTOList.getResult();
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    void getBookByAvalability() {
        // Arrange
        Book book1 = new Book();
        book1.setId("1");
        book1.setBookName("Book 1");
        book1.setAvailable(true);

        Book book2 = new Book();
        book2.setId("2");
        book2.setBookName("Book 2");
        book2.setAvailable(true);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        when(bookServiceIml.getAvailableBooks()).thenReturn(books);

        // Act
        ResponseEntity response = bookResource.getBookByAvalability();

        EventBookDTOList responseBody = (EventBookDTOList) response.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventBookDTOList);

        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("Books retrieved successfully", responseBody.getMessage());
        Assertions.assertEquals(2, responseBody.getResult().size());
        Assertions.assertEquals("Book 1", responseBody.getResult().get(0).getBookName());
        Assertions.assertEquals(true, responseBody.getResult().get(0).isAvailable());
        Assertions.assertEquals("Book 2", responseBody.getResult().get(1).getBookName());
        Assertions.assertEquals(true, responseBody.getResult().get(0).isAvailable());
        verify(bookServiceIml, times(1)).getAvailableBooks();
    }

    @Test
    public void testGetAvailableBooks_NoBooksAvailable() {
        // Arrange
        when(bookServiceIml.getAvailableBooks()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity response = bookResource.getBookByAvalability();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventBookDTOList);
        EventBookDTOList responseBody = (EventBookDTOList) response.getBody();
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("List is empty", responseBody.getMessage());
        Assertions.assertEquals(0, responseBody.getResult().size());
        verify(bookServiceIml, times(1)).getAvailableBooks();
    }

    @Test
    void getUsersByBookId() {
        UserDTO expectedUser = new UserDTO();
        expectedUser.setId("id1");
        expectedUser.setUsername("Demo");
        expectedUser.setRoles("ROLE_DEMO");

        List<UserDTO> userList = new ArrayList<>();
        userList.add(expectedUser);

        Book expectedBook = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);
        expectedBook.setUsers(userList);

        when(bookServiceIml.getUsersByBookId(anyString())).thenReturn(expectedBook);

        ResponseEntity response = bookResource.getUsersByBookId(anyString());

        EventBookUsersDTO responseBody = (EventBookUsersDTO) response.getBody();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals("users retreived successfully", responseBody.getMessage()),
            () -> assertEquals("demo", responseBody.getResult().getBookName()),
            () -> assertEquals("author", responseBody.getResult().getAuthor()),
            () -> assertEquals("category", responseBody.getResult().getCategory()),
            () -> assertEquals(userList, responseBody.getResult().getUsers())
        );
    }

    @Test
    void getAllBooksByKeyword() {
        BookDTO book1 = new BookDTO();
        book1.setId("1");
        book1.setBookName("Book 1");
        book1.setAuthor("author");
        book1.setCategory("category");

        BookDTO book2 = new BookDTO();
        book2.setId("2");
        book2.setBookName("Book 2");
        book2.setAuthor("author");
        book2.setCategory("category");

        List<BookDTO> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        when(bookServiceIml.searchBooksByKey(anyString())).thenReturn(books);

        // Act
        ResponseEntity response = bookResource.getAllBooksByKeyword("key");

        EventBookDTOList responseBody = (EventBookDTOList) response.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventBookDTOList);
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("Books retrieved successfully", responseBody.getMessage());
        Assertions.assertEquals(books, responseBody.getResult());
    }

    @Test
    public void testGetBooksByKeyNoBooksAvailable() {
        // Arrange
        when(bookServiceIml.searchBooksByKey(anyString())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity response = bookResource.getAllBooksByKeyword("key");

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventBookDTOList);
        EventBookDTOList responseBody = (EventBookDTOList) response.getBody();
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("No books available", responseBody.getMessage());
        Assertions.assertEquals(0, responseBody.getResult().size());
        verify(bookServiceIml, times(1)).searchBooksByKey("key");
    }

    @Test
    public void createReport() throws ParseException {
        // Arrange
        String startDate = "2023-05-04";
        String endDate = "2023-08-06";
        Book expectedBook = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        Book expectedBook2 = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        List<Book> books = new ArrayList<>();
        books.add(expectedBook);
        books.add(expectedBook2);

        HashMap<String, Object> map = new HashMap<>();
        map.put("dataForWeek", books);
        map.put("BookReturned", books);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("dataForTwoDays", map);

        when(bookServiceIml.createReport(anyString(), anyString())).thenReturn(map1);

        // Act
        ResponseEntity response = bookResource.createReport(startDate, endDate);

        Event responseBody = (Event) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(map);
        assertEquals("Report generated successfully", responseBody.getMessage());
        assertEquals(map1, ((Event) response.getBody()).getResult());
    }
}
