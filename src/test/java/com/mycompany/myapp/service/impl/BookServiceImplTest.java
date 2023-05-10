package com.mycompany.myapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.bookEvent.Event;
import com.mycompany.myapp.domain.bookEvent.EventBookDTOList;
import com.mycompany.myapp.domain.dto.BookDTO;
import com.mycompany.myapp.domain.dto.UserDTO;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookServiceIml;

    @Mock
    private BookRepository bookRepository;

    @Test
    void save() {
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

        when(bookRepository.save(any())).thenReturn(expectedBook);

        Book actualBook = bookServiceIml.save(expectedBook);

        assertAll(
            () -> assertEquals("demo", actualBook.getBookName()),
            () -> assertEquals("author", actualBook.getAuthor()),
            () -> assertEquals("category", actualBook.getCategory()),
            () -> assertEquals(10, actualBook.getQuantity()),
            () -> assertEquals(10.0, actualBook.getPrice()),
            () -> assertEquals(true, actualBook.isAvailable()),
            () -> assertEquals(userList, actualBook.getUsers())
        );
    }

    @Test
    void save_BookAlreadyExist() {
        // Arrange
        Book book = new Book();
        book.setBookName("demo");

        Book existingBook = new Book();
        existingBook.setBookName("demo");

        when(bookRepository.findAllBybookName(anyString())).thenReturn(existingBook);

        // Act & Assert
        assertThrows(ExceptionTranslator.UsernameAlreadyExistsException.class, () -> bookServiceIml.save(book));
        verify(bookRepository, times(1)).findAllBybookName(book.getBookName());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void update() {
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

        when(bookRepository.save(any())).thenReturn(expectedBook);

        Book actualBook = bookServiceIml.save(expectedBook);

        assertAll(
            () -> assertEquals("demo", actualBook.getBookName()),
            () -> assertEquals("author", actualBook.getAuthor()),
            () -> assertEquals("category", actualBook.getCategory()),
            () -> assertEquals(10, actualBook.getQuantity()),
            () -> assertEquals(10.0, actualBook.getPrice()),
            () -> assertEquals(true, actualBook.isAvailable()),
            () -> assertEquals(userList, actualBook.getUsers())
        );
    }

    @Test
    void partialUpdate() {
        Book existingBook = new Book();
        existingBook.setId("1");
        existingBook.setBookName("Test Book");
        existingBook.setPrice(10.0);
        existingBook.setQuantity(5);
        existingBook.setCategory("Fiction");

        BookDTO updatedBookDTO = new BookDTO();
        updatedBookDTO.setId("1");
        updatedBookDTO.setBookName("Updated Book");
        updatedBookDTO.setPrice(15.0);
        updatedBookDTO.setQuantity(10);
        updatedBookDTO.setCategory("Mystery");

        when(bookRepository.findById("1")).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        Optional<Book> result = bookServiceIml.partialUpdate(updatedBookDTO, "1");

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Updated Book", result.get().getBookName());
        Assertions.assertEquals(15.0, result.get().getPrice());
        Assertions.assertEquals(10, result.get().getQuantity());
        Assertions.assertEquals("Mystery", result.get().getCategory());
        verify(bookRepository, times(1)).findById("1");
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testFindAll() {
        // Given
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        List<Book> books = new ArrayList<>();
        Book existingBook = new Book();
        existingBook.setId("1");
        existingBook.setBookName("Test Book");
        existingBook.setAuthor("Author 1");
        existingBook.setPrice(10.0);
        existingBook.setQuantity(5);
        existingBook.setCategory("Fiction");

        Book existingBook2 = new Book();
        existingBook.setId("1");
        existingBook.setBookName("Test Book");
        existingBook2.setAuthor("Author 1");
        existingBook.setPrice(10.0);
        existingBook.setQuantity(5);
        existingBook.setCategory("Fiction");

        books.add(existingBook);
        books.add(existingBook2);
        Page<Book> page = new PageImpl<>(books);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        List<Book> result = bookServiceIml.findAll(pageNo, pageSize, sortBy);

        // Then
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(0).getBookName()).isEqualTo("Test Book");
        assertThat(result.get(0).getAuthor()).isEqualTo("Author 1");
    }

    @Test
    void searchBooksByKey() {
        BookDTO expectedBook = new BookDTO();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        BookDTO expectedBook2 = new BookDTO();
        expectedBook.setBookName("demo2");
        expectedBook.setAuthor("author2");
        expectedBook.setCategory("category2");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        List<BookDTO> bookList = new ArrayList<>();
        bookList.add(expectedBook);
        bookList.add(expectedBook2);

        when(bookRepository.findBybookNameContainingIgnoreCase(anyString())).thenReturn(bookList);

        List<BookDTO> actualList = bookServiceIml.searchBooksByKey("data");

        assertEquals(bookList, actualList);
    }

    @Test
    void findOne() {
        Book expectedBook = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        when(bookRepository.findById(anyString())).thenReturn(Optional.of(expectedBook));
        BookDTO responseEntity = bookServiceIml.findOne("id1");

        assertAll(
            () -> assertEquals("demo", responseEntity.getBookName()),
            () -> assertEquals("author", responseEntity.getAuthor()),
            () -> assertEquals("category", responseEntity.getCategory()),
            () -> assertEquals(10, responseEntity.getQuantity()),
            () -> assertEquals(10.0, responseEntity.getPrice()),
            () -> assertEquals(true, responseEntity.isAvailable())
        );
    }

    @Test
    public void testFindOne_BookNotFound() {
        // Arrange
        String bookId = "id1";
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
            ResponseStatusException.class,
            () -> {
                bookServiceIml.findOne(bookId);
            },
            "user with this id " + bookId + " not found"
        );

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void delete() {
        Book expectedBook = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        when(bookRepository.findById(anyString())).thenReturn(Optional.of(expectedBook));
        bookServiceIml.delete("id1");

        verify(bookRepository, times(1)).findById("id1");
        verify(bookRepository, times(1)).deleteById("id1");
    }

    @Test
    void getAvailableBooks() {
        Book expectedBook = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        Book expectedBook2 = new Book();
        expectedBook.setBookName("demo2");
        expectedBook.setAuthor("author2");
        expectedBook.setCategory("category2");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        List<Book> bookList = new ArrayList<>();
        bookList.add(expectedBook);
        bookList.add(expectedBook2);

        when(bookRepository.findByAvailableTrue()).thenReturn(bookList);

        List<Book> actualList = bookServiceIml.getAvailableBooks();

        assertEquals(bookList, actualList);
    }

    @Test
    void getUsersByBookId() {
        Book expectedBook = new Book();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setQuantity(10);
        expectedBook.setPrice(10.0);
        expectedBook.setAvailable(true);

        when(bookRepository.findById(anyString())).thenReturn(Optional.of(expectedBook));
        Book responseEntity = bookServiceIml.getUsersByBookId("id1");

        assertAll(
            () -> assertEquals("demo", responseEntity.getBookName()),
            () -> assertEquals("author", responseEntity.getAuthor()),
            () -> assertEquals("category", responseEntity.getCategory()),
            () -> assertEquals(10, responseEntity.getQuantity()),
            () -> assertEquals(10.0, responseEntity.getPrice()),
            () -> assertEquals(true, responseEntity.isAvailable())
        );
    }

    @Test
    void createReportForTwoDays() throws ParseException {
        String startDate = "01-01-2022";
        String endDate = "03-01-2022";

        HashMap<String, List> map = new HashMap<>();
        map.put("BookIssued", new ArrayList<>());
        map.put("BookReturned", new ArrayList<>());

        HashMap<String, Object> expectedData = new HashMap<>();
        expectedData.put("dataForTwoDays", map);
        when(bookRepository.findAllByIssueddateBetween(any(), any())).thenReturn(new ArrayList<>());
        when(bookRepository.findAllByReturneddateBetween(any(), any())).thenReturn(new ArrayList<>());

        // Act
        HashMap<String, Object> reportData = bookServiceIml.createReport(startDate, endDate);

        // Assert
        assertEquals(expectedData, reportData);
        verify(bookRepository, times(1)).findAllByIssueddateBetween(any(), any());
        verify(bookRepository, times(1)).findAllByReturneddateBetween(any(), any());
    }

    @Test
    void createReportForOneWeek() throws ParseException {
        String startDate = "03-01-2022";
        String endDate = "10-01-2022";

        HashMap<String, List> map = new HashMap<>();
        map.put("BookIssued", new ArrayList<>());
        map.put("BookReturned", new ArrayList<>());

        HashMap<String, Object> expectedData = new HashMap<>();
        expectedData.put("dataForOneWeek", map);
        when(bookRepository.findAllByIssueddateBetween(any(), any())).thenReturn(new ArrayList<>());
        when(bookRepository.findAllByReturneddateBetween(any(), any())).thenReturn(new ArrayList<>());

        System.out.println(expectedData);

        // Act
        HashMap<String, Object> reportData = bookServiceIml.createReport(startDate, endDate);
        System.out.println(reportData);

        // Assert
        assertEquals(expectedData, reportData);
        verify(bookRepository, times(1)).findAllByIssueddateBetween(any(), any());
        verify(bookRepository, times(1)).findAllByReturneddateBetween(any(), any());
    }

    @Test
    void createReportForSixMonth() throws ParseException {
        String startDate = "01-01-2022";
        String endDate = "01-07-2022";

        HashMap<String, List> map = new HashMap<>();
        map.put("BookIssued", new ArrayList<>());
        map.put("BookReturned", new ArrayList<>());

        HashMap<String, Object> expectedData = new HashMap<>();
        expectedData.put("dataForSixMonth", map);
        when(bookRepository.findAllByIssueddateBetween(any(), any())).thenReturn(new ArrayList<>());
        when(bookRepository.findAllByReturneddateBetween(any(), any())).thenReturn(new ArrayList<>());

        // Act
        HashMap<String, Object> reportData = bookServiceIml.createReport(startDate, endDate);

        // Assert
        assertEquals(expectedData, reportData);
        verify(bookRepository, times(1)).findAllByIssueddateBetween(any(), any());
        verify(bookRepository, times(1)).findAllByReturneddateBetween(any(), any());
    }

    @Test
    void createReportOneYear() throws ParseException {
        String startDate = "01-01-2022";
        String endDate = "01-01-2023";

        HashMap<String, List> map = new HashMap<>();
        map.put("BookIssued", new ArrayList<>());
        map.put("BookReturned", new ArrayList<>());

        HashMap<String, Object> expectedData = new HashMap<>();
        expectedData.put("dataForOneYear", map);
        when(bookRepository.findAllByIssueddateBetween(any(), any())).thenReturn(new ArrayList<>());
        when(bookRepository.findAllByReturneddateBetween(any(), any())).thenReturn(new ArrayList<>());

        // Act
        HashMap<String, Object> reportData = bookServiceIml.createReport(startDate, endDate);

        // Assert
        assertEquals(expectedData, reportData);
        verify(bookRepository, times(1)).findAllByIssueddateBetween(any(), any());
        verify(bookRepository, times(1)).findAllByReturneddateBetween(any(), any());
    }

    @Test
    void getByCriteria() {
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
        when(bookRepository.findBycategory(anyString())).thenReturn(bookList);
        List<Book> actualList = bookServiceIml.getByCriteria("author", "category");

        assertAll(
            () -> assertEquals("demo", actualList.get(0).getBookName()),
            () -> assertEquals("author", actualList.get(0).getAuthor()),
            () -> assertEquals("category", actualList.get(0).getCategory()),
            () -> assertEquals(10, actualList.get(0).getQuantity()),
            () -> assertEquals(10.0, actualList.get(0).getPrice()),
            () -> assertEquals(true, actualList.get(0).isAvailable())
        );
    }

    @Test
    public void testGetByInvalidCriteria() {
        try {
            bookServiceIml.getByCriteria("invalid", "value");
            fail("Expected BadRequestException not thrown");
        } catch (ExceptionTranslator.BadRequestException e) {
            // test passes
        }
    }
}
