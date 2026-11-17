package com.joblessops.joblessops.jobapplication.application;

import com.joblessops.joblessops.jobapplication.api.ApplicationStatusChange;
import com.joblessops.joblessops.jobapplication.api.JobApplicationPersist;
import com.joblessops.joblessops.jobapplication.api.dto.ApplicationStatusHistoryResponse;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobApplicationRequest;
import com.joblessops.joblessops.jobapplication.api.dto.JobApplicationResponse;
import com.joblessops.joblessops.jobapplication.domain.ApplicationStatusHistory;
import com.joblessops.joblessops.jobapplication.domain.JobApplication;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import com.joblessops.joblessops.jobapplication.exception.JobApplicationNotFoundException;
import com.joblessops.joblessops.jobapplication.exception.JobPostingNotFoundException;
import com.joblessops.joblessops.jobapplication.exception.UnknownApplicationStatusException;
import com.joblessops.joblessops.jobapplication.persistence.ApplicationStatusHistoryRepository;
import com.joblessops.joblessops.jobapplication.persistence.ApplicationStatusRepository;
import com.joblessops.joblessops.jobapplication.persistence.JobApplicationRepository;
import com.joblessops.joblessops.jobapplication.persistence.JobPostingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobApplicationService implements JobApplicationPersist {
    private final JobApplicationRepository repository;

    private final ApplicationStatusRepository statusRepository;
    private final ApplicationStatusHistoryRepository historyRepository;

    private final JobPostingRepository postingRepository;

    public JobApplicationService(JobApplicationRepository repository,
                                 ApplicationStatusRepository statusRepository,
                                 ApplicationStatusHistoryRepository historyRepository,
                                 JobPostingRepository postingRepository) {
        this.repository = repository;
        this.postingRepository = postingRepository;
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

    @Transactional
    public JobApplicationResponse create(CreateJobApplicationRequest request) {
        var application = save(findPosting(request.jobPostingId()), request.notes(), request.appliedAt(),
                List.of(new ApplicationStatusChange("APPLIED", request.appliedAt().atStartOfDay())));
        return response(application);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findAll() {
        return repository.findAll().stream().map(this::response).toList();
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse get(UUID id) { return response(findApplication(id)); }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findByPosting(UUID id) {
        findPosting(id);
        return repository.findByJobPostingId(id).stream().map(this::response).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationStatusHistoryResponse> history(UUID id) {
        findApplication(id);
        return historyRepository.findByJobApplicationIdOrderByChangedAtAsc(id).stream()
                .map(ApplicationStatusHistoryResponse::from).toList();
    }

    @Transactional
    public ApplicationStatusHistoryResponse changeStatus(UUID id, ApplicationStatusChange change) {
        var application = findApplication(id);
        var status = statusRepository.findByCode(change.statusCode()).orElseThrow(() ->
                new UnknownApplicationStatusException("Unknown application status: " + change.statusCode()));
        var entry = new ApplicationStatusHistory();
        entry.setJobApplication(application);
        entry.setStatus(status);
        entry.setChangedAt(change.changedAt());
        return ApplicationStatusHistoryResponse.from(historyRepository.save(entry));
    }

    private JobApplication findApplication(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new JobApplicationNotFoundException("Job application not found: " + id));
    }

    private JobPosting findPosting(UUID id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new JobPostingNotFoundException("Job posting not found: " + id));
    }

    private JobApplicationResponse response(JobApplication application) {
        var history = historyRepository.findByJobApplicationIdOrderByChangedAtAsc(application.getId());
        String statusCode = history.isEmpty() ? null : history.getLast().getStatus().getCode();
        return new JobApplicationResponse(application.getId(), application.getJobPosting().getId(),
                application.getNotes(), application.getAppliedAt(), statusCode);
    }
}
