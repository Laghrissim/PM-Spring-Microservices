package ma.appsegov.authservice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AuthenticatedUserDTO {

    private String username;

    private String email;

    private String isCompany;

    private String poste;

    private List<RoleDTO> roles;

    private AccountDTO account;

}
