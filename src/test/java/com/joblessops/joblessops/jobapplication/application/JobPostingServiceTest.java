package com.joblessops.joblessops.jobapplication.application;


import com.joblessops.joblessops.company.api.CompanyLookup;
import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import com.joblessops.joblessops.jobapplication.exception.JobPostingNotFoundException;
import com.joblessops.joblessops.jobapplication.persistence.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobPostingServiceTest {

    @Mock
    JobPostingRepository jobPostingRepository;

    @Mock
    CompanyLookup companyLookup;

    @InjectMocks
    JobPostingService underTest;

    @Test
    public void testSave() {
        UUID companyId = UUID.randomUUID();
        CreateJobPostingRequest request = new CreateJobPostingRequest(companyId, "TITLE", "URL", "DESCRIPTION");
        when(jobPostingRepository.save(any(JobPosting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobPosting result = underTest.save(request);

        assertNotNull(result);
        assertEquals(companyId, result.getCompanyId());
        assertEquals("TITLE", result.getTitle());
        assertEquals("URL", result.getUrl());
        assertEquals("DESCRIPTION", result.getDescription());
        verify(jobPostingRepository).save(argThat(posting ->
                posting.getCompanyId().equals(companyId)
                        && posting.getTitle().equals("TITLE")
                        && posting.getUrl().equals("URL")
                        && posting.getDescription().equals("DESCRIPTION")
        ));
        verify(companyLookup).ensureExists(companyId);
    }

    @Test
    public void testSaveWithNonExistingCompany() {
        UUID companyId = UUID.randomUUID();
        CreateJobPostingRequest request = new CreateJobPostingRequest(companyId, "TITLE", "URL", "DESCRIPTION");
        doThrow(new CompanyNotFoundException("Not found")).when(companyLookup).ensureExists(companyId);

        assertThrows(CompanyNotFoundException.class, () -> underTest.save(request));

        verifyNoInteractions(jobPostingRepository);
        verify(companyLookup).ensureExists(companyId);
    }

    @Test
    public void testLoadByCompanyId() {
        UUID companyId = UUID.randomUUID();
        JobPosting jp = new JobPosting();
        jp.setTitle("TITLE");
        jp.setCompanyId(companyId);
        jp.setDescription("DESCRIPTION");
        jp.setUrl("URL");
        when(jobPostingRepository.findByCompanyId(companyId)).thenReturn(List.of(jp));
        List<JobPosting> result = underTest.findByCompanyId(companyId);
        assertEquals(1, result.size());
        JobPosting resultJob = result.getFirst();
        assertEquals(jp, resultJob);
        verify(jobPostingRepository).findByCompanyId(companyId);
        verify(companyLookup).ensureExists(companyId);
    }

    @Test
    public void testLoadByCompanyIdEmptyList() {
        UUID companyId = UUID.randomUUID();
        when(jobPostingRepository.findByCompanyId(companyId)).thenReturn(Collections.emptyList());
        List<JobPosting> result = underTest.findByCompanyId(companyId);
        assertTrue(result.isEmpty());
        verify(jobPostingRepository).findByCompanyId(companyId);
        verify(companyLookup).ensureExists(companyId);
    }

    @Test
    public void testLoadByCompanyIdWithNonExistingCompany() {
        UUID companyId = UUID.randomUUID();
        doThrow(new CompanyNotFoundException("Not found")).when(companyLookup).ensureExists(companyId);

        assertThrows(CompanyNotFoundException.class, () -> underTest.findByCompanyId(companyId));

        verifyNoInteractions(jobPostingRepository);
        verify(companyLookup).ensureExists(companyId);
    }

    @Test
    public void testLoadById() {
        UUID jobPostingId = UUID.randomUUID();
        JobPosting jp = new JobPosting();
        jp.setTitle("TITLE");
        jp.setId(jobPostingId);
        jp.setDescription("DESCRIPTION");
        jp.setUrl("URL");
        when(jobPostingRepository.findById(jobPostingId)).thenReturn(Optional.of(jp));
        JobPosting result = underTest.find(jobPostingId);
        assertEquals(jp, result);
        verify(jobPostingRepository).findById(jobPostingId);
    }

    @Test
    public void testLoadByIdWithNoData() {
        UUID jobPostingId = UUID.randomUUID();
        when(jobPostingRepository.findById(jobPostingId)).thenReturn(Optional.empty());
        assertThrows(JobPostingNotFoundException.class, () -> underTest.find(jobPostingId));
        verify(jobPostingRepository).findById(jobPostingId);
    }

}
