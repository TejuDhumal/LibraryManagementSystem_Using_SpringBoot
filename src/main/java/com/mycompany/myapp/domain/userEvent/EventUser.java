package com.mycompany.myapp.domain.userEvent;

import com.mycompany.myapp.domain.User;

public class EventUser {

    private int statusCode;
    private String message;
    private User result;

    public EventUser(int statusCode, String message, User result) {
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

    public User getResult() {
        return result;
    }

    public void setResult(User result) {
        this.result = result;
    }

    public EventUser() {}
}
