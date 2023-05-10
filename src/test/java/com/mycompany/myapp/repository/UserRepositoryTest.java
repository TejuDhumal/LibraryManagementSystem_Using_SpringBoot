package com.mycompany.myapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.mycompany.myapp.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        String username = "john_doe";
        User user = new User();
        user.setUsername(username);
        user.setPassword("pass1");
        user.setRoles("ROLE_DEMO");
        userRepository.save(user);

        // Act
        Object result = userRepository.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof User);
        assertEquals(username, ((User) result).getUsername());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}
