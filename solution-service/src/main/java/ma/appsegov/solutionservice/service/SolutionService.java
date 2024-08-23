package ma.appsegov.solutionservice.service;



import ma.appsegov.solutionservice.DTO.SolutionDTO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface SolutionService {
    List<SolutionDTO> getAllSolutions(String token) throws NoSuchAlgorithmException, InvalidKeySpecException;

    List<SolutionDTO> getAllSolutions();
    SolutionDTO getSolutionById(Long id);

    SolutionDTO createSolution(SolutionDTO solutionDTO);

    SolutionDTO updateSolution(SolutionDTO solutionDTO);

    void deactivateSolution(Long id);

    void deleteSolution(Long id);

    SolutionDTO getSolutionForUser(Long id,String token) throws NoSuchAlgorithmException, InvalidKeySpecException;

    long getUserCount();
}
