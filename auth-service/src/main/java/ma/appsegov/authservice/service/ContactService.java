package ma.appsegov.authservice.service;

import ma.appsegov.authservice.DTO.ContactDTO;
import ma.appsegov.authservice.DTO.UserResponseDTO;
import ma.appsegov.authservice.model.Contact;

import java.util.List;

public interface ContactService {
    public boolean createContactForCurrentUser(UserResponseDTO contact);

    List<Contact> getAllContacts();

    ContactDTO updateContact(ContactDTO contactDTO);

    ContactDTO createContact(ContactDTO contactDTO);

    void deleteContact(Long id);

    long getUserCount();
}
