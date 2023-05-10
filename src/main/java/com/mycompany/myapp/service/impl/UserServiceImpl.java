package com.mycompany.myapp.service.impl;

import com.google.common.reflect.TypeToken;
import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.dto.*;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BookRepository bookRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
    }

    List<BookDTO> books = new ArrayList<>();

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = (User) userRepository.findByUsername(user.getUsername());
        if (user1 != null) {
            throw new ExceptionTranslator.UsernameAlreadyExistsException("Username already exists");
        } else {
            log.debug("Request to save User : {}", user);
            return userRepository.save(user);
        }
    }

    @Override
    public List<User> findAll() {
        log.debug("Request to get all Users");
        List<User> users = this.userRepository.findAll();
        return users;
    }

    @Override
    public User findOne(String id) {
        log.debug("Request to get User : {}", id);
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with this id " + id + " not found"));
        return user;
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete User : {}", id);
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with this id " + id + " not found"));
        userRepository.deleteById(id);
    }

    @Override
    public User issueBook(String userId, BookIssuedDTO bookIssuedDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book1 = bookRepository.findById(bookIssuedDTO.getId());

        if (book1.isPresent() && user.isPresent()) {
            Book book2 = book1.get();
            User user1 = user.get();
            // set current date
            Date date = new Date();

            // ADD USER DTO WTHOUT BOOK LIST
            // MAP
            ModelMapper mapper = new ModelMapper();
            UserDTO userDTO = mapper.map(user1, UserDTO.class);
            book2.setIssueddate(date);
            book2.getUsers().add(userDTO);

            // ADD BOOK DTO WITHOUT USER LIST
            //MAP
            BookIssuedDTO bookDTO1 = mapper.map(book2, BookIssuedDTO.class);
            user1.setIssueddate(date);
            user1.getBooks().add(bookDTO1);

            bookRepository.save(book2);
            userRepository.save(user1);
            return user1;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public User returnBook(String userId, BookIssuedDTO bookDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book1 = bookRepository.findById(bookDTO.getId());

        // set current date
        Date date = new Date();
        if (book1.isPresent() && user.isPresent()) {
            Book book2 = book1.get();
            User user1 = user.get();

            // ADD USER DTO WTHOUT BOOK LIST
            // MAP
            ModelMapper mapper = new ModelMapper();
            UserDTO userDTO = mapper.map(user1, UserDTO.class);
            book2.setReturneddate(date);
            book2.getUsers().remove(userDTO);

            // ADD BOOK DTO WITHOUT USER LIST
            //MAP
            BookIssuedDTO bookDTO1 = mapper.map(book2, BookIssuedDTO.class);
            user1.setReturnedddate(date);
            user1.getBooks().remove(bookDTO1);

            bookRepository.save(book2);
            userRepository.save(user1);
            return user1;
        }
        System.out.println("else");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<User> findUserDataOneDay(String date, String type) throws ParseException {
        String datePattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date dayStart = simpleDateFormat.parse(date);
        Date dayEnd = simpleDateFormat.parse(date);

        dayEnd.setHours(23);
        dayEnd.setMinutes(59);
        dayEnd.setSeconds(60);

        switch (type) {
            case "issued":
                return userRepository.findAllByIssueddateBetween(dayStart, dayEnd);
            case "returned":
                return userRepository.findAllByReturnedddateBetween(dayStart, dayEnd);
            default:
                throw new ExceptionTranslator.BadRequestException("enter correct date type");
        }
    }
}
