package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.dto.BookIssuedDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A User.
 */
@Document(collection = "user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank
    @Field("username")
    private String username;

    @NotBlank
    @Field("password")
    private String password;

    @NotBlank
    @Field("roles")
    private String roles;

    @Field("issueddate")
    private Date issueddate;

    @Field("returneddate")
    private Date returnedddate;

    //    private UserInfoDTO userInfoDTOSet;

    private List<BookIssuedDTO> books = new ArrayList<>();

    public User(String id, String username, String password, String roles, Date issueddate, Date returnedddate, List<BookIssuedDTO> books) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.issueddate = issueddate;
        this.returnedddate = returnedddate;
        this.books = books;
    }

    public User(String id1, String demo1, String pass1, String roleDemo1) {}

    public Date getIssueddate() {
        return issueddate;
    }

    public void setIssueddate(Date issueddate) {
        this.issueddate = issueddate;
    }

    public Date getReturnedddate() {
        return returnedddate;
    }

    public void setReturnedddate(Date returnedddate) {
        this.returnedddate = returnedddate;
    }

    public List<BookIssuedDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookIssuedDTO> books) {
        this.books = books;
    }

    public User() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public User id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public User username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public User password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return this.roles;
    }

    public User roles(String roles) {
        this.setRoles(roles);
        return this;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", roles='" + getRoles() + "'" +
            "}";
    }
}
