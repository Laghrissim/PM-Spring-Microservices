package ma.appsegov.solutionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", contextId = "tokenKeyRestClient")
public interface TokenKeyRestClient {

    @GetMapping("/api/auth/access-token/publicKey")
    String getAccessTokenPublicKey();

    @GetMapping("/api/auth/refresh-token/publicKey")
    String getRefreshTokenPublicKey();
}
