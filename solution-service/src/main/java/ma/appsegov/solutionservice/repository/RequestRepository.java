package ma.appsegov.solutionservice.repository;

import ma.appsegov.solutionservice.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    @Query(value = "SELECT COUNT(*) FROM Request r INNER JOIN r.solution s WHERE r.contact_id = ?1 AND s.id = ?2")
    Long findByContact_idAndSolutionId(Long contact_id, Long solution_id);
}
