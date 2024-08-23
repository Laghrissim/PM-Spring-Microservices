package ma.appsegov.projectservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.appsegov.projectservice.DTO.ProjectDTO;
import ma.appsegov.projectservice.client.PrincipalRestClient;
import ma.appsegov.projectservice.client.Request.UserIdRequest;
import ma.appsegov.projectservice.exception.ProjectNotFoundException;
import ma.appsegov.projectservice.model.Project;
import ma.appsegov.projectservice.repository.ProjectRepository;
import ma.appsegov.projectservice.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PrincipalRestClient principalRestClient;


    /*private final PrincipalRestClient principalRestClient;*/
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ProjectDTO> getAllProjects() {

        return projectRepository.findAll().stream().map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project with id " + id + " not found !"));

        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public Project createProject(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);
        projectRepository.save(project);
        return project;
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project with id " + id + " not found !"));

        Project project = modelMapper.map(projectDTO, Project.class);
        project.setId(existingProject.getId());

        return modelMapper.map( projectRepository.save(existingProject), ProjectDTO.class);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectDTO> getAllProjectsByClientIdOrSubscriberIds(Long clientId) {
        List<Project> projects = projectRepository.findByClientIdOrSubscriberIds(clientId);
        return mapProjectsToDTOs(projects);
    }

    @Override
    public int getProjectCountByClientId(Long clientId) {
        return projectRepository.countByClientId(clientId);
    }

    @Override
    public Long getProjectIdBySolutionIdAndClientId(Long solutionId, Long clientId) {

        UserIdRequest userIdRequest = new UserIdRequest(clientId);

        Long contactId= principalRestClient.findContactByUserId(userIdRequest).getId();


        return projectRepository.findIdBySolutionIdAndContactId(solutionId, contactId);

    }

    @Override
    public Project addSubscriberToProject(Long projectId, Long subscriberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        if (!project.getSubscriberIds().contains(subscriberId)) {
            project.getSubscriberIds().add(subscriberId);
            return projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Subscriber is already subscribed to the project");
        }
    }




    @Override
    public boolean isUserSubscribed(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        return project.getSubscriberIds().stream().anyMatch(subscriber -> subscriber.equals(userId));

    }

    @Override
    public Optional<Project> updateProjectStage(Long projectId, String stage) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setStage(stage);
            projectRepository.save(project);
            return Optional.of(project);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Project addOrUpdateManager(Long projectId, Long managerId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setManager_id(managerId);
            return projectRepository.save(project);
        }
        throw new IllegalArgumentException("Project not found with ID: " + projectId);

    }

    @Override
    public Project removeManager(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setManager_id(null);
            return projectRepository.save(project);
        }
        throw new IllegalArgumentException("Project not found with ID: " + projectId);

    }

    @Override
    public Project removeSubscriberFromProject(Long projectId, Long subscriberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        project.getSubscriberIds().remove(subscriberId);
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void deleteProjectsBySolutionId(Long solutionId) {
        // Find projects associated with the given solutionId and delete them
        List<Project> projects = projectRepository.findBySolutionId(solutionId);
        projectRepository.deleteAll(projects);
    }

    @Override
    public long getUserCount() {

        return projectRepository.count();
    }

    private List<ProjectDTO> mapProjectsToDTOs(List<Project> projects) {
        return projects.stream()
                .map(this::mapProjectToDTO)
                .collect(Collectors.toList());
    }

    private ProjectDTO mapProjectToDTO(Project project) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(project, ProjectDTO.class);
    }

}
