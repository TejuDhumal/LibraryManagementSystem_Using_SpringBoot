package com.mycompany.myapp.domain.dto;

import com.mycompany.myapp.domain.Book;
import javax.validation.constraints.NotBlank;

public class BookDTO {

    @NotBlank(message = "id must not be blank")
    private String id;

    @NotBlank(message = "bookName must not be blank")
    private String bookName;

    private String author;
    private double price;
    private int quantity;
    private String category;
    private boolean available;

    public BookDTO(String id, String bookName, String author, double price, int quantity, String category, boolean available) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookDTO(String bookName, String author, double price, int quantity) {
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    public BookDTO() {}

    public BookDTO(String id) {}
}
