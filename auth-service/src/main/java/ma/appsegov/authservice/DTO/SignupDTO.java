package ma.appsegov.authservice.DTO;

import lombok.Data;

@Data
public class SignupDTO {
    private String username;
    private String password;
    private String email;
    private Long contactId;

}
