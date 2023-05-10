package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.dto.BookIssuedDTO;
import java.text.ParseException;
import java.util.List;

/**
 * Service Interface for managing {@link User}.
 */
public interface UserService {
    /**
     * Save a user.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    User save(User user);

    /**
     * Updates a user.
     *
     * @param user the entity to update.
     * @return the persisted entity.
     */

    /**
     * Partially updates a user.
     *
     * @param user the entity to update partially.
     * @return the persisted entity.
     */

    /**
     * Get all the users.
     *
     * @return the list of entities.
     */
    List<User> findAll();

    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    User findOne(String id);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    User issueBook(String userId, BookIssuedDTO bookIssuedDTO);

    User returnBook(String userId, BookIssuedDTO bookDTO);

    List<User> findUserDataOneDay(String date, String type) throws ParseException;
}
