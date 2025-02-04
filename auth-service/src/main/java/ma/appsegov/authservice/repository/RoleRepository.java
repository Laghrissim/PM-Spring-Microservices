package ma.appsegov.authservice.repository;

import ma.appsegov.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleById(Long id);

    Role findByNom(String roleName);
}
