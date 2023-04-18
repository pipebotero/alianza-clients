package tech.alianza.clients.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.alianza.clients.domain.Client;
import tech.alianza.clients.domain.User;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldFindUserByUsername() {
        String username = "awatson";
        User user = new User(
                null,
                "Ashley Watson",
                username,
                "awatson@gmail.com",
                "1234",
                new ArrayList<>(),
                false,
                true
        );
        underTest.save(user);
        Optional<User> userToFind = underTest.findByUsername(username);
        assertTrue(userToFind.isPresent());
    }
}