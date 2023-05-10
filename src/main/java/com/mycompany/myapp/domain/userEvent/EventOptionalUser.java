package com.mycompany.myapp.domain.userEvent;

import com.mycompany.myapp.domain.User;
import java.util.Optional;

public class EventOptionalUser {

    private int statusCode;
    private String message;
    private Optional<User> result;

    public EventOptionalUser(int statusCode, String message, Optional<User> result) {
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

    public Optional<User> getResult() {
        return result;
    }

    public void setResult(Optional<User> result) {
        this.result = result;
    }

    public EventOptionalUser() {}
}
