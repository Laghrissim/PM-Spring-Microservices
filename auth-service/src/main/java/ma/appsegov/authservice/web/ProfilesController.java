package ma.appsegov.authservice.web;

import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor

public class ProfilesController {

    private final ImageService imageService;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getProfile(@PathVariable String fileName) throws IOException {
        Resource resource = imageService.getProfile(fileName);

        // Determine the content type dynamically based on the file/resource
        MediaType mediaType = MediaType.IMAGE_JPEG; // Set a default media type, change accordingly

        try {
            mediaType = MediaType.parseMediaType(imageService.getContentType(fileName));
        } catch (IOException e) {
            // Handle exception if content type cannot be determined
        }

        return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
    }

    @PostMapping("/")
    public ResponseEntity<String> updateProfile(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String imagePath = imageService.saveImage(file); // Save the image and get the generated file path
//                    if (existingCoach.getPicturePath() != null) {
//                        imageService.deleteProfile(existingCoach.getPicturePath()); // Delete the old profile picture
//                    }
                // Set the image path in the DTO instead of the byte array
            } catch (IOException e) {
                // Handle file processing error
                return new ResponseEntity<>("Failed to process the image", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        return new ResponseEntity<>("image set successfuly", HttpStatus.OK);

    }

}
