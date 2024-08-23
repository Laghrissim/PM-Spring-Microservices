package ma.appsegov.authservice.service;

import ma.appsegov.authservice.DTO.SolutionDTO;

import java.util.List;

public interface SolutionService {
    List<SolutionDTO> getAllSolutions();

    SolutionDTO getSolutionById(Long id);

    SolutionDTO createSolution(SolutionDTO solutionDTO);

    SolutionDTO updateSolution(Long id, SolutionDTO solutionDTO);

    void deactivateSolution(Long id);

    void deleteSolution(Long id);
}
