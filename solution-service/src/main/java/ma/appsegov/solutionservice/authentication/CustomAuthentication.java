package ma.appsegov.solutionservice.authentication;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.appsegov.solutionservice.DTO.AccountDTO;
import ma.appsegov.solutionservice.DTO.RoleDTO;
import ma.appsegov.solutionservice.DTO.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomAuthentication implements Authentication {

    private final UserDTO userDTO; // Assuming UserDTO is your custom user details class

    public CustomAuthentication(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<RoleDTO> getRoles() {
        return userDTO.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDTO.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNom()))
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null; // You might need to implement this method if needed
    }

    @Override
    public Object getDetails() {
        return userDTO; // Return the user details object
    }

    @Override
    public Object getPrincipal() {
        return userDTO.getEmail(); // Return principal information, usually username
    }

    @Override
    public boolean isAuthenticated() {
        return true; // You can set this based on your authentication logic
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // You might need to implement this method if needed
    }

    @Override
    public String getName() {
        return userDTO.getUsername(); // Return the username
    }

    public boolean isCompany() {
        return userDTO.getIsCompany();
    }

    public AccountDTO getAccount() {
        return userDTO.getAccount();
    }
}

