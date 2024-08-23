package ma.appsegov.projectservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.appsegov.projectservice.DTO.ProjectDTO;
import ma.appsegov.projectservice.DTO.TeamDTO;
import ma.appsegov.projectservice.exception.TeamNotFoundException;
import ma.appsegov.projectservice.model.Project;
import ma.appsegov.projectservice.model.Team;
import ma.appsegov.projectservice.repository.ProjectRepository;
import ma.appsegov.projectservice.repository.TeamRepository;
import ma.appsegov.projectservice.service.ProjectService;
import ma.appsegov.projectservice.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    private final ProjectService projectService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<TeamDTO> getProjectTeams(Long idProject) {

        Project project = projectRepository.findById(idProject).orElse(null);
        if (project != null) {

            return teamRepository.findTeamsByProjectId(idProject).stream().map(team -> modelMapper.map(team, TeamDTO.class)).toList();
        }
        return null;
    }

    @Override
    @Transactional
    public ProjectDTO addTeamToProject(Long teamId, Long projectId) throws Exception {
        Team team = getTeamById(teamId);
        Project project = modelMapper.map(projectService.getProjectById(projectId),Project.class);

        if (!teamRepository.existsByIdAndProjectId(teamId, projectId)) {

            project.getTeams().add(team);
            team.setProject(project);

            teamRepository.save(team);
            return modelMapper.map(projectRepository.save(project), ProjectDTO.class);
        }
        throw new Exception(MessageFormat.format("The team {0} is already working on the Project {1}", team.getName(), project.getName()));

    }

    @Override
    @Transactional
    public ProjectDTO removeTeamFromProject(Long teamId, Long projectId) throws Exception {
        Team team = getTeamById(teamId);
        Project project = modelMapper.map(projectService.getProjectById(projectId),Project.class);

        if (teamRepository.existsByIdAndProjectId(teamId, projectId)) {

            project.getTeams().remove(team);
            team.setProject(null);

            teamRepository.save(team);
            return modelMapper.map(projectRepository.save(project), ProjectDTO.class);
        }
        throw new Exception(MessageFormat.format("The team {0} is not working on the Project {1}", team.getName(), project.getName()));

    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException("Team with id " + id + " not found !"));
    }

    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        return modelMapper.map(teamRepository.save(modelMapper.map(teamDTO, Team.class)), TeamDTO.class);
    }


    @Override
    public TeamDTO updateTeam(Long idTeam, TeamDTO teamDTO) {
        Team trouve = teamRepository.findById(idTeam).orElseThrow(() -> new TeamNotFoundException("Team with id " + idTeam + " not found !"));

        Team team = modelMapper.map(teamDTO, Team.class);
        team.setId(trouve.getId());
        return modelMapper.map(teamRepository.save(team), TeamDTO.class);

    }


    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }


}
