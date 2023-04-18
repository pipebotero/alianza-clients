package tech.alianza.clients.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.alianza.clients.advsearch.ClientSearchDTO;
import tech.alianza.clients.advsearch.ClientSpecificationBuilder;
import tech.alianza.clients.advsearch.SearchCriteria;
import tech.alianza.clients.domain.Client;
import tech.alianza.clients.dto.ClientCreationDTO;
import tech.alianza.clients.dto.ClientDTO;
import tech.alianza.clients.repository.ClientRepository;
import tech.alianza.clients.service.ClientService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/v1/client")
public class ClientController {

    private final ClientService clientService;
//    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
//        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<Client> allClients = clientService.getClients();
        List<ClientDTO> allClientDTO = clientService.toDTOList(allClients);
        return ResponseEntity.ok().body(allClientDTO);
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<ClientDTO>> getClientsPageable(
            @RequestParam(defaultValue = "0") final Integer pageNumber,
            @RequestParam(defaultValue = "5") final Integer size
    ) {
        return ResponseEntity.ok().body(clientService.getClientsPageable(pageNumber, size));
    }

    @GetMapping("/data")
    public ResponseEntity<Page<ClientDTO>> getClientsByUsername(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") final Integer pageNumber,
            @RequestParam(defaultValue = "5") final Integer size
    ) {
        Page<ClientDTO> clients = this.clientService.getClientsByUsername(username, pageNumber, size);
        return ResponseEntity.ok().body(clients);
    }

    @PostMapping("/search")
    public ResponseEntity searchClients(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody ClientSearchDTO clientSearchDTO
    ) {
        ClientSpecificationBuilder builder = new
                ClientSpecificationBuilder();
        List<SearchCriteria> criteriaList =
                clientSearchDTO.getSearchCriteriaList();
        if(!criteriaList.isEmpty()) {
            criteriaList.forEach(x->
            {x.setDataOption(clientSearchDTO
                    .getDataOption());
                builder.with(x);
            });
        }
        Pageable page = PageRequest.of(pageNumber, size);
        Page<Client> clientPage =
                clientService.findBySearchCriteria(builder.build(),
                        page);
        return new ResponseEntity<>(clientPage, HttpStatus.OK);
    }
//
//    @GetMapping
//    public Optional<Client> getClient(
//            @RequestParam(required = false) String username
//    ) {
//        return this.clientService.getClientByUsername(username);
//    }

    @PostMapping
    public ResponseEntity registerNewClient(@RequestBody ClientCreationDTO clientDTO) {
        try {
            Client client = clientService.toClient(clientDTO);
            Client clientSaved = this.clientService.addNewClient(client);
            StringBuilder sb = new StringBuilder();
            sb.append("/api/v1/client/");
            sb.append(clientSaved.getId().toString());
            return ResponseEntity.created(new URI(sb.toString())).build();
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email has been taken");
        }
    }

    @DeleteMapping(path="{clientId}")
    public void deleteClient(@PathVariable("clientId") Long clientId) {
        clientService.deleteClient(clientId);
    }

    @PutMapping(path="{clientId}")
    public void updateClient(
            @PathVariable("clientId") Long clientId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {
        clientService.updateClient(clientId, username, name, email, phone);
    }
}
