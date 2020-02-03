/**
 * Spring Boot + JPA/Hibernate + PostgreSQL RESTful CRUD API Example (https://www.dariawan.com)
 * Copyright (C) 2020 Dariawan <hello@dariawan.com>
 *
 * Creative Commons Attribution-ShareAlike 4.0 International License
 *
 * Under this license, you are free to:
 * # Share - copy and redistribute the material in any medium or format
 * # Adapt - remix, transform, and build upon the material for any purpose,
 *   even commercially.
 *
 * The licensor cannot revoke these freedoms
 * as long as you follow the license terms.
 *
 * License terms:
 * # Attribution - You must give appropriate credit, provide a link to the
 *   license, and indicate if changes were made. You may do so in any
 *   reasonable manner, but not in any way that suggests the licensor
 *   endorses you or your use.
 * # ShareAlike - If you remix, transform, or build upon the material, you must
 *   distribute your contributions under the same license as the original.
 * # No additional restrictions - You may not apply legal terms or
 *   technological measures that legally restrict others from doing anything the
 *   license permits.
 *
 * Notices:
 * # You do not have to comply with the license for elements of the material in
 *   the public domain or where your use is permitted by an applicable exception
 *   or limitation.
 * # No warranties are given. The license may not give you all of
 *   the permissions necessary for your intended use. For example, other rights
 *   such as publicity, privacy, or moral rights may limit how you use
 *   the material.
 *
 * You may obtain a copy of the License at
 *   https://creativecommons.org/licenses/by-sa/4.0/
 *   https://creativecommons.org/licenses/by-sa/4.0/legalcode
 */
package com.dariawan.contactapp.controller;

import com.dariawan.contactapp.domain.Address;
import com.dariawan.contactapp.domain.Contact;
import com.dariawan.contactapp.exception.BadResourceException;
import com.dariawan.contactapp.exception.ResourceAlreadyExistsException;
import com.dariawan.contactapp.exception.ResourceNotFoundException;
import com.dariawan.contactapp.service.ContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for Creating, Retrieving, Updating and Deleting of Contacts.",
        tags = {"contact"})
@RestController
@RequestMapping("/api")
public class ContactController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final int ROW_PER_PAGE = 5;
    
    @Autowired
    private ContactService contactService;
    
    @ApiOperation(value = "Find Contacts by name", notes = "Name search by %name% format", tags = { "contact" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response=List.class )  })	    
    @GetMapping(value = "/contacts")
    public ResponseEntity<List<Contact>> findAll(
            @ApiParam("Page number, default is 1") @RequestParam(value="page", defaultValue="1") int pageNumber,
            @ApiParam("Name of the contact for search.") @RequestParam(required=false) String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.ok(contactService.findAll(pageNumber, ROW_PER_PAGE));
        }
        else {
            return ResponseEntity.ok(contactService.findAllByName(name, pageNumber, ROW_PER_PAGE));
        }
    }

    @ApiOperation(value = "Find contact by ID", notes = "Returns a single contact", tags = { "contact" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response=Contact.class),
        @ApiResponse(code = 404, message = "Contact not found") })
    @GetMapping(value = "/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> findContactById(
            @ApiParam("Id of the contact to be obtained. Cannot be empty.")
            @PathVariable long contactId) {
        try {
            Contact contact = contactService.findById(contactId);
            return ResponseEntity.ok(contact);  // return 200, with json body
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
        }
    }
    
    @ApiOperation(value = "Add a new contact", tags = { "contact" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Contact created"), 
        @ApiResponse(code = 400, message = "Invalid input"), 
        @ApiResponse(code = 409, message = "Contact already exists") })	    
    @PostMapping(value = "/contacts")
    public ResponseEntity<Contact> addContact(
            @ApiParam("Contact to add. Cannot null or empty.")
            @Valid @RequestBody Contact contact) 
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
    
    @ApiOperation(value = "Update an existing contact", tags = { "contact" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Contact not found"),
        @ApiResponse(code = 405, message = "Validation exception") })
    @PutMapping(value = "/contacts/{contactId}")
    public ResponseEntity<Contact> updateContact(
            @ApiParam("Id of the contact to be update. Cannot be empty.")
            @PathVariable long contactId,
            @ApiParam("Contact to update. Cannot null or empty.")
            @Valid @RequestBody Contact contact) {
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
    
    @ApiOperation(value = "Update an existing contact's address", tags = { "contact" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 404, message = "Contact not found") })
    @PatchMapping("/contacts/{contactId}")
    public ResponseEntity<Void> updateAddress(
            @ApiParam("Id of the contact to be update. Cannot be empty.")
            @PathVariable long contactId,
            @ApiParam("Contact's address to update.")
            @RequestBody Address address) {
        try {
            contactService.updateAddress(contactId, address);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @ApiOperation(value = "Deletes a contact", tags = { "contact" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 404, message = "Contact not found") })
    @DeleteMapping(path="/contacts/{contactId}")
    public ResponseEntity<Void> deleteContactById(
            @ApiParam("Id of the contact to be delete. Cannot be empty.")
            @PathVariable long contactId) {
        try {
            contactService.deleteById(contactId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
