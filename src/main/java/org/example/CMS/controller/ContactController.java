package org.example.CMS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.CMS.dto.Contact;
import org.example.CMS.dto.ContactList;
import org.example.CMS.errorhandler.ErrorCode;
import org.example.CMS.errorhandler.InvalidInputException;
import org.example.CMS.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/contact")
@Validated
@Tag(name = "Contact Controller", description = "This controller provide APIs for creating, updating, fetching and deleting contacts")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping()
    @Operation(summary = "For creating new contact")
    public ResponseEntity<HttpStatus> createContact(@Valid @RequestBody Contact contact, BindingResult result) {
        return contactService.createContact(contact);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "For updating new contact")
    public ResponseEntity<HttpStatus> updateContact(@PathVariable("id") Long id,
                                                    @RequestBody Contact contact) {
        contact.setId(id);
        return contactService.updateContact(contact);
    }

    @GetMapping()
    @Operation(summary = "For fetching contacts based on filters")
    public ContactList getContact(@RequestParam(value = "id", required = false) Long id,
                                  @RequestParam(value = "firstName", required = false) String firstName,
                                  @RequestParam(value = "lastName", required = false) String lastName,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return contactService.getContact(id, firstName, lastName, email, pageNo, pageSize);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "For deleting contacts by id")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable("id") Long id) {
        return contactService.deleteContact(id);
    }
}
