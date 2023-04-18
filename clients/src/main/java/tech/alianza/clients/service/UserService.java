package tech.alianza.clients.service;

import tech.alianza.clients.domain.Role;
import tech.alianza.clients.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    List<User> getUsers();

    User getUserById(Long clientId);
}
