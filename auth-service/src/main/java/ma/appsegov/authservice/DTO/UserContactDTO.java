package ma.appsegov.authservice.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.model.User;

@Data
@RequiredArgsConstructor
public class UserContactDTO {
    private  String id;
    private String username;
    private String email;

    private ContactDTO contactDTO;

    }
