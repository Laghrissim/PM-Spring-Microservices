package ma.appsegov.projectservice.repository;

import ma.appsegov.projectservice.model.Project;
import ma.appsegov.projectservice.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t WHERE t.project.id =?1")
    List<Team> findTeamsByProjectId(Long projectId);

    @Query("SELECT t FROM Team t WHERE t.id =?1 and t.project.id =?2")
    List<Team> findTeamByProjectId(Long teamId, Long projectId);

    boolean existsByIdAndProjectId(Long teamId, Long projectId);
}
