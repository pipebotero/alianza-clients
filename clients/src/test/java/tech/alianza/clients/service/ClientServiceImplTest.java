package tech.alianza.clients.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import tech.alianza.clients.domain.Client;
import tech.alianza.clients.dto.ClientCreationDTO;
import tech.alianza.clients.repository.ClientRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    private AutoCloseable autoCloseable;
    private ClientServiceImpl underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ClientServiceImpl(clientRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllClients() {
        underTest.getClients();
        verify(clientRepository).findAll();
    }

    @Test
    void getClientsByUsername() {
        String username = "jdoe";
        underTest.getClientsByUsername(username, 0, 5);
        verify(clientRepository).findClientsByUsername(username, PageRequest.of(0, 5));
    }

    @Test
    void getClientByUsername() {
        String username = "jdoe";
        underTest.getClientByUsername(username);
        verify(clientRepository).findClientByUsername(username);
    }

    @Test
    void canAddNewClient() {
        ClientCreationDTO clientDTO = new ClientCreationDTO(
                "John Doe",
                "jdoe@gmail.com",
                "3201129034"
        );
        Client client = underTest.toClient(clientDTO);
        underTest.addNewClient(client);
        ArgumentCaptor<Client> clientArgumentCaptor =
                ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientArgumentCaptor.capture());
        Client capturedClient = clientArgumentCaptor.getValue();
        assertEquals(capturedClient, client);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        Client client = new Client(
                "jdoe",
                "John Doe",
                "jdoe@gmail.com",
                "3201129034"
        );
        given(clientRepository.selectExistsEmail(client.getEmail())).willReturn(true);
        assertThrowsExactly(
                DataIntegrityViolationException.class,
                () -> underTest.addNewClient(client),
                "email taken"
        );
        verify(clientRepository, never()).save(any());
    }

    @Test
    @Disabled
    void deleteClient() {
    }

    @Test
    @Disabled
    void updateClient() {
    }
}