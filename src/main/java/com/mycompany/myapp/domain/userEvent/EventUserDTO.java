package com.mycompany.myapp.domain.userEvent;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.dto.UserDTO;
import java.util.List;

public class EventUserDTO {

    private int statusCode;
    private String message;
    private List<UserDTO> result;

    public EventUserDTO(int statusCode, String message, List<UserDTO> result) {
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

    public List<UserDTO> getResult() {
        return result;
    }

    public void setResult(List<UserDTO> result) {
        this.result = result;
    }

    public EventUserDTO() {}
}
