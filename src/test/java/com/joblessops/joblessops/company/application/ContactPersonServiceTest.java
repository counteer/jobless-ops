package com.joblessops.joblessops.company.application;

import com.joblessops.joblessops.company.api.dto.CreateContactPersonRequest;
import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.company.domain.ContactPerson;
import com.joblessops.joblessops.company.persistence.ContactPersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactPersonServiceTest {
    @Mock
    private ContactPersonRepository contactPersonRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private ContactPersonService underTest;

    @Test
    public void save() {
        Company company = new Company();
        UUID companyId = UUID.randomUUID();
        when(companyService.get(companyId)).thenReturn(company);
        ContactPerson contactPerson1 = new ContactPerson("ANY_NAME", "ANY_EMAIL", "ANY_PHONE", "ANY_TITLE", company);
        CreateContactPersonRequest dto = new CreateContactPersonRequest("ANY_NAME", "ANY_EMAIL", "ANY_PHONE", "ANY_TITLE");

        when(contactPersonRepository.save(any())).thenReturn(contactPerson1);
        ContactPerson result = underTest.save(companyId, dto);


        ArgumentCaptor<ContactPerson> captor =
                ArgumentCaptor.forClass(ContactPerson.class);
        verify(companyService).get(companyId);

        verify(contactPersonRepository).save(captor.capture());

        ContactPerson captured = captor.getValue();

        assertEquals(dto.name(), captured.getName());
        assertEquals(dto.email(), captured.getEmail());
        assertEquals(dto.phone(), captured.getPhone());
        assertEquals(dto.title(), captured.getTitle());
        assertSame(company, captured.getCompany());
        assertSame(contactPerson1, result);

    }

    @Test
    public void saveWithNonExistingCompanyTest() {
        UUID companyId = UUID.randomUUID();
        when(companyService.get(companyId)).thenThrow(new CompanyNotFoundException("CompanyNotFound"));
        CreateContactPersonRequest dto = new CreateContactPersonRequest("ANY_NAME", "ANY_EMAIL", "ANY_PHONE", "ANY_TITLE");

        assertThrows(CompanyNotFoundException.class, () -> underTest.save(companyId, dto));

    }

    @Test
    public void findByCompanyIdTest() {
        UUID companyId = UUID.randomUUID();
        List<ContactPerson> mockResult =new ArrayList<>();
        when(contactPersonRepository.findByCompanyId(companyId)).thenReturn(mockResult);
        assertSame(mockResult, underTest.findByCompanyId(companyId));
    }

    @Test
    public void findByCompanyIdCompanyDoesNotExistTest() {
        UUID companyId = UUID.randomUUID();
        doThrow(new CompanyNotFoundException("Company found"))
                .when(companyService).ensureExists(companyId);
        assertThrows(CompanyNotFoundException.class, () -> underTest.findByCompanyId(companyId));
    }


}
