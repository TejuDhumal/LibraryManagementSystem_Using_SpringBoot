package com.mycompany.myapp.service.impl;

import com.google.common.reflect.TypeToken;
import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.dto.BookDTO;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.BookService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;

        this.userRepository = userRepository;
    }

    @Override
    public Book save(Book book) {
        Book book1 = bookRepository.findAllBybookName(book.getBookName());
        if (book1 != null) {
            throw new ExceptionTranslator.UsernameAlreadyExistsException("Book with this book name already exist");
        }
        log.debug("Request to save Book : {}", book);
        return bookRepository.save(book);
        // add the user to the book object

    }

    @Override
    public Book update(Book book) {
        log.debug("Request to update Book : {}", book);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> partialUpdate(BookDTO bookDTO, String id) {
        log.debug("Request to partially update Book : {}", bookDTO);

        return bookRepository
            .findById(id)
            .map(existingProduct -> {
                if (bookDTO.getBookName() != null) {
                    existingProduct.setBookName(bookDTO.getBookName());
                }
                if (bookDTO.getPrice() != 0) {
                    existingProduct.setPrice(bookDTO.getPrice());
                }
                if (bookDTO.getQuantity() != 0) {
                    existingProduct.setQuantity(bookDTO.getQuantity());
                }
                if (bookDTO.getCategory() != null) {
                    existingProduct.setCategory(bookDTO.getCategory());
                }

                return existingProduct;
            })
            .map(bookRepository::save);
    }

    @Override
    public List<Book> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Book> books = bookRepository.findAll(paging);
        System.out.println(books);
        if (books.hasContent()) {
            return books.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<BookDTO> searchBooksByKey(String key) {
        List<BookDTO> books = bookRepository.findBybookNameContainingIgnoreCase(key);
        return books;
    }

    @Override
    public BookDTO findOne(String id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id " + id + " not found"));
        ModelMapper mapper = new ModelMapper();
        BookDTO bookDTO = mapper.map(book, BookDTO.class);
        return bookDTO;
    }

    @Override
    public void delete(String id) {
        User user = new User();
        System.out.println(user.getBooks());
        log.debug("Request to delete Book : {}", id);
        Book book = bookRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id " + id + " not found"));
        if (!book.getUsers().isEmpty()) {
            System.out.println(user.getBooks().size());
            //            System.out.println(user.getBooks().get(0));
            throw new ExceptionTranslator.BadRequestException("cannot delete this book, It is issued to user");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getByCriteria(String author, String category) {
        if (category == null || category.isEmpty()) {
            throw new ExceptionTranslator.BadRequestException("Category not provided");
        }
        List<Book> books = bookRepository.findBycategory(category);

        if (books.isEmpty()) {
            throw new ExceptionTranslator.BadRequestException("Books of " + category + " category is not available");
        }
        List<Book> bookList = books
            .stream()
            .filter(book -> author == null || author.isEmpty() || book.getAuthor().equalsIgnoreCase(author))
            .collect(Collectors.toList());

        if (bookList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Books by this author not available in " + category + " category");
        }
        return bookList;
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    @Override
    public Book getUsersByBookId(String bookId) {
        Book book = bookRepository
            .findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with this id " + bookId + " not found"));
        return book;
    }

    public HashMap<String, List> getDataOfReport(Date startDate, Date endDate) {
        ModelMapper mapper = new ModelMapper();
        TypeToken<List<BookDTO>> typeToken = new TypeToken<List<BookDTO>>() {};
        HashMap<String, List> map1 = new HashMap<>();
        List<Book> booksIssued = bookRepository.findAllByIssueddateBetween(startDate, endDate);
        List<Book> booksReturned = bookRepository.findAllByReturneddateBetween(startDate, endDate);
        List<BookDTO> bookIssued = mapper.map(booksIssued, typeToken.getType());
        List<BookDTO> bookReturned = mapper.map(booksReturned, typeToken.getType());

        map1.put("BookIssued", bookIssued);
        map1.put("BookReturned", bookReturned);
        return map1;
    }

    @Override
    public HashMap<String, Object> createReport(String startDate, String endDate) throws ParseException {
        String datePattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(endDate);
        HashMap<String, Object> map = new HashMap<>();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(start);
        calendar1.add(Calendar.DAY_OF_WEEK, 2);
        Date endTwoDay = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(start);
        calendar2.add(Calendar.DAY_OF_MONTH, 7);
        Date endWeek = calendar2.getTime();

        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(start);
        calendar3.add(Calendar.MONTH, 6);
        Date endSixMonth = calendar3.getTime();

        Calendar calendar4 = Calendar.getInstance();
        calendar4.setTime(start);
        calendar4.add(Calendar.YEAR, 1);
        Date endYear = calendar4.getTime();

        if (start.equals(end)) {
            end.setHours(23);
            end.setMinutes(59);
            end.setSeconds(60);
            map.put("dataForOneDay", getDataOfReport(start, end));
            return map;
        }
        if (end.equals(endTwoDay)) {
            map.put("dataForTwoDays", getDataOfReport(start, end));
            return map;
        }
        if (end.equals(endWeek)) {
            map.put("dataForOneWeek", getDataOfReport(start, end));
            return map;
        }
        if (end.equals(endSixMonth)) {
            map.put("dataForSixMonth", getDataOfReport(start, end));
            return map;
        }
        if (end.equals(endYear)) {
            map.put("dataForOneYear", getDataOfReport(start, end));
            return map;
        } else {
            throw new ExceptionTranslator.BadRequestException("enter correct date type");
        }
    }
}
