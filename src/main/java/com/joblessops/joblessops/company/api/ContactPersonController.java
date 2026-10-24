package com.joblessops.joblessops.company.api;

import com.joblessops.joblessops.company.api.dto.ContactPersonResponse;
import com.joblessops.joblessops.company.api.dto.CreateContactPersonRequest;
import com.joblessops.joblessops.company.application.ContactPersonService;
import com.joblessops.joblessops.company.domain.ContactPerson;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies/{companyId}/contacts")
public class ContactPersonController {

    private final ContactPersonService contactPersonService;


    public ContactPersonController(ContactPersonService contactPersonService) {
        this.contactPersonService = contactPersonService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactPersonResponse save(@RequestBody @Valid CreateContactPersonRequest personRequest, @PathVariable UUID companyId) {
        ContactPerson savedPerson = contactPersonService.save(companyId, personRequest);
        return convert(savedPerson);
    }

    @GetMapping
    public List<ContactPersonResponse> findAll(@PathVariable UUID companyId) {
        List<ContactPerson> personList = contactPersonService.findByCompanyId(companyId);
        return personList.stream().map(this::convert).toList();
    }

    private ContactPersonResponse convert(ContactPerson contactPerson) {
        return new ContactPersonResponse(contactPerson.getId(), contactPerson.getName(), contactPerson.getEmail(), contactPerson.getPhone(), contactPerson.getTitle(), contactPerson.getCompany().getId());
    }
}
