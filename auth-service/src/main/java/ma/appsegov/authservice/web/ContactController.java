package ma.appsegov.authservice.web;

import lombok.AllArgsConstructor;
import ma.appsegov.authservice.DTO.ContactDTO;
import ma.appsegov.authservice.DTO.UserResponseDTO;
import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.service.ContactService;
import ma.appsegov.authservice.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@AllArgsConstructor
public class ContactController {


    private final ContactService contactService;
    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<String> createContact(@RequestBody UserResponseDTO userResponseDTO) {
        try {
            // Call the service method to create contact for the current user
            boolean contactCreated = contactService.createContactForCurrentUser(userResponseDTO);
            if (contactCreated) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Contact created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contact not created successfully");
            }
        } catch (Exception e) {
            // Handle any exceptions and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create contact");
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllContacts() {
        try {
            List<Contact> contacts = contactService.getAllContacts();
            if (contacts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No contacts found");
            } else {
                return ResponseEntity.ok(contacts);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve contacts");
        }

    }
    @PostMapping("/createOrUpdate")
    public ResponseEntity<ContactDTO> createOrUpdateContact(
            @RequestPart("contactDTO") ContactDTO contactDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String imagePath = imageService.saveImage(file);
                contactDTO.setPicture(imagePath);
            }

            if (contactDTO.getId() != null) {
                // Update existing contact
                ContactDTO updatedContact = contactService.updateContact(contactDTO);
                return ResponseEntity.ok(updatedContact);
            } else {
                // Create a new contact
                ContactDTO createdContact = contactService.createContact(contactDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count")
    public long getUserCount() {
        return contactService.getUserCount();
    }


}