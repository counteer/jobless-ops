package com.joblessops.joblessops.company.application;

import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;
import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.company.persistence.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService underTest;

    @Test
    void testSave() {
        CreateCompanyRequest company = new CreateCompanyRequest("Google", "https://google.com");
        when(companyRepository.save(any())).thenReturn(company);

        CompanyResponse result = underTest.save(company);

        assertEquals(company.name(), result.name());
        verify(companyRepository).save(any());
    }

    @Test
    void testFindByIdWithValidId() {
        Company company = new Company("Google", "https://google.com");
        UUID id = UUID.randomUUID();
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        Company result = underTest.get(id);

        assertSame(company, result);
        verify(companyRepository).findById(id);
    }

    @Test
    void testFindByIdWithNonExistingId() {
        UUID id = UUID.randomUUID();
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> underTest.get(id));

        verify(companyRepository).findById(id);

    }
}
