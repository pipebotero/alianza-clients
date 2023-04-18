package tech.alianza.clients.domain;

import jakarta.persistence.*;
import lombok.Data;
import tech.alianza.clients.core.domain.BaseEntity;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Entity(name = "Client")
@Table(
        name = "client",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "client_email_unique",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "client_username_unique",
                        columnNames = "username"
                )
        }
)
public class Client extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "client_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "username",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String username;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "phone",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String phone;

    public Client(String username,
                   String name,
                   String email,
                   String phone) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Client() {

    }
}
