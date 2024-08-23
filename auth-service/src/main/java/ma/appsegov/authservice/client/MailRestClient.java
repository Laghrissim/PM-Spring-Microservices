package ma.appsegov.authservice.client;

import ma.appsegov.authservice.client.request.MailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-service")
public interface MailRestClient {

    @PostMapping("/password/reset")
    void sendMail(@RequestBody MailRequest request);
}
