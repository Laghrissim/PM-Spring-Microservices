package ma.appsegov.projectservice.repository;

import feign.Param;
import ma.appsegov.projectservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.client_id = :clientId OR :clientId MEMBER OF p.subscriberIds")
    List<Project> findByClientIdOrSubscriberIds(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.client_id = :clientId")
    int countByClientId(@Param("clientId") Long clientId);

    @Query("SELECT p.id FROM Project p WHERE p.solution_id = :solutionId AND p.contact_id = :clientId")
    Long findIdBySolutionIdAndContactId(Long solutionId, Long clientId);

    @Query("SELECT p FROM Project p WHERE p.solution_id = ?1")
    List<Project> findBySolutionId(Long solutionId);}
