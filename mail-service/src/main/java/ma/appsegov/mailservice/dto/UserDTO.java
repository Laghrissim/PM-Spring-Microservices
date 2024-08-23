package ma.appsegov.mailservice.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserDTO {

    private String username;

    private String email;

    public UserDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
