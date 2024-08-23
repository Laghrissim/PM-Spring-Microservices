package ma.appsegov.authservice.service;


import ma.appsegov.authservice.DTO.AuthenticatedUserDTO;
import ma.appsegov.authservice.DTO.UserDTO;
import ma.appsegov.authservice.client.request.PasswordResetRequest;
import ma.appsegov.authservice.model.Role;
import ma.appsegov.authservice.model.User;

import javax.mail.MessagingException;
import java.util.List;

public  interface AccountService {

    User save(User utilisateur);

    User addUser(User utilisateur);
    Role addRole(Role role);

    void addRoleToUser( String username, String roleName);

    User loadUserByUsername( String username);

    User loadUserByEmail( String email);

    User resetPassword(PasswordResetRequest passwordResetRequest);

    List<UserDTO> usersList();

    String forgotPassword(String email) throws MessagingException;

    boolean existsByEmail(String email);

    UserDTO findUserById(Long id);

    User getUser(Long id);

    AuthenticatedUserDTO getUserById(Long id);

    void deleteUser(Long userId);

    long getUserCount();
}
