package ma.appsegov.projectservice.service;

import ma.appsegov.projectservice.DTO.ProjectDTO;
import ma.appsegov.projectservice.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<ProjectDTO> getAllProjects();

    ProjectDTO getProjectById(Long id);

    Project createProject(ProjectDTO solutionDTO);

    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);

    void deleteProject(Long id);

    List<ProjectDTO> getAllProjectsByClientIdOrSubscriberIds(Long clientId);

    int getProjectCountByClientId(Long clientId);

    Long getProjectIdBySolutionIdAndClientId(Long solutionId, Long clientId);

    Project addSubscriberToProject(Long projectId, Long subscriberId);

    boolean isUserSubscribed(Long projectId, Long userId);

    Optional<Project> updateProjectStage(Long id, String stage);

    Project addOrUpdateManager(Long projectId, Long managerId);

    Project removeManager(Long projectId);

    Project removeSubscriberFromProject(Long projectId, Long subscriberId);

    void deleteProjectsBySolutionId(Long solutionId);

    long getUserCount();
}
