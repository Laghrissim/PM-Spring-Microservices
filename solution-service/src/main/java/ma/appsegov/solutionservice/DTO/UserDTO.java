package ma.appsegov.solutionservice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserDTO {

    private String username;

    private String email;

    private boolean isCompany;

    private String poste;

    private List<RoleDTO> roles;

    private AccountDTO account;
    private ContactDTO contact;

    public boolean getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }
}
