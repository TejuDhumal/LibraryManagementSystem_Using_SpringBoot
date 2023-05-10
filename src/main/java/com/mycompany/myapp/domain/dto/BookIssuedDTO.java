package com.mycompany.myapp.domain.dto;

import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Field;

public class BookIssuedDTO {

    @NotBlank(message = "id must not be blank")
    private String id;

    @NotBlank(message = "bookName must not be blank")
    private String bookName;

    @NotBlank(message = "author name must not be blank")
    private String author;

    @NotNull
    private double price;

    @NotBlank(message = "category must not be blank")
    private String category;

    public BookIssuedDTO(String id, String bookName, String author, double price, String category) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.category = category;
    }

    public BookIssuedDTO() {}

    public BookIssuedDTO(String bookId) {}

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BookIssuedDTO bookIssuedDTO = (BookIssuedDTO) obj;
        return Objects.equals(id, bookIssuedDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
