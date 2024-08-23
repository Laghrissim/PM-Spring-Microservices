package ma.appsegov.solutionservice.service;

import ma.appsegov.solutionservice.DTO.RequestDTO;

import java.util.List;

public interface RequestService {
    RequestDTO souscrire(RequestDTO requestDTO, String token) throws Exception;

    void valider(Long id);

    List<RequestDTO> getAllRequests();

    RequestDTO getRequestById(Long id);

    RequestDTO updateRequest(Long id, RequestDTO requestDTO);

    void deleteRequest(Long id);
}
