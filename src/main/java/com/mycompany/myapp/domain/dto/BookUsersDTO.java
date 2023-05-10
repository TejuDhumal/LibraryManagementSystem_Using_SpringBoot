package com.mycompany.myapp.domain.dto;

import com.mycompany.myapp.domain.User;
import java.util.List;

public class BookUsersDTO {

    private String id;
    private String bookName;
    private String category;
    private String author;
    private List<UserDTO> users;

    public BookUsersDTO() {}

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public BookUsersDTO(String id, String bookName, String category, String author, List<UserDTO> users) {
        this.id = id;
        this.bookName = bookName;
        this.category = category;
        this.author = author;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
