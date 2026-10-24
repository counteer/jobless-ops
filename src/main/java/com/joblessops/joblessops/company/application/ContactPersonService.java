package com.joblessops.joblessops.company.application;

import com.joblessops.joblessops.company.api.dto.CreateContactPersonRequest;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.company.domain.ContactPerson;
import com.joblessops.joblessops.company.persistence.ContactPersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final CompanyService companyService;

    public ContactPersonService(ContactPersonRepository contactPersonRepository, CompanyService companyService) {
        this.contactPersonRepository = contactPersonRepository;
        this.companyService = companyService;
    }

    public List<ContactPerson> findByCompanyId(UUID companyId) {
        companyService.ensureExists(companyId);
        return contactPersonRepository.findByCompanyId(companyId);
    }

    public ContactPerson save(UUID companyId, CreateContactPersonRequest contactPersonDto) {
        Company company = this.companyService.get(companyId);
        ContactPerson contactPerson = new ContactPerson(contactPersonDto.name(), contactPersonDto.email(), contactPersonDto.phone(), contactPersonDto.title(), company);

        return contactPersonRepository.save(contactPerson);
    }
}
