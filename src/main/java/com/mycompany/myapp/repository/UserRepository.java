package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.User;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Object findByUsername(String username);

    List<User> findAllByIssueddateBetween(Date dayStart, Date dayEnd);

    List<User> findAllByReturnedddateBetween(Date dayStart, Date dayEnd);
}
