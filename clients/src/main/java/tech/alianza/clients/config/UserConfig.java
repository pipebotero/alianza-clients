package tech.alianza.clients.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.alianza.clients.domain.Role;
import tech.alianza.clients.domain.User;
import tech.alianza.clients.service.UserService;

import java.util.ArrayList;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(
                    new User(
                            null,
                            "Jhon Doe",
                            "jdoe",
                            "jdoe@gmail.com",
                            "Abcd1234",
                            new ArrayList<Role>(),
                            false,
                            true
                    )
            );
            userService.saveUser(
                    new User(
                            null,
                            "George travolta",
                            "gtravolta",
                            "gtravolta@gmail.com",
                            "Abcd1234",
                            new ArrayList<Role>(),
                            false,
                            true
                    )
            );

            userService.addRoleToUser("jdoe", "ROLE_USER");
            userService.addRoleToUser("jdoe", "ROLE_MANAGER");

            userService.addRoleToUser("gtravolta", "ROLE_ADMIN");
            userService.addRoleToUser("gtravolta", "ROLE_SUPER_ADMIN");
        };
    }
}
