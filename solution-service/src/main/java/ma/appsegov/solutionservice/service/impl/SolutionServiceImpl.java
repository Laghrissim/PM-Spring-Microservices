package ma.appsegov.solutionservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import ma.appsegov.solutionservice.DTO.SolutionDTO;
import ma.appsegov.solutionservice.client.PrincipalRestClient;
import ma.appsegov.solutionservice.client.ProjectRestClient;
import ma.appsegov.solutionservice.client.request.UserIdRequest;
import ma.appsegov.solutionservice.exception.SolutionNotFoundException;
import ma.appsegov.solutionservice.model.Request;
import ma.appsegov.solutionservice.model.Solution;
import ma.appsegov.solutionservice.repository.SolutionRepository;
import ma.appsegov.solutionservice.service.SolutionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;
    private final JwtService jwtService;
    private final PrincipalRestClient principalRestClient;
    private final ProjectRestClient projectRestClient;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<SolutionDTO> getAllSolutions(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Long userid = jwtService.extractSubject(token);
        UserIdRequest userIdRequest = new UserIdRequest(userid);
        Long contactId= principalRestClient.findContactByUserId(userIdRequest).getId();
        List<Solution> solutions = solutionRepository.findAll();

        // Iterate through each solution
        for (Solution solution : solutions) {
            Long projectId =projectRestClient.getProjectIdBySolutionIdAndClientId(solution.getId(), userid).getBody();
            boolean isClient = false; // Flag to track if the user is a client for this solution

            // Iterate through each request associated with the solution
            for (Request request : solution.getRequests()) {
                // Check if the userID in the request matches the userId
                if (request.getUser_id().equals(userid)) {
                    // If userId matches, set the status of the solution to "client"
                    solution.setStatus("client");
                    isClient = true;
                    break; // No need to continue checking other requests for this solution
                }
            }

            // If the user is not a client and contactId exists in the list of contact IDs for the solution, set the status to "beneficiary"
            if (!isClient && solution.getContactIds().contains(contactId)) {
                if(projectRestClient.isUserSubscribed(projectId,userid)){
                    solution.setStatus("client");
                }
                else {
                    solution.setStatus("beneficiary");
                }
            }

            // Save the updated solution back to the repository
        }

        // Map and return the list of SolutionDTOs
        return solutions.stream()
                .map(solution -> modelMapper.map(solution, SolutionDTO.class))
                .toList();    }

    @Override
    public List<SolutionDTO> getAllSolutions() {
        List<Solution> solutions = solutionRepository.findAll();
        List<SolutionDTO> solutionDTOs = new ArrayList<>();
        for (Solution solution : solutions) {
            SolutionDTO solutionDTO = new SolutionDTO();
            solutionDTO.setId(solution.getId());
            solutionDTO.setName(solution.getName());
            solutionDTO.setWebsite(solution.getWebsite());
            solutionDTO.setPicture(solution.getPicture());
            solutionDTO.setDescription(solution.getDescription());
            solutionDTO.setVideo(solution.getVideo());
            solutionDTO.setStatus(solution.getStatus());
            solutionDTO.setActive(solution.isActive());
            solutionDTO.setRequestCount(solution.getRequests().size()); // Calculate request count
            solutionDTOs.add(solutionDTO);
        }
        return solutionDTOs;
    }

    @Override
    public SolutionDTO getSolutionById(Long id) {
        Optional<Solution> optionalSolution = solutionRepository.findById(id);
        if (optionalSolution.isPresent()) {
            return modelMapper.map(optionalSolution, SolutionDTO.class);
        } else {
            throw new SolutionNotFoundException("Solution with id " + id + " not found");
        }
    }

    @Override
    public SolutionDTO createSolution(SolutionDTO solutionDTO) {
        Solution solution = modelMapper.map(solutionDTO, Solution.class);
        solutionRepository.save(solution);
        return modelMapper.map(solution, SolutionDTO.class);
    }

    @Override
    public SolutionDTO updateSolution(SolutionDTO solutionDTO) {
        // Check if the solution exists
        Solution existingSolution = solutionRepository.findById(solutionDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Solution not found with id: " + solutionDTO.getId()));

        // Update the existing solution entity
        modelMapper.map(solutionDTO, existingSolution);
        solutionRepository.save(existingSolution);

        return modelMapper.map(existingSolution, SolutionDTO.class);
    }


//    @Override
//    public SolutionDTO updateSolution(Long id, SolutionDTO solutionDTO) {
//        Solution existingSolution = solutionRepository.findById(id).orElseThrow(() -> new SolutionNotFoundException("Solution with id " + id + " not found"));
//
//        BeanUtils.copyProperties(solutionDTO, existingSolution, "id");
//        solutionRepository.save(existingSolution);
//        return modelMapper.map(existingSolution, SolutionDTO.class);
//    }

    @Override
    public void deactivateSolution(Long id) {
        Solution existingSolution = solutionRepository.findById(id).orElseThrow(() -> new SolutionNotFoundException("Solution with id " + id + " not found"));

        existingSolution.setActive(false);
        solutionRepository.save(existingSolution);
    }

    @Override
    public void deleteSolution(Long id) {
        Solution solution = solutionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solution with id " + id + " not found"));

        projectRestClient.deleteProjectsBySolutionId(id);

        solutionRepository.deleteById(id);
    }

    @Override
    public SolutionDTO getSolutionForUser(Long id,String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Long userid = jwtService.extractSubject(token);
        UserIdRequest userIdRequest = new UserIdRequest(userid);
        Long contactId= principalRestClient.findContactByUserId(userIdRequest).getId();
        Solution solution = solutionRepository.findById(id).get();

        Long projectId =projectRestClient.getProjectIdBySolutionIdAndClientId(solution.getId(), userid).getBody();
        boolean isClient = false; // Flag to track if the user is a client for this solution

        // Iterate through each request associated with the solution
        for (Request request : solution.getRequests()) {
            // Check if the userID in the request matches the userId
            if (request.getUser_id().equals(userid)) {
                // If userId matches, set the status of the solution to "client"
                solution.setStatus("client");
                isClient = true;
                break; // No need to continue checking other requests for this solution
            }
        }

        // If the user is not a client and contactId exists in the list of contact IDs for the solution, set the status to "beneficiary"
        if (!isClient && solution.getContactIds().contains(contactId)) {
            if(projectRestClient.isUserSubscribed(projectId,userid)){
                solution.setStatus("client");
            }
            else {
                solution.setStatus("beneficiary");
            }
        }

        return modelMapper.map(solution, SolutionDTO.class);
    }

    @Override
    public long getUserCount() {

        return solutionRepository.count();
    }

    private SolutionDTO convertToDTO(Solution solution) {
        SolutionDTO dto = new SolutionDTO();
        BeanUtils.copyProperties(solution, dto);
        return dto;
    }

}
