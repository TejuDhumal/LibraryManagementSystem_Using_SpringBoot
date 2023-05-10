package com.mycompany.myapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.dto.BookIssuedDTO;
import com.mycompany.myapp.domain.dto.UserDTO;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import java.text.ParseException;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.userServiceImpl = new UserServiceImpl(this.userRepository, this.passwordEncoderMock, this.bookRepository);
    }

    @Test
    void save() {
        BookIssuedDTO expectedBook = new BookIssuedDTO();
        expectedBook.setBookName("demo");
        expectedBook.setAuthor("author");
        expectedBook.setCategory("category");
        expectedBook.setPrice(10.0);

        List<BookIssuedDTO> bookList = new ArrayList<>();
        bookList.add(expectedBook);

        User expectedUser = new User();
        expectedUser.setUsername("Demo");
        expectedUser.setRoles("ROLE_DEMO");
        expectedUser.setPassword("pass1");
        expectedUser.setBooks(bookList);

        when(userRepository.save(any())).thenReturn(expectedUser);
        when(passwordEncoderMock.encode(expectedUser.getPassword())).thenReturn("hashedPassword");

        User actualUser = userServiceImpl.save(expectedUser);

        assertAll(
            () -> assertEquals("Demo", actualUser.getUsername()),
            () -> assertEquals("ROLE_DEMO", actualUser.getRoles()),
            () -> assertEquals(passwordEncoderMock.encode("pass1"), actualUser.getPassword()),
            () -> assertEquals(bookList, actualUser.getBooks())
        );
    }

    @Test
    public void testSave_UserWithExistingUsername_ThrowsUsernameAlreadyExistsException() {
        // Arrange
        User user = new User();
        user.setUsername("john");
        user.setPassword("password123");

        User existingUser = new User();
        existingUser.setUsername("john");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(existingUser);

        // Act & Assert
        assertThrows(ExceptionTranslator.UsernameAlreadyExistsException.class, () -> userServiceImpl.save(user));
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(passwordEncoderMock, never()).encode(user.getPassword());
        verify(userRepository, never()).save(any());
    }

    @Test
    void findAll() {
        List<User> expectedUsers = Arrays.asList(
            new User("id1", "demo1", "pass1", "ROLE_DEMO1"),
            new User("id2", "demo2", "pass2", "ROLE_DEMO2"),
            new User("id3", "demo3", "pass3", "ROLE_DEMO3")
        );
        when(userRepository.findAll()).thenReturn(expectedUsers);
        List<User> actualUsers = userServiceImpl.findAll();
        assertThat(expectedUsers).isEqualTo(actualUsers);
    }

    @Test
    void findOne() {
        User expectedUser = new User();
        expectedUser.setId("id1");
        expectedUser.setUsername("Demo");
        expectedUser.setRoles("ROLE_DEMO");
        expectedUser.setPassword("pass1");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(expectedUser));
        User responseEntity = userServiceImpl.findOne("id1");

        assertAll(
            () -> assertEquals("id1", responseEntity.getId()),
            () -> assertEquals("Demo", responseEntity.getUsername()),
            () -> assertEquals("ROLE_DEMO", responseEntity.getRoles()),
            () -> assertEquals("pass1", responseEntity.getPassword())
        );
    }

    @Test
    public void testFindOne_UserNotFound() {
        // Arrange
        String userId = "id1";
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
            ResponseStatusException.class,
            () -> {
                userServiceImpl.findOne(userId);
            },
            "user with this id " + userId + " not found"
        );

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void delete() {
        User expectedUser = new User();
        expectedUser.setId("id1");
        expectedUser.setUsername("Demo");
        expectedUser.setRoles("ROLE_DEMO");
        expectedUser.setPassword("pass1");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(expectedUser));
        userServiceImpl.delete("id1");

        verify(userRepository, times(1)).findById("id1");
        verify(userRepository, times(1)).deleteById("id1");
    }

    @Test
    public void testDelete_UserNotFound() {
        // Arrange
        String userId = "id1";
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
            ResponseStatusException.class,
            () -> {
                userServiceImpl.delete(userId);
            },
            "user with this id " + userId + " not found"
        );

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testIssueBookAndUserExistBookIssuedSuccessfully() {
        // Arrange
        String userId = "userDemo";
        String bookId = "bookDemo";

        User user = new User();
        user.setId(userId);

        BookIssuedDTO bookDTO = new BookIssuedDTO();
        bookDTO.setId(bookId);

        Book book = new Book();
        book.setId(bookId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        User result = userServiceImpl.issueBook(userId, bookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getBooks().size());
        assertEquals(bookId, result.getBooks().get(0).getId());
        verify(userRepository, times(1)).save(user);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testIssueBookNotFoundReturnsResponseStatusException() {
        // Arrange
        String userId = "userDemo";
        String bookId = "bookDemo";

        User user = new User();
        user.setId(userId);

        BookIssuedDTO bookDTO = new BookIssuedDTO();
        bookDTO.setId(bookId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userServiceImpl.issueBook(userId, bookDTO));
        verify(userRepository, never()).save(user);
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void testIssueBookUserNotFoundReturnsResponseStatusException() {
        // Arrange
        String userId = "userDemo";
        String bookId = "bookDemo";

        BookIssuedDTO bookDTO = new BookIssuedDTO();
        bookDTO.setId(bookId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userServiceImpl.issueBook(userId, bookDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void returnBook() {
        String userId = "user1";
        String bookId = "book1";
        BookIssuedDTO bookDTO = new BookIssuedDTO();
        bookDTO.setId(bookId);

        User user = new User();
        user.setId(userId);

        UserDTO userDTO = new UserDTO();
        user.setId(userId);
        Book book = new Book();
        book.setId(bookId);
        user.getBooks().add(bookDTO);
        book.getUsers().add(userDTO);

        Optional<User> userOptional = Optional.of(user);
        Optional<Book> bookOptional = Optional.of(book);

        // Set current date
        Date date = new Date();

        // Mock repository methods
        when(userRepository.findById(userId)).thenReturn(userOptional);
        when(bookRepository.findById(bookId)).thenReturn(bookOptional);

        // Call the returnBook method
        User returnedUser = userServiceImpl.returnBook(userId, bookDTO);

        // Assertions
        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(0, returnedUser.getBooks().size());
        Assertions.assertEquals(date.toString(), returnedUser.getReturnedddate().toString());
        Assertions.assertEquals(date.toString(), book.getReturneddate().toString());

        // Verify repository method invocations
        verify(bookRepository).save(book);
        verify(userRepository).save(user);
    }
}
