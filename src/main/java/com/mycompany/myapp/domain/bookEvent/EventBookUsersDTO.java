package com.mycompany.myapp.domain.bookEvent;

import com.mycompany.myapp.domain.dto.BookDTO;
import com.mycompany.myapp.domain.dto.BookUsersDTO;
import java.util.List;

public class EventBookUsersDTO {

    private int statusCode;
    private String message;
    private BookUsersDTO result;

    public EventBookUsersDTO(int statusCode, String message, BookUsersDTO result) {
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

    public BookUsersDTO getResult() {
        return result;
    }

    public void setResult(BookUsersDTO result) {
        this.result = result;
    }

    public EventBookUsersDTO() {}
}
