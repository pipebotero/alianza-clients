package tech.alianza.clients.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.alianza.clients.advsearch.ClientSearchDTO;
import tech.alianza.clients.advsearch.SearchCriteria;
import tech.alianza.clients.dto.ClientCreationDTO;
import tech.alianza.clients.service.ClientServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
class ClientControllerTest {

    private final static String BASE_URL = "/api/v1/client";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void itShouldReturnAllClients() throws Exception {
        MvcResult mockMvcResult =  mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, mockMvcResult.getResponse().getStatus());
    }

    @Test
    void itShouldReturnAllClientsPageable() throws Exception {
        MvcResult mockMvcResult =  mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL.concat("/pageable"))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, mockMvcResult.getResponse().getStatus());
    }

    @Test
    void itShouldReturnClientsPageableByUsername() throws Exception {
        MvcResult mockMvcResult =  mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL.concat("/data"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("username", "carlos")
                )
                .andReturn();
        assertEquals(200, mockMvcResult.getResponse().getStatus());
    }

    @Test
    void itShouldReturnClientsPageableBySearchCriteria() throws Exception {
        ClientSearchDTO clientSearchDTO = buildClientSearchDTO();
        MvcResult mockMvcResult =  mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL.concat("/search"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(clientSearchDTO))
                )
                .andReturn();
        assertEquals(200, mockMvcResult.getResponse().getStatus());
    }

    @Test
    void registerNewClient() throws Exception {
        ClientCreationDTO clientCreationDTO = buildClientDTO();
        MvcResult mockMvcResult =  mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(clientCreationDTO)))
                .andReturn();
        assertEquals(201, mockMvcResult.getResponse().getStatus());
    }

    @Test
    void willThrowException() throws Exception {
        ClientCreationDTO clientCreationDTO = new ClientCreationDTO();
        MvcResult mockMvcResult =  mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(clientCreationDTO)))
                .andReturn();
        assertEquals(400, mockMvcResult.getResponse().getStatus());
    }

    @Test
    @Disabled
    void deleteClient() {
    }

    @Test
    @Disabled
    void updateClient() {
    }

    private ClientCreationDTO buildClientDTO() {
        ClientCreationDTO clientCreationDTO = new ClientCreationDTO(
                "Ana Gimenez",
                "anagimenez@hotmail.com",
                "2122920022"
        );
        return clientCreationDTO;
    }

    private ClientSearchDTO buildClientSearchDTO() {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        SearchCriteria searchCriteria1 = new SearchCriteria(
                "username",
                "cn",
                "acampos"
        );
        SearchCriteria searchCriteria2 = new SearchCriteria(
                "email",
                "eq",
                "acampos@gmail.com"
        );
        searchCriteriaList.add(searchCriteria1);
        searchCriteriaList.add(searchCriteria2);
        ClientSearchDTO clientSearchDTO = new ClientSearchDTO(
                searchCriteriaList,
                "all"
        );
        return clientSearchDTO;
    }
}