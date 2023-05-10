package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.dto.UserDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Book.
 */
@Document(collection = "book")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @NotBlank
    @Field("book_name")
    private String bookName;

    @NotNull
    @NotBlank
    @Field("author")
    private String author;

    @NotNull
    @Field("price")
    @Min(1)
    private Double price;

    @NotNull
    @Field("quantity")
    @Min(1)
    private Integer quantity;

    @Field("category")
    @NotBlank
    private String category;

    @Field("available")
    @NotNull(message = "availability status cannot be null")
    private boolean available;

    @Field("issuedDate")
    private Date issueddate;

    @Field("returnedDate")
    private Date returneddate;

    //        @DBRef
    @Field("users")
    private List<UserDTO> users = new ArrayList<>();

    public Book(
        String id,
        String bookName,
        String author,
        Double price,
        Integer quantity,
        String category,
        boolean available,
        Date issueddate,
        Date returneddate,
        List<UserDTO> users
    ) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.available = available;
        this.issueddate = issueddate;
        this.returneddate = returneddate;
        this.users = users;
    }

    public Date getIssueddate() {
        return issueddate;
    }

    public void setIssueddate(Date issueddate) {
        this.issueddate = issueddate;
    }

    public Date getReturneddate() {
        return returneddate;
    }

    public void setReturneddate(Date returneddate) {
        this.returneddate = returneddate;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Book id(String id) {
        this.setId(id);
        return this;
    }

    public Book() {}

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return this.bookName;
    }

    public Book bookName(String bookName) {
        this.setBookName(bookName);
        return this;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return this.author;
    }

    public Book author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return this.price;
    }

    public Book price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Book quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Book category(String category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore


    @Override
    public String toString() {
        return "Book{" +
            "id='" + id + '\'' +
            ", bookName='" + bookName + '\'' +
            ", author='" + author + '\'' +
            ", price=" + price +
            ", quantity=" + quantity +
            ", category='" + category + '\'' +
            ", available=" + available +
            ", users=" + users +
            '}';
    }
}
