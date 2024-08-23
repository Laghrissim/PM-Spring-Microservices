package ma.appsegov.projectservice.client;

import ma.appsegov.projectservice.DTO.ContactDTO;
import ma.appsegov.projectservice.client.Request.UserIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", contextId = "principalRestClient")
public interface PrincipalRestClient {

    @PostMapping("/api/auth/user")
    ContactDTO findContactByUserId(@RequestBody UserIdRequest request);
}