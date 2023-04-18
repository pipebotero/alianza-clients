package tech.alianza.clients.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.alianza.clients.domain.Client;
import tech.alianza.clients.dto.ClientCreationDTO;
import tech.alianza.clients.dto.ClientDTO;

import java.util.List;
import java.util.Optional;

@Service
public interface ClientService {

    List<Client> getClients();

    Page<ClientDTO> getClientsPageable(Integer pageNumber, Integer size);

    Page<ClientDTO> getClientsByUsername(String username, Integer pageNumber, Integer size);

    Optional<Client> getClientByUsername(String username);

    Page<Client> findBySearchCriteria(Specification<Client> spec, Pageable page);

    Client addNewClient(Client client);

    void deleteClient(Long clientId);

    @Transactional
    void updateClient(Long clientId,
                             String username,
                             String name,
                             String email,
                             String phone);

    ClientDTO toDTO(Client client);
    Client toClient(ClientCreationDTO clientDTO);

    List<ClientDTO> toDTOList(List<Client> clients);

}
