package com.dariawan.contactapp.controller;

import com.dariawan.contactapp.domain.Contact;
import com.dariawan.contactapp.exception.BadResourceException;
import com.dariawan.contactapp.exception.ResourceAlreadyExistsException;
import com.dariawan.contactapp.exception.ResourceNotFoundException;
import com.dariawan.contactapp.service.ContactService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final int ROW_PER_PAGE = 5;
    
    @Autowired
    private ContactService contactService;
    
    @GetMapping(value = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contact>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(required=false) String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.ok(contactService.findAll(pageNumber, ROW_PER_PAGE));
        }
        else {
            return ResponseEntity.ok(contactService.findAllByName(name, pageNumber, ROW_PER_PAGE));
        }
    }

    @GetMapping("/api/contacts/{contactId}")
    public ResponseEntity<Contact> findContactById(@PathVariable long contactId) {
        try {
            Contact book = contactService.findById(contactId);
            return ResponseEntity.ok(book);  // return 200, with json body
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
        }
    }
    
    @PostMapping(value = "/api/contacts")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) 
            throws URISyntaxException {
        try {
            Contact newContact = contactService.save(contact);
            return ResponseEntity.created(new URI("/api/contacts/" + newContact.getId()))
                    .body(contact);
        } catch (ResourceAlreadyExistsException ex) {
            // log exception first, then return Conflict (409)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping(value = "/api/contacts/{contactId}")
    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact, 
            @PathVariable long contactId) {
        try {
            contact.setId(contactId);
            contactService.update(contact);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PatchMapping("/api/contacts/{contactId}")
    public ResponseEntity<Void> updateAddress(@PathVariable long bookId, 
            @RequestParam(required=false) String address1,
            @RequestParam(required=false) String address2,
            @RequestParam(required=false) String address3,
            @RequestParam(required=false) String postalCode) {
        try {
            contactService.updateAddress(bookId, 
                    address1, address2, address3, postalCode);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping(path="/api/contacts/{contactId}")
    public ResponseEntity<Void> deleteContactById(@PathVariable long contactId) {
        try {
            contactService.deleteById(contactId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
