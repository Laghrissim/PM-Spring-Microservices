package ma.appsegov.authservice.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordResetDTO {
    private String password;
}
