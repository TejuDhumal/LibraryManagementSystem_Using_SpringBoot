package com.mycompany.myapp.web.rest;

import com.google.common.reflect.TypeToken;
import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.bookEvent.*;
import com.mycompany.myapp.domain.dto.*;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.service.BookService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.InvalidClassException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Book}.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "libraryManagementBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookService bookService;

    private final BookRepository bookRepository;

    public BookResource(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    Event response = new Event();
    EventBookDTO eventBookDTO = new EventBookDTO();

    EventBookDTOList eventBookDTOList = new EventBookDTOList();
    TypeToken<List<BookDTO>> typeToken = new TypeToken<List<BookDTO>>() {};
    ModelMapper mapper = new ModelMapper();

    /**
     * {@code POST  /books} : Create a new book.
     *
     * @param book the book to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new book, or with status {@code 400 (Bad Request)} if the book has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/books")
    public ResponseEntity createBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);
        Book result = bookService.save(book);
        Event response = new Event();
        response.setStatusCode(201);
        response.setMessage("Book created successfully");
        response.setResult(result);
        return ResponseEntity.created(new URI("/api/books/" + result.getId())).body(response);
    }

    /**
     * {@code PUT  /books/:id} : Updates an existing book.
     *
     * @param id the id of the book to save.
     * @param book the book to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated book,
     * or with status {@code 400 (Bad Request)} if the book is not valid,
     * or with status {@code 500 (Internal Server Error)} if the book couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/books/{id}")
    public ResponseEntity updateBook(@PathVariable(value = "id", required = false) final String id, @Valid @RequestBody Book book)
        throws URISyntaxException, InvalidClassException {
        log.debug("REST request to update Book : {}, {}", id, book);
        if (!bookRepository.existsById(book.getId()) && !bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found");
        }
        if (book.getId() == null) {
            throw new BadRequestAlertException("id is null", ENTITY_NAME, "idnull");
        }
        Book result = bookService.update(book);
        ModelMapper mapper = new ModelMapper();
        BookDTO bookDTO = mapper.map(result, BookDTO.class);
        eventBookDTO.setStatusCode(200);
        eventBookDTO.setMessage("Book retreived successfully");
        eventBookDTO.setResult(bookDTO);
        return ResponseEntity.ok(eventBookDTO);
    }

    /**
     * {@code PATCH  /books/:id} : Partial updates given fields of an existing book, field will ignore if it is null
     *
     * @param id the id of the book to save.
     * @param bookDTO the book to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated book,
     * or with status {@code 400 (Bad Request)} if the book is not valid,
     * or with status {@code 404 (Not Found)} if the book is not found,
     * or with status {@code 500 (Internal Server Error)} if the book couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity partialUpdateBook(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody BookDTO bookDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Book partially : {}, {}", id, bookDTO);
        if (id == null) {
            throw new BadRequestAlertException("id is null", ENTITY_NAME, "idnull");
        }
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found");
        }
        Optional<Book> book = bookService.partialUpdate(bookDTO, id);
        Book result = book.get();
        ModelMapper mapper = new ModelMapper();
        BookDTO bookDTO1 = mapper.map(result, BookDTO.class);
        eventBookDTO.setStatusCode(200);
        eventBookDTO.setMessage("Book updated successfully");
        eventBookDTO.setResult(bookDTO1);
        return ResponseEntity.ok(eventBookDTO);
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of books in body.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @GetMapping("/books")
    public ResponseEntity getAllBooks(
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy
    ) {
        log.debug("REST request to get all Books");
        List<Book> books = bookService.findAll(pageNo, pageSize, sortBy);
        TypeToken<List<BookDTO>> typeToken2 = new TypeToken<List<BookDTO>>() {};
        List<BookDTO> result = mapper.map(books, typeToken2.getType());
        eventBookDTOList.setStatusCode(200);
        if (result == null) {
            eventBookDTOList.setMessage("List is empty");
            eventBookDTOList.setResult(result);
            return ResponseEntity.ok(eventBookDTOList);
        } else {
            eventBookDTOList.setMessage("Books retrieved successfully");
            eventBookDTOList.setResult(result);
        }
        return ResponseEntity.ok(eventBookDTOList);
    }

    //
    /**
     * {@code GET  /books/:id} : get the "id" book.
     *
     * @param id the id of the book to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the book, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @GetMapping("/books/{id}")
    public ResponseEntity getBook(@PathVariable String id) {
        log.debug("REST request to get Book : {}", id);
        BookDTO result = bookService.findOne(id);
        eventBookDTO.setStatusCode(200);
        eventBookDTO.setMessage("Book getting successfully");
        eventBookDTO.setResult(result);
        return ResponseEntity.ok(eventBookDTO);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" book.
     *
     * @param id the id of the book to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/books/{id}")
    public ResponseEntity deleteBook(@PathVariable String id) {
        log.debug("REST request to delete Book : {}", id);
        Optional<Book> book = bookRepository.findById(id);
        bookService.delete(id);
        EventOptionalBook eventOptionalBook = new EventOptionalBook();
        eventOptionalBook.setStatusCode(200);
        eventOptionalBook.setMessage("Book deleted successfully");
        eventOptionalBook.setResult(book);
        return ResponseEntity.ok(eventOptionalBook);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @GetMapping("/books/find")
    public ResponseEntity getBookByCriteria(@RequestParam String author, @RequestParam String category) {
        log.debug("REST request to find Book : {}", author, category);
        List<Book> books = bookService.getByCriteria(author, category);
        List<BookDTO> result = mapper.map(books, typeToken.getType());
        eventBookDTOList.setStatusCode(200);
        if (result.isEmpty()) {
            eventBookDTOList.setMessage("List is empty");
            eventBookDTOList.setResult(result);
            return ResponseEntity.ok(eventBookDTOList);
        } else {
            eventBookDTOList.setMessage("Books retrieved successfully");
            eventBookDTOList.setResult(result);
        }
        return ResponseEntity.ok(eventBookDTOList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @GetMapping("/books/available")
    public ResponseEntity getBookByAvalability() {
        List<Book> books = bookService.getAvailableBooks();
        List<BookDTO> result = mapper.map(books, typeToken.getType());
        eventBookDTOList.setStatusCode(200);
        if (result.isEmpty()) {
            eventBookDTOList.setMessage("List is empty");
            eventBookDTOList.setResult(result);
            return ResponseEntity.ok(eventBookDTOList);
        } else {
            eventBookDTOList.setMessage("Books retrieved successfully");
            eventBookDTOList.setResult(result);
        }
        return ResponseEntity.ok(eventBookDTOList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("books/user/{bookId}")
    public ResponseEntity getUsersByBookId(@PathVariable String bookId) {
        Book result = bookService.getUsersByBookId(bookId);
        BookUsersDTO bookUsersDTO = mapper.map(result, BookUsersDTO.class);
        EventBookUsersDTO eventBookUsersDTO = new EventBookUsersDTO();
        eventBookUsersDTO.setStatusCode(200);
        eventBookUsersDTO.setMessage("users retreived successfully");
        eventBookUsersDTO.setResult(bookUsersDTO);
        return ResponseEntity.ok(eventBookUsersDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @GetMapping("/books/key")
    public ResponseEntity getAllBooksByKeyword(@RequestParam("key") String key) {
        log.debug("REST request to get all Books by key " + key);
        List<BookDTO> result = bookService.searchBooksByKey(key);
        eventBookDTOList.setStatusCode(200);
        if (result.isEmpty()) {
            eventBookDTOList.setMessage("No books available");
            eventBookDTOList.setResult(result);
            return ResponseEntity.ok(eventBookDTOList);
        }
        eventBookDTOList.setMessage("Books retrieved successfully");
        eventBookDTOList.setResult(result);
        return ResponseEntity.ok(eventBookDTOList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/books/report")
    public ResponseEntity createReport(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate)
        throws ParseException {
        HashMap<String, Object> map = bookService.createReport(startDate, endDate);
        response.setStatusCode(200);
        response.setMessage("Report generated successfully");
        response.setResult(map);
        return ResponseEntity.ok(response);
    }
}
