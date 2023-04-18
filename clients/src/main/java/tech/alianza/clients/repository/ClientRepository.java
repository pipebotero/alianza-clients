package tech.alianza.clients.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import tech.alianza.clients.domain.Client;

import java.util.List;
import java.util.Optional;

@Repository
//@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {


//    Optional<List<Client>> findAllBy(String username);

    @Query("SELECT c FROM Client c")
    Page<Client> getClients(Pageable pageable);

    @Query("SELECT c FROM Client c WHERE c.username LIKE %:username%")
    Page<Client> findClientsByUsername(String username, Pageable pageable);
    @Query("SELECT c FROM Client c WHERE c.username = ?1")
    Optional<Client> findClientByUsername(String username);
    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    Optional<Client> findClientByEmail(String email);

    @Query("" +
            "SELECT CASE WHEN COUNT(c) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Client c WHERE c.username = ?1"
    )
    Boolean selectExistsUsername(String username);

    @Query("" +
            "SELECT COUNT(c)" +
            "FROM Client c WHERE c.username LIKE ?1%"
    )
    int countSimilarUsername(String username);

    @Query("" +
            "SELECT CASE WHEN COUNT(c) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Client c WHERE c.email = ?1"
    )
    Boolean selectExistsEmail(String email);

}
