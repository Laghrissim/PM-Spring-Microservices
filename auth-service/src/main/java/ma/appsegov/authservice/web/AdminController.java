package ma.appsegov.authservice.web;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.DTO.*;
import ma.appsegov.authservice.client.request.MailRequest;
import ma.appsegov.authservice.client.MailRestClient;
import ma.appsegov.authservice.client.request.PasswordResetRequest;
import ma.appsegov.authservice.model.Account;
import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.model.Role;
import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.repository.AccountRepository;
import ma.appsegov.authservice.repository.RoleRepository;
import ma.appsegov.authservice.repository.UserRepository;
import ma.appsegov.authservice.security.TokenGenerator;
import ma.appsegov.authservice.service.AdminService;
import ma.appsegov.authservice.service.ContactService;
import ma.appsegov.authservice.service.UserManager;
import ma.appsegov.authservice.service.impl.AccountServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AccountServiceImpl accountService;

    private final RoleRepository roleRepository;
    private final UserManager userManager;
    private final MailRestClient mailRestClient;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final TokenGenerator tokenGenerator;
    private final UserDetailsManager userDetailsManager;
    private final ContactService contactService;
    private final UserRepository userRepository;




    private final ModelMapper modelMapper = new ModelMapper();
    private final AdminService adminService;
    private final AccountRepository accountRepository;

    @PostMapping("/admin/login")
    public ResponseEntity adminLogin(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> users = accountService.usersList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody SignupDTO signupDTO){

        User user = modelMapper.map(signupDTO, User.class);

        // Check if the user already exists before creating
        if (accountService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        userManager.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> listRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }
    @PostMapping("/roles")
    public ResponseEntity<Role> saveRole(@RequestBody RoleDTO roleDTO) {
        Role role = Role.builder().nom(roleDTO.getNom()).build();
        userManager.addNewRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PostMapping( "/addRoleToUser")
    public ResponseEntity addRoleToUser(@RequestBody RoleUserDTO roleUserDTO){

        User user = userManager.findUserById(roleUserDTO.getUserId());
        Role role = roleRepository.findRoleById(roleUserDTO.getRoleId());

        if (user == null || role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Role not found");
        }

        if (user.getRoles().contains(role)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already has the role");
        }

        user.getRoles().add(role);
        userManager.createUser(user);

        return ResponseEntity.ok("Role added to user: " + user.getUsername());
    }

    @PostMapping("/hello")
    public void hello(@RequestBody MailRequest request){
        System.out.println(request.getEmail());
        System.out.println(request.getUsername());
    }
    @PostMapping("/exists")
    public ResponseEntity sendMailIfUserExists(@RequestBody MailRequest request){
        if (accountService.existsByEmail(request.getEmail())) {
            request.setUsername(accountService.loadUserByEmail(request.getEmail()).getUsername());
            mailRestClient.sendMail(request);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        }
        return ResponseEntity.ok(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<User> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest){
         return ResponseEntity.ok(accountService.resetPassword(passwordResetRequest) );
    }

    @GetMapping("/user/email")
    public ResponseEntity<UserContactDTO> loadUserByEmail(@RequestBody EmailDTO emailDTO){

        User user = accountService.loadUserByEmail(emailDTO.getEmail());
        // Create UserContactDTO and set the contactDTO
        UserContactDTO userContactDTO = modelMapper.map(user, UserContactDTO.class);
        System.out.println("user contact => " + user.getContact());
        userContactDTO.setContactDTO(modelMapper.map(user.getContact(), ContactDTO.class));

        System.out.println("user contactDTO => " +userContactDTO.getContactDTO());

        // model mapper 
        return ResponseEntity.ok( userContactDTO );
    }
    @GetMapping("/project-managers")
    public ResponseEntity<List<User>> getProjectManagers() {
        List<User> projectManagers = adminService.getProjectManagers();
        return ResponseEntity.ok().body(projectManagers);
    }
//    @PostMapping("/register/project-manager")
//    public ResponseEntity registerProjectManager(@RequestBody ManagerDTO signupDTO) {
//        if (userDetailsManager.userExists(signupDTO.getEmail())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("User with this email already exists."));
//        }
//
//        // Create a new Account object
//        Account account = new Account();
//        account.setPhone(signupDTO.getPhone());
//
//        // Save the Account entity first
//        accountRepository.save(account);
//
//        // Create a new user object
//        User user = new User();
//        user.setUsername(signupDTO.getUsername());
//        user.setEmail(signupDTO.getEmail());
//        user.setPassword(signupDTO.getPassword());
//        user.setPoste(signupDTO.getPoste());
//
//        // Add the PROJECT_MANAGER role to the user
//        Role role = roleRepository.findRoleById(3L);
//        user.getRoles().add(role);
//
//        // Associate the Account with the User
//        user.setAccount(account);
//
//        // Creating a new user using userDetailsManager
//        userDetailsManager.createUser(user);
//
//        return ResponseEntity.ok("User Created Successfully");
//    }

    @PostMapping("/register/project-manager")
    public ResponseEntity registerOrUpdateProjectManager(@RequestBody ManagerDTO signupDTO) {
        if (signupDTO.getId() != null) {
            return updateProjectManager(signupDTO);
        } else {
            return registerProjectManager(signupDTO);
        }
    }

    private ResponseEntity registerProjectManager(ManagerDTO signupDTO) {
        if (userDetailsManager.userExists(signupDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("User with this email already exists."));
        }

        User user = convertDTOToUser(signupDTO);
        userDetailsManager.createUser(user);

        return ResponseEntity.ok("User Created Successfully");
    }

    private ResponseEntity updateProjectManager(ManagerDTO signupDTO) {
        User existingUser = accountService.getUser(signupDTO.getId());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User not found with ID: " + signupDTO.getId()));
        }

        // Update the fields of the existingUser with data from signupDTO if they are not null
        if (signupDTO.getUsername() != null) {
            existingUser.setUsername(signupDTO.getUsername());
        }
        if (signupDTO.getEmail() != null) {
            existingUser.setEmail(signupDTO.getEmail());
        }
        if (signupDTO.getPassword() != null) {
            existingUser.setPassword(signupDTO.getPassword()); // Note: consider if you want to allow updating password here
        }
        if (signupDTO.getPoste() != null) {
            existingUser.setPoste(signupDTO.getPoste());
        }
        // Update other fields as needed

        // Save the updated user using the user repository
        userRepository.save(existingUser);

        return ResponseEntity.ok("User Updated Successfully");
    }



    private User convertDTOToUser(ManagerDTO signupDTO) {
        User user = new User();
        user.setId(signupDTO.getId());
        user.setUsername(signupDTO.getUsername());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(signupDTO.getPassword());
        user.setPoste(signupDTO.getPoste());

        Account account = new Account();
        account.setPhone(signupDTO.getPhone());
        accountRepository.save(account);
        user.setAccount(account);

        Role role = roleRepository.findRoleById(3L);
        user.getRoles().add(role);

        return user;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            accountService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @GetMapping("/count")
    public long getUserCount() {
        return accountService.getUserCount();
    }

}
