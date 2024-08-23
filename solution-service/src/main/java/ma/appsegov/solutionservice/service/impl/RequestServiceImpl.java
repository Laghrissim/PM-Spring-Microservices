package ma.appsegov.solutionservice.service.impl;


import lombok.RequiredArgsConstructor;
import ma.appsegov.solutionservice.DTO.ContactDTO;
import ma.appsegov.solutionservice.DTO.RequestDTO;
import ma.appsegov.solutionservice.client.PrincipalRestClient;
import ma.appsegov.solutionservice.client.ProjectRestClient;
import ma.appsegov.solutionservice.client.request.ProjectCreationRequest;
import ma.appsegov.solutionservice.client.request.UserIdRequest;
import ma.appsegov.solutionservice.exception.RequestNotFoundException;
import ma.appsegov.solutionservice.model.Request;
import ma.appsegov.solutionservice.model.Solution;
import ma.appsegov.solutionservice.model.enums.EtatRequete;
import ma.appsegov.solutionservice.repository.RequestRepository;
import ma.appsegov.solutionservice.repository.SolutionRepository;
import ma.appsegov.solutionservice.service.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final SolutionRepository solutionRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PrincipalRestClient principalRestClient;
    private final ProjectRestClient projectRestClient;
    private final JwtService jwtService;


    @Override
    public RequestDTO souscrire(RequestDTO requestDTO, String token) throws Exception {

        Solution solution = solutionRepository.findDistinctById(requestDTO.getSolution_id());
        if (solution == null) {
            throw new Exception("Solution not found");
        }

        Long result = requestRepository.findByContact_idAndSolutionId(requestDTO.getContact_id(), requestDTO.getSolution_id());

        if (result > 0) {
            throw new Exception("Vous avez déjà bénéficié du service {0}" + solution.getName());
        }

        Long userid = jwtService.extractSubject(token);

        Request request = modelMapper.map(requestDTO, Request.class);
        UserIdRequest userIdRequest = new UserIdRequest(userid);
        request.setUser_id(userid);
        request.setContact_id(principalRestClient.findContactByUserId(userIdRequest).getId());
        request.setSolution(solution);
        request.setDate_creation(LocalDate.now());
        request.setService(solution.getName());
        request.setDescription(solution.getDescription());
        request.setEtat(EtatRequete.NON_VALIDE);

        Request savedRequest = requestRepository.save(request);
        this.valider(savedRequest.getId());
        solution.getRequests().add(request);
        solution.getContactIds().add(principalRestClient.findContactByUserId(userIdRequest).getId());
        solutionRepository.save(solution);
        requestDTO = modelMapper.map(request, RequestDTO.class);
        return requestDTO;
    }

    @Override
    public void valider(Long id) {
        // Retrieve the existing Request entity from the database
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();

            UserIdRequest userIdRequest = new UserIdRequest(request.getUser_id());
            ContactDTO contact= principalRestClient.findContactByUserId(userIdRequest);

            request.setEtat(EtatRequete.VALIDE);

            // Retrieve the associated Solution entity if solution_id is not null
            Long solutionId = request.getSolution().getId(); // Assuming solution_id is stored in the Request entity itself
            if (solutionId != null) {
                Solution solution = solutionRepository.findById(solutionId).orElse(null);
                if (solution != null) {
                    ProjectCreationRequest projectCreationRequest = ProjectCreationRequest.builder()
                            .contact_id(request.getContact_id())
                            .solution_id(solutionId)
                            .name(solution.getName())
                            .client_id(request.getUser_id())
                            .build();

                    projectCreationRequest.setDescriptionFromContactAndSolution(contact.getName(), solution.getName());
                    projectRestClient.createProject(projectCreationRequest);

                }
            }

            // Save the updated Request entity back to the database
            requestRepository.save(request);
        } else {
            throw new RequestNotFoundException("Request with id " + id + " not found");
        }
    }


    @Override
    public List<RequestDTO> getAllRequests() {
        return requestRepository.findAll().stream().map(solution -> modelMapper.map(solution, RequestDTO.class)).toList();
    }

    @Override
    public RequestDTO getRequestById(Long id) {
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            RequestDTO requestDTO = modelMapper.map(request, RequestDTO.class);
            if (request.getSolution() != null) {
                requestDTO.setSolution_id(request.getSolution().getId());
            }
            return requestDTO;
        } else {
            throw new RequestNotFoundException("Request with id " + id + " not found");
        }
    }

    @Override
    public RequestDTO updateRequest(Long id, RequestDTO requestDTO) {
        Request existingRequest = requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException("Request with id " + id + " not found"));

        BeanUtils.copyProperties(requestDTO, existingRequest, "id");
        requestRepository.save(existingRequest);
        return modelMapper.map(existingRequest, RequestDTO.class);
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }

}
