package ma.appsegov.authservice.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
