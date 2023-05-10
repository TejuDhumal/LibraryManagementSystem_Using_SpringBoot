package com.mycompany.myapp.web.rest;

import com.google.common.reflect.TypeToken;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.bookEvent.Event;
import com.mycompany.myapp.domain.dto.*;
import com.mycompany.myapp.domain.userEvent.EventOptionalUser;
import com.mycompany.myapp.domain.userEvent.EventUser;
import com.mycompany.myapp.domain.userEvent.EventUserDTO;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.service.UserService;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "libraryManagementUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    public UserResource(
        UserService userService,
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        TokenProvider tokenProvider
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    EventUser eventUser = new EventUser();

    EventUserDTO eventUserDTO = new EventUserDTO();

    /**
     * {@code POST  /users} : Create a new user.
     *
     * @param user the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the user has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User : {}", user);
        User result = userService.save(user);
        eventUser.setStatusCode(201);
        eventUser.setMessage("user created successfully");
        eventUser.setResult(result);
        return ResponseEntity.created(new URI("/api/users/" + result.getId())).body(eventUser);
    }

    /**
     * {@code PUT  /users/:id} : Updates an existing user.
     *
     * @param id   the id of the user to save.
     * @param user the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user,
     * or with status {@code 400 (Bad Request)} if the user is not valid,
     * or with status {@code 500 (Internal Server Error)} if the user couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    /**
     * {@code PATCH  /users/:id} : Partial updates given fields of an existing user, field will ignore if it is null
     *
     * @param id   the id of the user to save.
     * @param user the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user,
     * or with status {@code 400 (Bad Request)} if the user is not valid,
     * or with status {@code 404 (Not Found)} if the user is not found,
     * or with status {@code 500 (Internal Server Error)} if the user couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    /**
     * {@code GET  /users} : get all the users.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        log.debug("REST request to get all Users");
        List<User> result = userService.findAll();
        TypeToken<List<UserDTO>> typeToken = new TypeToken<List<UserDTO>>() {};
        ModelMapper mapper = new ModelMapper();
        List<UserDTO> userDto1 = mapper.map(result, typeToken.getType());
        eventUserDTO.setStatusCode(200);
        eventUserDTO.setMessage("users retrived successfully");
        eventUserDTO.setResult(userDto1);
        return ResponseEntity.ok(eventUserDTO);
    }

    /**
     * {@code GET  /users/:id} : get the "id" user.
     *
     * @param id the id of the user to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the user, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        log.debug("REST request to get User : {}", id);
        User result = userService.findOne(id);
        eventUser.setStatusCode(200);
        eventUser.setMessage("user retreived successfully");
        eventUser.setResult(result);
        return ResponseEntity.ok(eventUser);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param id the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        log.debug("REST request to delete User : {}", id);
        Optional<User> result = userRepository.findById(id);
        userService.delete(id);
        EventOptionalUser eventOptionalUser = new EventOptionalUser();
        eventOptionalUser.setStatusCode(200);
        eventOptionalUser.setMessage("user deleted successfully");
        eventOptionalUser.setResult(result);
        return ResponseEntity.ok(eventOptionalUser);
    }

    @PostMapping("/authenticate")
    public String authRequest(@Valid @RequestBody AuthRequest authRequest) {
        System.out.println("authrequest");
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return tokenProvider.createToken(authentication, true);
        } else {
            return "username not found";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/bookissued/{userId}")
    public ResponseEntity issueBook(@Valid @PathVariable String userId, @RequestBody BookIssuedDTO bookIssuedDTO) {
        User result = userService.issueBook(userId, bookIssuedDTO);
        eventUser.setStatusCode(200);
        eventUser.setMessage("Books issued successfully");
        eventUser.setResult(result);
        return ResponseEntity.ok(eventUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/bookReturned/{userId}")
    public ResponseEntity returnBook(@Valid @PathVariable String userId, @RequestBody BookIssuedDTO bookDTO) {
        User result = userService.returnBook(userId, bookDTO);
        eventUser.setStatusCode(200);
        eventUser.setMessage("Books returned successfully");
        eventUser.setResult(result);
        return ResponseEntity.ok(eventUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/oneday")
    public ResponseEntity findUserIssuedInOneDay(@RequestParam("date") String date, @RequestParam("type") String type)
        throws ParseException {
        List<User> users = userService.findUserDataOneDay(date, type);
        TypeToken<List<UserDTO>> typeToken = new TypeToken<List<UserDTO>>() {};
        ModelMapper mapper = new ModelMapper();
        List<UserDTO> result = mapper.map(users, typeToken.getType());
        eventUserDTO.setStatusCode(200);
        if (!result.isEmpty()) {
            eventUserDTO.setMessage("users retrived successfully");
            eventUserDTO.setResult(result);
            return ResponseEntity.ok(eventUserDTO);
        } else {
            eventUserDTO.setMessage("No books issued to user on date" + date);
            eventUserDTO.setResult(result);
            return ResponseEntity.ok(eventUserDTO);
        }
    }
}
