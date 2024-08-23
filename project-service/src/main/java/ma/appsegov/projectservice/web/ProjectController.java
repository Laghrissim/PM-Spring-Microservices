package ma.appsegov.projectservice.web;

import lombok.RequiredArgsConstructor;
import ma.appsegov.projectservice.DTO.ProjectCreationDTO;
import ma.appsegov.projectservice.DTO.ProjectDTO;
import ma.appsegov.projectservice.model.Project;
import ma.appsegov.projectservice.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> listProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO projectDTO = projectService.getProjectById(id);
        return ResponseEntity.ok().body(projectDTO);
    }

    @PostMapping("/request/save")
    public ResponseEntity<?> saveProjectAfterRequestValidation(@RequestBody ProjectCreationDTO projectDTO) {

        if (projectService.createProject(ProjectDTO.builder().description(projectDTO.getDescription()).client_id(projectDTO.getClient_id()).name(projectDTO.getName()).build()) != null)
            return ResponseEntity.status(HttpStatus.CREATED).body("Project created Successfully  !");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error during project creation !");
    }

    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody ProjectDTO projectDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectDTO));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsByClientIdOrSubscriberIds(@PathVariable("clientId") Long clientId) {
        try {
            List<ProjectDTO> projects = projectService.getAllProjectsByClientIdOrSubscriberIds(clientId);
            return ResponseEntity.ok(projects);
        } catch (Exception ex) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count/{clientId}")
    public ResponseEntity<Integer> getProjectCountByClientId(@PathVariable("clientId") Long clientId) {
        try {
            int projectCount = projectService.getProjectCountByClientId(clientId);
            return ResponseEntity.ok(projectCount);
        } catch (Exception ex) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getProjectId")
    public ResponseEntity<Long> getProjectIdBySolutionIdAndClientId(@RequestParam Long solutionId, @RequestParam Long clientId) {
        Long projectId = projectService.getProjectIdBySolutionIdAndClientId(solutionId, clientId);
        return ResponseEntity.ok(projectId);
    }
    @PostMapping("/{projectId}/subscribe")
    public ResponseEntity<Project> subscribeToProject(@PathVariable Long projectId, @RequestParam Long subscriberId) {
        Project project = projectService.addSubscriberToProject(projectId, subscriberId);
        return ResponseEntity.ok(project);
    }
    @PostMapping("/{projectId}/unsubscribe")
    public ResponseEntity<Project> unsubscribeFromProject(@PathVariable Long projectId, @RequestParam Long subscriberId) {
        Project project = projectService.removeSubscriberFromProject(projectId, subscriberId);
        return ResponseEntity.ok(project);
    }
    @GetMapping("/{projectId}/subscribed")
    public ResponseEntity<Boolean> isUserSubscribed(@PathVariable Long projectId, @RequestParam Long userId) {
        boolean isSubscribed = projectService.isUserSubscribed(projectId, userId);
        return ResponseEntity.ok(isSubscribed);
    }

    @PutMapping("/{id}/stage")
    public ResponseEntity<Project> updateProjectStage(@PathVariable Long id, @RequestParam String stage) {
        Optional<Project> updatedProject = projectService.updateProjectStage(id, stage);
        if (updatedProject.isPresent()) {
            return ResponseEntity.ok(updatedProject.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{projectId}/manager")
        public ResponseEntity<Project> addOrUpdateManager(@PathVariable Long projectId, @RequestParam Long managerId) {
        Project updatedProject = projectService.addOrUpdateManager(projectId, managerId);
        return ResponseEntity.ok(updatedProject);
    }

    // Endpoint to remove the manager for a project
    @DeleteMapping("/{projectId}/manager")
    public ResponseEntity<Project> removeManager(@PathVariable Long projectId) {
        Project updatedProject = projectService.removeManager(projectId);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/solution/{solutionId}")
    public ResponseEntity<String> deleteProjectsBySolutionId(@PathVariable Long solutionId) {
        try {
            projectService.deleteProjectsBySolutionId(solutionId);
            return ResponseEntity.ok("Projects associated with solution " + solutionId + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting projects associated with solution " + solutionId);
        }
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = projectService.getUserCount();
        return ResponseEntity.ok(count);
    }

}
