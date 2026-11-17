package com.joblessops.joblessops.jobapplication.application;

import com.joblessops.joblessops.jobapplication.api.ApplicationStatusChange;
import com.joblessops.joblessops.jobapplication.domain.*;
import com.joblessops.joblessops.jobapplication.exception.JobApplicationNotFoundException;
import com.joblessops.joblessops.jobapplication.exception.UnknownApplicationStatusException;
import com.joblessops.joblessops.jobapplication.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {
    @Mock JobPostingRepository postingRepository;
    @Mock JobApplicationRepository repository;
    @Mock ApplicationStatusRepository statusRepository;
    @Mock ApplicationStatusHistoryRepository historyRepository;
    @InjectMocks JobApplicationService service;

    @Test
    void preservesApplicationFieldsAndDatedStatusChanges() {
        var posting = new JobPosting();
        var date = LocalDate.of(2026, 11, 17);
        var applied = new ApplicationStatus();
        var rejected = new ApplicationStatus();
        when(statusRepository.findByCode("APPLIED")).thenReturn(Optional.of(applied));
        when(statusRepository.findByCode("REJECTED")).thenReturn(Optional.of(rejected));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var saved = service.save(posting, "Notes", date, List.of(
                new ApplicationStatusChange("APPLIED", date.atTime(10, 30)),
                new ApplicationStatusChange("REJECTED", date.plusDays(2).atTime(12, 0))));

        assertSame(posting, saved.getJobPosting());
        assertEquals("Notes", saved.getNotes());
        assertEquals(date, saved.getAppliedAt());
        var captor = ArgumentCaptor.forClass(ApplicationStatusHistory.class);
        verify(historyRepository, times(2)).save(captor.capture());
        var entries = captor.getAllValues();
        assertSame(saved, entries.get(0).getJobApplication());
        assertSame(saved, entries.get(1).getJobApplication());
        assertSame(applied, entries.get(0).getStatus());
        assertSame(rejected, entries.get(1).getStatus());
        assertEquals(date.atTime(10, 30), entries.get(0).getChangedAt());
        assertEquals(date.plusDays(2).atTime(12, 0), entries.get(1).getChangedAt());
    }

    @Test
    void rejectsUnknownStatusBeforeWriting() {
        var date = LocalDate.of(2026, 11, 17);
        when(statusRepository.findByCode("APPLIED")).thenReturn(Optional.of(new ApplicationStatus()));
        when(statusRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());
        var error = assertThrows(UnknownApplicationStatusException.class, () -> service.save(new JobPosting(), null, date,
                List.of(new ApplicationStatusChange("APPLIED", date.atStartOfDay()),
                        new ApplicationStatusChange("UNKNOWN", date.atTime(12, 0)))));
        assertEquals("Unknown application status: UNKNOWN", error.getMessage());
        verifyNoInteractions(repository, historyRepository);
    }

    @Test
    void importsLegacyApplicationWithoutHistory() {
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertNotNull(service.save(new JobPosting(), null, LocalDate.of(2026, 11, 17), List.of()));
        verifyNoInteractions(statusRepository, historyRepository);
    }

    @Test
    void missingApplicationIsReportedBeforeChangingStatus() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(JobApplicationNotFoundException.class,
                () -> service.changeStatus(id, new ApplicationStatusChange("APPLIED", LocalDate.of(2026, 11, 17).atStartOfDay())));
        verifyNoInteractions(statusRepository, historyRepository);
    }

    @Test
    void currentStatusComesFromLatestHistoryEntry() {
        var id = UUID.randomUUID();
        var application = new JobApplication();
        var posting = new JobPosting();
        posting.setId(UUID.randomUUID());
        application.setId(id);
        application.setJobPosting(posting);
        var applied = new ApplicationStatus();
        applied.setCode("APPLIED");
        var offer = new ApplicationStatus();
        offer.setCode("OFFER");
        var first = new ApplicationStatusHistory();
        first.setStatus(applied);
        var latest = new ApplicationStatusHistory();
        latest.setStatus(offer);
        when(repository.findById(id)).thenReturn(Optional.of(application));
        when(historyRepository.findByJobApplicationIdOrderByChangedAtAsc(id)).thenReturn(List.of(first, latest));
        assertEquals("OFFER", service.get(id).statusCode());
    }
}
