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
package com.dariawan.contactapp.service;

import com.dariawan.contactapp.domain.Address;
import com.dariawan.contactapp.domain.Contact;
import com.dariawan.contactapp.exception.BadResourceException;
import com.dariawan.contactapp.exception.ResourceAlreadyExistsException;
import com.dariawan.contactapp.exception.ResourceNotFoundException;
import com.dariawan.contactapp.repository.ContactRepository;
import com.dariawan.contactapp.specification.ContactSpecification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;
    
    private boolean existsById(Long id) {
        return contactRepository.existsById(id);
    }
    
    public Contact findById(Long id) throws ResourceNotFoundException {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact==null) {
            throw new ResourceNotFoundException("Cannot find Contact with id: " + id);
        }
        else return contact;
    }
    
    public List<Contact> findAll(int pageNumber, int rowPerPage) {
        List<Contact> contacts = new ArrayList<>();
        contactRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPage)).forEach(contacts::add);
        return contacts;
    }
    
    public List<Contact> findAllByName(String name, int pageNumber, int rowPerPage) {
        Contact filter = new Contact();
        filter.setName(name);
        Specification<Contact> spec = new ContactSpecification(filter);
        
        List<Contact> contacts = new ArrayList<>();
        contactRepository.findAll(spec, PageRequest.of(pageNumber - 1, rowPerPage)).forEach(contacts::add);
        return contacts;
    }
    
    public Contact save(Contact contact) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(contact.getName())) {
            if (contact.getId() != null && existsById(contact.getId())) { 
                throw new ResourceAlreadyExistsException("Contact with id: " + contact.getId() +
                        " already exists");
            }
            return contactRepository.save(contact);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save contact");
            exc.addErrorMessage("Contact is null or empty");
            throw exc;
        }
    }
    
    public void update(Contact contact) 
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(contact.getName())) {
            if (!existsById(contact.getId())) {
                throw new ResourceNotFoundException("Cannot find Contact with id: " + contact.getId());
            }
            contactRepository.save(contact);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save contact");
            exc.addErrorMessage("Contact is null or empty");
            throw exc;
        }
    }
    
    public void updateAddress(Long id, Address address) 
            throws ResourceNotFoundException {
        Contact contact = findById(id);
        contact.setAddress1(address.getAddress1());
        contact.setAddress2(address.getAddress2());
        contact.setAddress3(address.getAddress3());
        contact.setPostalCode(address.getPostalCode());
        contactRepository.save(contact);        
    }
    
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find contact with id: " + id);
        }
        else {
            contactRepository.deleteById(id);
        }
    }
    
    public Long count() {
        return contactRepository.count();
    }
}
