package ma.appsegov.mailservice.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class MailRequest {

    private String email;
    private String username;
}
