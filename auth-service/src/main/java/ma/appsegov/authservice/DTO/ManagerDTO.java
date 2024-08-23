package ma.appsegov.authservice.DTO;

import lombok.Data;

@Data

public class ManagerDTO {
        private Long id;
        private String username;
        private String password;
        private String email;
        private String phone;
        private String poste;

}
