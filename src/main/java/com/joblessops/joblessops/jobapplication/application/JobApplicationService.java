package com.joblessops.joblessops.jobapplication.application;

import com.joblessops.joblessops.jobapplication.api.JobApplicationPersist;
import com.joblessops.joblessops.jobapplication.domain.JobApplication;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import com.joblessops.joblessops.jobapplication.persistence.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.joblessops.joblessops.jobapplication.api.ApplicationStatusChange;
import com.joblessops.joblessops.jobapplication.domain.ApplicationStatusHistory;
import com.joblessops.joblessops.jobapplication.persistence.ApplicationStatusRepository;
import com.joblessops.joblessops.jobapplication.persistence.ApplicationStatusHistoryRepository;
import org.springframework.transaction.annotation.Transactional;
import com.joblessops.joblessops.jobapplication.exception.UnknownApplicationStatusException;

@Service
public class JobApplicationService implements JobApplicationPersist {
    private final JobApplicationRepository repository;

    private final ApplicationStatusRepository statusRepository;
    private final ApplicationStatusHistoryRepository historyRepository;

    public JobApplicationService(JobApplicationRepository repository,
                                 ApplicationStatusRepository statusRepository,
                                 ApplicationStatusHistoryRepository historyRepository) {
        this.repository = repository;
        this.statusRepository = statusRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    @Transactional
    public JobApplication save(JobPosting jobPosting, String notes, LocalDate appliedAt,
                               List<ApplicationStatusChange> statusHistory) {
        JobApplication application = new JobApplication();
        application.setJobPosting(jobPosting);
        application.setNotes(notes);
        application.setAppliedAt(appliedAt);
        // Resolve all codes before writing any application or history records.
        var history = statusHistory.stream().map(change -> {
            var status = statusRepository.findByCode(change.statusCode()).orElseThrow(() ->
                    new UnknownApplicationStatusException(
                            "Unknown application status: " + change.statusCode()));
            var entry = new ApplicationStatusHistory();
            entry.setStatus(status);
            entry.setChangedAt(change.changedAt());
            return entry;
        }).toList();
        var saved = repository.save(application);
        for (var entry : history) {
            entry.setJobApplication(saved);
            historyRepository.save(entry);
        }
        return saved;
    }
}
