package ma.appsegov.authservice.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.DTO.AuthenticatedUserDTO;
import ma.appsegov.authservice.DTO.UserDTO;
import ma.appsegov.authservice.client.request.PasswordResetRequest;
import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.model.Role;
import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.repository.AccountRepository;
import ma.appsegov.authservice.repository.RoleRepository;
import ma.appsegov.authservice.repository.UserRepository;
import ma.appsegov.authservice.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public User save(User utilisateur) {

        return userRepository.save(utilisateur);
    }
    @Override
    public User addUser(User utilisateur) {

        accountRepository.save(utilisateur.getAccount());
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));

        return userRepository.save(utilisateur);
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        User user = loadUserByUsername(username);
        user.getRoles().add(roleRepository.findByNom(roleName));

        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public User resetPassword(PasswordResetRequest passwordResetRequest) {
        User user = loadUserByEmail(passwordResetRequest.getEmail());
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        return save(user);
    }

    @Override
    public List<UserDTO> usersList() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO) // Map User entities to UserDTO
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .email(user.getEmail())
                .poste(user.getPoste())
                .is_company(user.getIs_company())
                .account(user.getAccount())
                .roles(user.getRoles())
                .contact_id(user.getContact() != null ? user.getContact().getId() : null)
                .build();
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with this email: " + email);
        }
        /* emaiUtil.sendPasswordEmail(email);*/
        return "please check your email to send new password";

    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO findUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return null;
        }
        return convertToDTO(user);
    }
    @Override
    public User getUser(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return null;
        }
        return user;
    }


    @Override
    public AuthenticatedUserDTO getUserById(Long id) {
        return modelMapper.map(userRepository.findUserById(id), AuthenticatedUserDTO.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        user.getRoles().clear();
        userRepository.delete(user);
    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }

    public Contact findContactByUserId(Long id) {
        return userRepository.findContactByUserId(id);
    }
}
