package ma.appsegov.solutionservice.client;

import ma.appsegov.solutionservice.client.request.ProjectCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "project-service", contextId = "projectRestClient")
public interface ProjectRestClient {

    @PostMapping("/projects")
    void createProject(@RequestBody ProjectCreationRequest request);

    @GetMapping("/projects/{projectId}/subscribed")
    boolean isUserSubscribed(@PathVariable("projectId") Long projectId, @RequestParam("userId") Long userId);

    @GetMapping("/projects/getProjectId")
    ResponseEntity<Long> getProjectIdBySolutionIdAndClientId(@RequestParam("solutionId") Long solutionId, @RequestParam("clientId") Long clientId);
    @DeleteMapping("/projects/solution/{solutionId}")
    void deleteProjectsBySolutionId(@PathVariable("solutionId") Long solutionId);

}

