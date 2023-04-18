package tech.alianza.clients.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.alianza.clients.repository.ClientRepository;
import tech.alianza.clients.domain.Client;

import java.util.List;

@Configuration
public class ClientConfig {

    @Bean
    CommandLineRunner commandLineRunner(ClientRepository clientRepository) {
        return args -> {
            Client maria = new Client(
                    "mmelo",
                    "Maria Melo",
                    "mariamelo@gmail.com",
                    "3212030321"
            );
            Client alex = new Client(
                    "acampos",
                    "Alex Campos",
                    "alexcampos@gmail.com",
                    "3112920210"
            );
            Client andres = new Client(
                    "abarrios",
                    "Andres Barrios",
                    "andbarrios@gmail.com",
                    "3398920012"
            );
            Client carlos = new Client(
                    "cceballos",
                    "Carlos Ceballos",
                    "cceballos12@gmail.com",
                    "3229890011"
            );
            Client martha = new Client(
                    "mlopez",
                    "Martha Lopez",
                    "mlopez@gmail.com",
                    "31099201120"
            );
            Client daniela = new Client(
                    "dmartinez",
                    "Daniela Martinez",
                    "dmartinez@gmail.com",
                    "3119203904"
            );
            Client sebastian = new Client(
                    "scampos",
                    "Sebastian Campos",
                    "scampos90@gmail.com",
                    "3209182903"
            );
            clientRepository.saveAll(
                    List.of(maria, alex, andres, carlos, martha, daniela, sebastian)
            );;
        };
    }
}
