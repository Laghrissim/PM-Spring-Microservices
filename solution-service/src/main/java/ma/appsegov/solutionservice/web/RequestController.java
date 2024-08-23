package ma.appsegov.solutionservice.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import ma.appsegov.solutionservice.DTO.RequestDTO;
import ma.appsegov.solutionservice.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        List<RequestDTO> solutions = requestService.getAllRequests();
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable Long id) {
        RequestDTO solutionDTO = requestService.getRequestById(id);
        return ResponseEntity.ok(solutionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestDTO> updateRequest(@PathVariable Long id, @RequestBody RequestDTO solutionDTO) {
        RequestDTO updatedRequest = requestService.updateRequest(id, solutionDTO);
        return ResponseEntity.ok(updatedRequest);
    }

    @PostMapping("/souscrire")
    public ResponseEntity<?> souscrire(@RequestBody RequestDTO requestDTO, HttpServletRequest request) throws Exception {

        final String authHeader = request.getHeader("Authorization");
        System.out.println("authorization header :" + authHeader);
        String token;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotFoundException("You need to log in !");
        }
        token = authHeader.substring(7);
        return ResponseEntity.ok(requestService.souscrire(requestDTO, token));

    }

    @PostMapping("/request/{id}/valider")
    public ResponseEntity<?> valider(@PathVariable("id") Long id) {
        requestService.valider(id);

        return ResponseEntity.ok().body(HttpStatus.ACCEPTED);
    }

}
