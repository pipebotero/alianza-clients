package tech.alianza.clients.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.alianza.clients.domain.Client;
import tech.alianza.clients.domain.Role;
import tech.alianza.clients.dto.ClientCreationDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;

    @Test
    void itShouldFindRoleByName() {
        String roleName = "ROLE_USER";
        Role roleToSave = buildRole();
        underTest.save(roleToSave);
        Optional<Role> clients = underTest.findByName(roleName);
        assertEquals(true, clients.isPresent());
    }

    private Role buildRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return role;
    }
}