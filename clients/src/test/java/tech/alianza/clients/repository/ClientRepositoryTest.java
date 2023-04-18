package tech.alianza.clients.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tech.alianza.clients.domain.Client;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository underTest;

    @Test
    void itShouldFindClientsByUsername() {
        String username = "awatson";
        Client client1 = new Client(
                username,
                "Ashley Watson",
                "awatson@gmail.com",
                "3112920098"
        );
        String username2 = "awatson2";
        Client client2 = new Client(
                username2,
                "Andrea Watson",
                "andreawatson@gmail.com",
                "3132920011"
        );
        underTest.save(client1);
        underTest.save(client2);
        Pageable page = PageRequest.of(0, 5);
        Page<Client> clients = underTest.findClientsByUsername(username, page);
        assertEquals(2, clients.getNumberOfElements());
    }

    @Test
    void itShouldFindClientByUsername() {
        String username = "jdoe";
        Client client = new Client(
                username,
                "John Doe",
                "jdoe@gmail.com",
                "3201129034"
        );
        underTest.save(client);
        Optional<Client> optionalClient = underTest.findClientByUsername(username);
        assertTrue(optionalClient.isPresent());
    }

    @Test
    void itShouldFindClientByEmail() {
        String email = "jdoe@gmail.com";
        Client client = new Client(
                "jdoe",
                "John Doe",
                email,
                "3201129034"
        );
        underTest.save(client);
        Optional<Client> optionalClient = underTest.findClientByEmail(email);
        assertTrue(optionalClient.isPresent());
    }

    @Test
    void itShouldCheckIfExistEmail() {
        String email = "jdoe@gmail.com";
        Client client = new Client(
                "jdoe",
                "John Doe",
                email,
                "3201129034"
        );
        underTest.save(client);
        boolean emailExist = underTest.selectExistsEmail(email);
        assertTrue(emailExist);
    }

    @Test
    void itShouldCheckIfExistUsername() {
        String username = "jdoe";
        Client client = new Client(
                username,
                "John Doe",
                "jdoe@gmail.com",
                "3201129034"
        );
        underTest.save(client);
        boolean usernameExist = underTest.selectExistsUsername(username);
        assertTrue(usernameExist);
    }

    @Test
    void itShouldCountSimilarUsername() {
        String username = "acampos";
        Client client1 = new Client(
                "acampos",
                "Alex Campos",
                "alexcampos@gmail.com",
                "3201129034"
        );
        Client client2 = new Client(
                "acampos2",
                "Andrea Campos",
                "andreacampos@gmail.com",
                "3119029903"
        );
        List<Client> clients = List.of(client1, client2);
        underTest.saveAll(clients);
        int numOfClients = underTest.countSimilarUsername("acampos");
        assertEquals(2, numOfClients);
    }
}