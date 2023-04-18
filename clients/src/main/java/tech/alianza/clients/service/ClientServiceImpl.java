package tech.alianza.clients.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.alianza.clients.domain.Client;
import tech.alianza.clients.dto.ClientCreationDTO;
import tech.alianza.clients.dto.ClientDTO;
import tech.alianza.clients.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> getClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

    @Override
    public Page<ClientDTO> getClientsPageable(Integer pageNumber, Integer size) {
        log.info("Fetching all clients as pageable");
        return clientRepository.getClients(PageRequest.of(pageNumber, size)).map(this::toDTO);
    }

    @Override
    public Page<ClientDTO> getClientsByUsername(
            String username,
            Integer pageNumber,
            Integer size) {
        log.info("Getting clients by username");
        Page<Client> clients =
                clientRepository.findClientsByUsername(username, PageRequest.of(pageNumber, size));
        if (clients == null) {
            return null;
        }
        return clients.map(this::toDTO);
    }

    @Override
    public Optional<Client> getClientByUsername(String username) {
        log.info("Getting one client by username");
        return clientRepository.findClientByUsername(username);
    }

    @Override
    public Page<Client> findBySearchCriteria
            (Specification<Client> spec, Pageable page){
        log.info("Search clients by criteria");
        Page<Client> searchResult = clientRepository.findAll(spec,
                page);
        return searchResult;
    }

    @Override
    public Client addNewClient(Client client) throws DataIntegrityViolationException {
        log.info("Adding new client");
        Boolean existEmail = clientRepository
                .selectExistsEmail(client.getEmail());
        if(existEmail) {
            throw new DataIntegrityViolationException("email taken");
        }
        Boolean existUsername = clientRepository
                .selectExistsUsername(client.getUsername());
        if(existUsername) {
            int numOfClients = clientRepository.countSimilarUsername(client.getUsername());
            StringBuilder sb = new StringBuilder();
            sb.append(client.getUsername());
            sb.append(numOfClients + 1);
            String newUsername = sb.toString();
            client.setUsername(newUsername);
        }
        Client clientSaved = clientRepository.save(client);
        return clientSaved;
    }

    @Override
    public void deleteClient(Long clientId) {
        log.info("Deleting client");
        boolean exists = clientRepository.existsById(clientId);
        if (!exists) {
            throw new IllegalStateException(
                    "student whit id" + clientId + "does not exist");
        }
        clientRepository.deleteById(clientId);
    }

    @Override
    @Transactional
    public void updateClient(Long clientId,
                             String username,
                             String name,
                             String email,
                             String phone) {
        log.info("Updating client");
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException(
                        "client with id " + clientId + " does not exist"
                ));

        if (username != null && username.length() > 0 &&
                !Objects.equals(client.getUsername(), username)) {
            Optional<Client> clientOptional = clientRepository
                    .findClientByUsername(username);
            if(clientOptional.isPresent()) {
                throw new IllegalStateException("shared key taken");
            }
            client.setEmail(email);
        }
        if (name != null && name.length() > 0 &&
                !Objects.equals(client.getName(), name)) {
            client.setName(name);
        }
        if (phone != null && phone.length() > 0 &&
                !Objects.equals(client.getPhone(), phone)) {
            client.setPhone(phone);
        }
        if (email != null && email.length() > 0 &&
                !Objects.equals(client.getEmail(), email)) {
            Optional<Client> clientOptional = clientRepository
                    .findClientByEmail(email);
            if(clientOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            client.setEmail(email);
        }
    }

    @Override
    public ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getUsername(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getCreatedAt(),
                client.getUpdatedAt()
                );
    }

    @Override
    public List<ClientDTO> toDTOList(List<Client> clients) {
        return clients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Client toClient(ClientCreationDTO clientDTO) {
        String[] arrOfName = clientDTO.getName().trim().split(" ", 2);
        StringBuilder sb = new StringBuilder();
        if(arrOfName.length > 1) {
            sb.append(arrOfName[0].toLowerCase().charAt(0));
            sb.append(arrOfName[1].toLowerCase());
        } else {
            sb.append(arrOfName[0].toLowerCase());
        }
        String username = sb.toString();
        return new Client(username, clientDTO.getName().trim(), clientDTO.getEmail(), clientDTO.getPhone());
    }

}
