package ma.appsegov.authservice.client.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PasswordResetRequest {
    private String email;
    private String password;
}
