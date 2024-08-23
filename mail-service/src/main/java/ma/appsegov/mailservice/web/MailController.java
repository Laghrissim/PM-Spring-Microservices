package ma.appsegov.mailservice.web;

import lombok.RequiredArgsConstructor;
import ma.appsegov.mailservice.client.MailRequest;
import ma.appsegov.mailservice.dto.UserDTO;
import ma.appsegov.mailservice.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/password/reset")
    public ResponseEntity<UserDTO> sendMail(@RequestBody MailRequest mailRequest) throws MessagingException {

        UserDTO userDTO = new UserDTO(mailRequest.getEmail(), mailRequest.getUsername());
        return ResponseEntity.ok().body( mailService.sendPasswordResetEmail(userDTO) );

    }
}
