package com.mycompany.myapp.domain.bookEvent;

import com.mycompany.myapp.domain.Book;
import java.util.Optional;

public class Event {

    private int statusCode;
    private String message;
    private Object result;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Event(int statusCode, String message, Object result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public Event() {}
}
