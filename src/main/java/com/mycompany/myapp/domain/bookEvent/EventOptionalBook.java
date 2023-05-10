package com.mycompany.myapp.domain.bookEvent;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.dto.BookDTO;
import java.util.Optional;

public class EventOptionalBook {

    private int statusCode;
    private String message;
    private Optional<Book> result;

    public EventOptionalBook(int statusCode, String message, Optional<Book> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Optional<Book> getResult() {
        return result;
    }

    public void setResult(Optional<Book> result) {
        this.result = result;
    }

    public EventOptionalBook() {}
}
