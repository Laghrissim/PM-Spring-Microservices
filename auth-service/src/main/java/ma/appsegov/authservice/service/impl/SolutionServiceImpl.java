package ma.appsegov.authservice.service.impl;

import lombok.AllArgsConstructor;
import ma.appsegov.authservice.DTO.SolutionDTO;
import ma.appsegov.authservice.exception.SolutionNotFoundException;
import ma.appsegov.authservice.model.Solution;
import ma.appsegov.authservice.repository.SolutionRepository;
import ma.appsegov.authservice.service.SolutionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SolutionServiceImpl implements SolutionService {
    private final SolutionRepository solutionRepository;

    @Override
    public List<SolutionDTO> getAllSolutions() {
        List<Solution> solutions = solutionRepository.findAll();
        return solutions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SolutionDTO getSolutionById(Long id) {
        Optional<Solution> optionalSolution = solutionRepository.findById(id);
        if (optionalSolution.isPresent()) {
            return convertToDTO(optionalSolution.get());
        } else {
            throw new SolutionNotFoundException("Solution with id " + id + " not found");
        }
    }

    @Override
    public SolutionDTO createSolution(SolutionDTO solutionDTO) {
        Solution solution = convertToEntity(solutionDTO);
        Solution savedSolution = solutionRepository.save(solution);
        return convertToDTO(savedSolution);
    }

    @Override
    public SolutionDTO updateSolution(Long id, SolutionDTO solutionDTO) {
        Solution existingSolution = solutionRepository.findById(id)
                .orElseThrow(() -> new SolutionNotFoundException("Solution with id " + id + " not found"));

        BeanUtils.copyProperties(solutionDTO, existingSolution, "id");
        Solution updatedSolution = solutionRepository.save(existingSolution);
        return convertToDTO(updatedSolution);
    }

    @Override
    public void deactivateSolution(Long id) {
        Solution existingSolution = solutionRepository.findById(id)
                .orElseThrow(() -> new SolutionNotFoundException("Solution with id " + id + " not found"));

        existingSolution.setActive(false);
        solutionRepository.save(existingSolution);
    }

    @Override
    public void deleteSolution(Long id) {
        solutionRepository.deleteById(id);
    }

    private SolutionDTO convertToDTO(Solution solution) {
        SolutionDTO dto = new SolutionDTO();
        BeanUtils.copyProperties(solution, dto);
        return dto;
    }

    private Solution convertToEntity(SolutionDTO dto) {
        Solution solution = new Solution();
        BeanUtils.copyProperties(dto, solution);
        return solution;
    }
}
