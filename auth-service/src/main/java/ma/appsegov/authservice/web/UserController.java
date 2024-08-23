package ma.appsegov.authservice.web;

import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.DTO.UserDTO;
import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.service.impl.AccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
//@RolesAllowed({"ADMIN", "USER","PMO"})
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AccountServiceImpl accountService;

    @GetMapping("/{id}")
//    @PreAuthorize("#user.id == #id")
    public ResponseEntity user(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(accountService.findUserById(id));
    }
}