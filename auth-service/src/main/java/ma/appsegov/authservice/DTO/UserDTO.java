package ma.appsegov.authservice.DTO;

import ma.appsegov.authservice.model.Account;
import ma.appsegov.authservice.model.Role;
import ma.appsegov.authservice.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class UserDTO {
    private  String id;
    private String username;
    private String email;
    private String poste;
    private String is_company;
    private Long contact_id;
    private Account account;
    private List<Role> roles = new ArrayList<>();


    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }

        UserDTOBuilder builder = UserDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .email(user.getEmail());



        return builder.build();
    }
}
