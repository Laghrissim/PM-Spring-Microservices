package ma.appsegov.authservice.web;


import lombok.AllArgsConstructor;
import ma.appsegov.authservice.DTO.SolutionDTO;
import ma.appsegov.authservice.service.SolutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solutions")
@AllArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping
    public ResponseEntity<List<SolutionDTO>> getAllSolutions() {
        List<SolutionDTO> solutions = solutionService.getAllSolutions();
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolutionDTO> getSolutionById(@PathVariable Long id) {
        SolutionDTO solutionDTO = solutionService.getSolutionById(id);
        return ResponseEntity.ok(solutionDTO);
    }

    @PostMapping
    public ResponseEntity<SolutionDTO> createSolution(@RequestBody SolutionDTO solutionDTO) {
        SolutionDTO createdSolution = solutionService.createSolution(solutionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSolution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolutionDTO> updateSolution(@PathVariable Long id, @RequestBody SolutionDTO solutionDTO) {
        SolutionDTO updatedSolution = solutionService.updateSolution(id, solutionDTO);
        return ResponseEntity.ok(updatedSolution);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateSolution(@PathVariable Long id) {
        solutionService.deactivateSolution(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolution(@PathVariable Long id) {
        solutionService.deleteSolution(id);
        return ResponseEntity.noContent().build();
    }
}
