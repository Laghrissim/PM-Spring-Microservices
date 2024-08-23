package ma.appsegov.solutionservice.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import ma.appsegov.solutionservice.DTO.RequestDTO;
import ma.appsegov.solutionservice.DTO.SolutionDTO;
import ma.appsegov.solutionservice.service.ImageService;
import ma.appsegov.solutionservice.service.RequestService;
import ma.appsegov.solutionservice.service.SolutionService;
import ma.appsegov.solutionservice.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@RequestMapping("/api/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;
    private final RequestService requestService;
    private final JwtService jwtService;
    private final ImageService imageService;



    @GetMapping
    public ResponseEntity<List<SolutionDTO>> getAllSolutions(HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("authorization header :" + authHeader);
        String token;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotFoundException("You need to log in !");
        }
        token = authHeader.substring(7);
        List<SolutionDTO> solutions = solutionService.getAllSolutions(token);
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("all")
    public ResponseEntity<List<SolutionDTO>> getAllSolutions() {
        List<SolutionDTO> solutions = solutionService.getAllSolutions();
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolutionDTO> getSolutionById(@PathVariable Long id) {
        SolutionDTO solutionDTO = solutionService.getSolutionById(id);
        return ResponseEntity.ok(solutionDTO);
    }

    @GetMapping("/forUser/{id}")
    public ResponseEntity<SolutionDTO> getSolutionForUser(@PathVariable Long id,HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("authorization header :" + authHeader);
        String token;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotFoundException("You need to log in !");
        }
        token = authHeader.substring(7);
        SolutionDTO solutionDTO = solutionService.getSolutionForUser(id,token);
        return ResponseEntity.ok(solutionDTO);
    }

    @PostMapping
    public ResponseEntity<SolutionDTO> createOrUpdateSolution(
            @RequestPart("solutionDTO") SolutionDTO solutionDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String imagePath = imageService.saveImage(file);
                solutionDTO.setPicture(imagePath);
            }

            if (solutionDTO.getId() != null) {
                SolutionDTO updatedSolution = solutionService.updateSolution(solutionDTO);
                return ResponseEntity.ok(updatedSolution);
            } else {
                // Create a new solution
                SolutionDTO createdSolution = solutionService.createSolution(solutionDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSolution);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/getImage/{fileName:.+}")
    public ResponseEntity<Resource> getProfile(@PathVariable String fileName) throws IOException {
        Resource resource = imageService.getProfile(fileName);

        MediaType mediaType = MediaType.IMAGE_JPEG;

        try {
            mediaType = MediaType.parseMediaType(imageService.getContentType(fileName));
        } catch (IOException e) {
        }

        return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
    }



    @PutMapping("/{id}")
    public ResponseEntity<SolutionDTO> updateSolution(@PathVariable Long id, @RequestBody SolutionDTO solutionDTO) {
        SolutionDTO updatedSolution = solutionService.updateSolution(solutionDTO);
        return ResponseEntity.ok(updatedSolution);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateSolution(@PathVariable Long id) {
        solutionService.deactivateSolution(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolution(@PathVariable Long id) {
        solutionService.deleteSolution(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getHeader")
    public ResponseEntity<String> getHeader(HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("authorization header :" + authHeader);
        String token;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotFoundException("You need to log in !");
        }
        token = authHeader.substring(7);

        if (jwtService.isTokenValid(token)) {
            jwtService.setUserPrincipal(token);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.ok(null);
    }
    @GetMapping("/count")
    public long getUserCount() {
        return solutionService.getUserCount();
    }
}
