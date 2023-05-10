package com.mycompany.myapp.domain.bookEvent;

import com.mycompany.myapp.domain.dto.BookDTO;

public class EventBookDTO {

    private int statusCode;
    private String message;
    private BookDTO result;

    public EventBookDTO(int statusCode, String message, BookDTO result) {
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

    public BookDTO getResult() {
        return result;
    }

    public void setResult(BookDTO result) {
        this.result = result;
    }

    public EventBookDTO() {}
}
