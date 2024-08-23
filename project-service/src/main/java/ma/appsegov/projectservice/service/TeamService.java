package ma.appsegov.projectservice.service;

import jakarta.transaction.Transactional;
import ma.appsegov.projectservice.DTO.ProjectDTO;
import ma.appsegov.projectservice.DTO.TeamDTO;
import ma.appsegov.projectservice.model.Team;

import java.util.List;

public interface TeamService {
    List<TeamDTO> getProjectTeams(Long idProject);

    @Transactional
    ProjectDTO addTeamToProject(Long teamId, Long projectId) throws Exception;

    @Transactional
    ProjectDTO removeTeamFromProject(Long teamId, Long projectId) throws Exception;

    Team getTeamById(Long id);

    TeamDTO createTeam(TeamDTO teamDTO);

    TeamDTO updateTeam(Long teamId, TeamDTO teamDTO);

    void deleteTeam(Long id);
}
