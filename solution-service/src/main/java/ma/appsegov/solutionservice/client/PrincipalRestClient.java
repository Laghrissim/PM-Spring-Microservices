package ma.appsegov.solutionservice.client;

import ma.appsegov.solutionservice.DTO.ContactDTO;
import ma.appsegov.solutionservice.DTO.UserDTO;
import ma.appsegov.solutionservice.client.request.UserIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", contextId = "principalRestClient")
public interface PrincipalRestClient {

    @PostMapping("/api/auth/principal")
    UserDTO getPrincipal(@RequestBody UserIdRequest request);

    @PostMapping("/api/auth/user")
    ContactDTO findContactByUserId(@RequestBody UserIdRequest request);
}

