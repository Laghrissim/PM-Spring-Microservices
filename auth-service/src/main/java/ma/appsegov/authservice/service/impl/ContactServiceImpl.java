package ma.appsegov.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.DTO.ContactDTO;
import ma.appsegov.authservice.DTO.UserResponseDTO;
import ma.appsegov.authservice.model.Contact;
import ma.appsegov.authservice.repository.ContactRepository;
import ma.appsegov.authservice.service.ContactService;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean createContactForCurrentUser(UserResponseDTO userResponseDTO) {
            Contact userContact = modelMapper.map(userResponseDTO, Contact.class);
            contactRepository.save(userContact);
            return true;

    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setName(contactDTO.getName());
        contact.setPicture(contactDTO.getPicture());
        contact.setWebsite(contactDTO.getWebsite());
        contact.setAddress(contactDTO.getAddress());
        contact.setMobile(contactDTO.getMobile());
        contact.setEmail(contactDTO.getEmail());

        Contact savedContact = contactRepository.save(contact);
        return mapToDTO(savedContact);
    }


    public void deleteContact(Long contactId) {
        contactRepository.deleteById(contactId);
    }

    @Override
    public long getUserCount() {

        return contactRepository.count();
    }

    @Override
    public ContactDTO updateContact(ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(contactDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        contact.setName(contactDTO.getName());
        contact.setPicture(contactDTO.getPicture());
        contact.setWebsite(contactDTO.getWebsite());
        contact.setAddress(contactDTO.getAddress());
        contact.setMobile(contactDTO.getMobile());
        contact.setEmail(contactDTO.getEmail());

        Contact updatedContact = contactRepository.save(contact);
        return mapToDTO(updatedContact);
    }

    private ContactDTO mapToDTO(Contact contact) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contact.getId());
        contactDTO.setName(contact.getName());
        contactDTO.setPicture(contact.getPicture());
        contactDTO.setWebsite(contact.getWebsite());
        contactDTO.setAddress(contact.getAddress());
        contactDTO.setMobile(contact.getMobile());
        contactDTO.setEmail(contact.getEmail());
        return contactDTO;
    }
}
