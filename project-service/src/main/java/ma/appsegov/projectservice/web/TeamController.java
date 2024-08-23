package ma.appsegov.projectservice.web;

import lombok.RequiredArgsConstructor;
import ma.appsegov.projectservice.DTO.ProjectDTO;
import ma.appsegov.projectservice.DTO.TeamDTO;
import ma.appsegov.projectservice.model.Team;
import ma.appsegov.projectservice.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("team")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/project/{id}")
    public ResponseEntity<List<TeamDTO>> getProjectTeams(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getProjectTeams(id));
    }

    @GetMapping("add/{projectId}/{teamId}")
    public ResponseEntity<ProjectDTO> addTeamToProject(@PathVariable Long teamId, @PathVariable Long projectId) throws Exception {
        return ResponseEntity.ok(teamService.addTeamToProject(teamId, projectId));
    }

    @GetMapping("remove/{projectId}/{teamId}")
    public ResponseEntity<ProjectDTO> removeTeamFromProject(@PathVariable Long teamId, @PathVariable Long projectId) throws Exception {

        return ResponseEntity.ok(teamService.removeTeamFromProject(teamId, projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {

        return ResponseEntity.ok(teamService.createTeam(teamDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long id, @RequestBody TeamDTO teamDTO) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id) {
        try {
            teamService.deleteTeam(id);
            return ResponseEntity.ok().body("Team deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete team: " + e.getMessage());
        }
    }

}
