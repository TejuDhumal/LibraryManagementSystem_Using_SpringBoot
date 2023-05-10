package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.dto.*;
import com.mycompany.myapp.domain.userEvent.EventOptionalUser;
import com.mycompany.myapp.domain.userEvent.EventUser;
import com.mycompany.myapp.domain.userEvent.EventUserDTO;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.service.impl.UserServiceImpl;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @InjectMocks
    private UserResource userResource;

    @Mock
    private UserServiceImpl userServiceImpl;

    private MockMvc mockMvc;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    void createUser() throws URISyntaxException {
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

        when(userServiceImpl.save(any())).thenReturn(expectedUser);

        ResponseEntity response = userResource.createUser(expectedUser);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        //        Assertions.assertTrue(response.getBody() instanceof Event);
        EventUser responseBody = (EventUser) response.getBody();
        Assertions.assertEquals(201, responseBody.getStatusCode());
        Assertions.assertEquals("user created successfully", responseBody.getMessage());
        Assertions.assertEquals(expectedUser, responseBody.getResult());
        Assertions.assertEquals("/api/users/null", response.getHeaders().getLocation().toString());
        verify(userServiceImpl, times(1)).save(expectedUser);
    }

    @Test
    void getAllUsers() {
        User user1 = new User();
        user1.setUsername("user2");
        user1.setRoles("ROLE_DEMO");

        User user2 = new User();
        user2.setUsername("user1");
        user2.setRoles("ROLE_DEMO");

        // Arrange
        //        MockitoAnnotations.openMocks(this);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userServiceImpl.findAll()).thenReturn(users);

        // Act
        ResponseEntity response = userResource.getAllUsers();

        EventUserDTO responseBody = (EventUserDTO) response.getBody();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(200, responseBody.getStatusCode()),
            () -> assertEquals("users retrived successfully", responseBody.getMessage()),
            () -> assertEquals(2, responseBody.getResult().size())
        );
        verify(userServiceImpl, times(1)).findAll();
    }

    @Test
    void getUser() {
        User expectedUser = new User();
        expectedUser.setUsername("demo");
        expectedUser.setRoles("ROLE_DEMO");

        when(userServiceImpl.findOne(anyString())).thenReturn(expectedUser);

        ResponseEntity response = userResource.getUser("id1");

        EventUser actualBook = (EventUser) response.getBody();
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals("user retreived successfully", actualBook.getMessage()),
            () -> assertEquals("demo", actualBook.getResult().getUsername()),
            () -> assertEquals("ROLE_DEMO", actualBook.getResult().getRoles())
        );
    }

    @Test
    void deleteUser() {
        String userId = "1";
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity response = userResource.deleteUser(userId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventOptionalUser);
        EventOptionalUser responseBody = (EventOptionalUser) response.getBody();
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("user deleted successfully", responseBody.getMessage());
        Assertions.assertTrue(responseBody.getResult().isPresent());
        Assertions.assertEquals(userId, responseBody.getResult().get().getId());
        Assertions.assertEquals("user1", responseBody.getResult().get().getUsername());
        verify(userServiceImpl, times(1)).delete(userId);
    }

    @Test
    void authRequest() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("test_username");
        request.setPassword("test_password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(tokenProvider.createToken(authentication, true)).thenReturn("test_token");
        when(authentication.isAuthenticated()).thenReturn(true);

        mockMvc
            .perform(
                post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(content().string("test_token"));
    }

    @Test
    void issueBook() {
        // Arrange
        String userId = "1";
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        BookIssuedDTO bookDTO = new BookIssuedDTO();
        bookDTO.setId("1");
        bookDTO.setBookName("Book 1");

        when(userServiceImpl.issueBook(userId, bookDTO)).thenReturn(user);

        // Act
        ResponseEntity response = userResource.issueBook(userId, bookDTO);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof EventUser);
        EventUser responseBody = (EventUser) response.getBody();
        Assertions.assertEquals(200, responseBody.getStatusCode());
        Assertions.assertEquals("Books issued successfully", responseBody.getMessage());
        Assertions.assertEquals(userId, responseBody.getResult().getId());
        Assertions.assertEquals("user1", responseBody.getResult().getUsername());
        verify(userServiceImpl, times(1)).issueBook(userId, bookDTO);
    }

    @Test
    public void testReturnBook() throws Exception {
        // Set up test data
        String userId = "123";
        BookIssuedDTO bookDTO = new BookIssuedDTO();
        bookDTO.setId("456");
        bookDTO.setBookName("demo");

        Date date = new Date();

        User user = new User();
        user.setId(userId);
        user.setUsername("John Doe");
        user.setReturnedddate(date);

        when(userServiceImpl.returnBook(userId, bookDTO)).thenReturn(user);

        // Perform the API call
        ResponseEntity response = userResource.returnBook(userId, bookDTO);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EventUser responseEvent = (EventUser) response.getBody();
        assertNotNull(responseEvent);
        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("Books returned successfully", responseEvent.getMessage());
        assertEquals(user, responseEvent.getResult());
    }
}
