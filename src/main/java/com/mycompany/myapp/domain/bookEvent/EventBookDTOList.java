package com.mycompany.myapp.domain.bookEvent;

import com.mycompany.myapp.domain.dto.BookDTO;
import java.util.List;

public class EventBookDTOList {

    private int statusCode;
    private String message;
    private List<BookDTO> result;

    public EventBookDTOList(int statusCode, String message, List<BookDTO> result) {
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

    public List<BookDTO> getResult() {
        return result;
    }

    public void setResult(List<BookDTO> result) {
        this.result = result;
    }

    public EventBookDTOList() {}
}
