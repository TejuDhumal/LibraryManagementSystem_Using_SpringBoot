package com.mycompany.myapp.domain.dto;

import com.mycompany.myapp.domain.Book;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UserDTO {

    private String id;

    private String username;

    private String roles;

    public UserDTO(String id, String username, String roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDTO() {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) obj;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
