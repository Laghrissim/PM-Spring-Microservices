package ma.appsegov.authservice.repository;

import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findUserById(Long id);

    boolean existsByEmail(String email);

    User findByUsername( String email);

    @Query("SELECT c FROM Contact c JOIN User u ON c.id = u.contact.id WHERE u.id =?1")
    Contact findContactByUserId(Long userId);

    List<User> findByRoles_Nom(String projectManager);
}
