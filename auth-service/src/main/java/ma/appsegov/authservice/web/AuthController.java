package ma.appsegov.authservice.web;

import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.DTO.*;
import ma.appsegov.authservice.client.request.UserIdRequest;
import ma.appsegov.authservice.model.Account;
import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.model.Role;
import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.repository.ContactRepository;
import ma.appsegov.authservice.security.KeyUtils;
import ma.appsegov.authservice.security.TokenGenerator;
import ma.appsegov.authservice.service.impl.AccountServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeyUtils keyUtils;

    private final AccountServiceImpl accountService;

    private final ModelMapper modelMapper = new ModelMapper();

    private final UserDetailsManager userDetailsManager;

    private final ContactRepository contactRepository;

    private final TokenGenerator tokenGenerator;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Qualifier("jwtAuthenticationProvider")
    private final JwtAuthenticationProvider refreshTokenAuthenticationProvider;


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignupDTO signupDTO) {
        if (userDetailsManager.userExists(signupDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("User with this email already exists."));
        }

        User user = new User();
        user.setUsername(signupDTO.getUsername());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(signupDTO.getPassword());

        // Fetch the existing contact entity from the database using the provided contactId
        Contact contact = contactRepository.findById(signupDTO.getContactId()).orElse(null);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Contact with the provided ID not found."));
        }

        // Associate the fetched contact with the new user
        user.setContact(contact);

        // Creating a new user using userDetailsManager
        userDetailsManager.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), Collections.EMPTY_LIST);
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication);
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthenticationProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @GetMapping("/access-token/publicKey")
    public String getAccessTokenPublicKey() {
        RSAPublicKey publicKey = keyUtils.getAccessTokenPublicKey();
        return formatPublicKeyAsPEM(publicKey);
    }

    private String formatPublicKeyAsPEM(RSAPublicKey publicKey) {
        String keyType = "RSA";
        byte[] publicKeyBytes = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(publicKeyBytes);
        String pemPublicKeyBuilder = "-----BEGIN " + keyType + " PUBLIC KEY-----\n" +
                base64PublicKey.replaceAll("(.{64})", "$1\n") +
                "\n-----END " + keyType + " PUBLIC KEY-----\n";
        return pemPublicKeyBuilder;
    }

    @GetMapping("/refresh-token/privatekey")
    public ResponseEntity<RSAPublicKey> getRefreshTokenPublicKey() {
        return ResponseEntity.ok(keyUtils.getRefreshTokenPublicKey());
    }

    @PostMapping("/principal")
    public ResponseEntity<AuthenticatedUserDTO> getPrincipal(@RequestBody UserIdRequest request) {
        return ResponseEntity.ok(accountService.getUserById(request.getId()));
    }

    @PostMapping("/user")
    public ResponseEntity<ContactDTO> findContactByUserId(@RequestBody UserIdRequest request) {
        Contact contact = accountService.findContactByUserId(request.getId()) ;
        System.out.println(contact);
        return ResponseEntity.ok( modelMapper.map( contact , ContactDTO.class) );
    }





}
