package ma.appsegov.authservice.DTO;

import lombok.Data;

@Data
public class RoleUserDTO {
    private Long userId;
    private Long roleId;
}
