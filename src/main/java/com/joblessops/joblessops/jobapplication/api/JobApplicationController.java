package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.api.dto.ApplicationStatusHistoryResponse;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobApplicationRequest;
import com.joblessops.joblessops.jobapplication.api.dto.JobApplicationResponse;
import com.joblessops.joblessops.jobapplication.application.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class JobApplicationController {
    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) { this.service = service; }

    @PostMapping("/job-applications")
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse create(@Valid @RequestBody CreateJobApplicationRequest request) {
        return service.create(request);
    }

    @GetMapping("/job-applications")
    public List<JobApplicationResponse> list() { return service.findAll(); }

    @GetMapping("/job-applications/{id}")
    public JobApplicationResponse get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping("/job-postings/{id}/job-applications")
    public List<JobApplicationResponse> listForPosting(@PathVariable UUID id) { return service.findByPosting(id); }

    @GetMapping("/job-applications/{id}/status-history")
    public List<ApplicationStatusHistoryResponse> history(@PathVariable UUID id) { return service.history(id); }

    @PostMapping("/job-applications/{id}/status-history")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationStatusHistoryResponse changeStatus(@PathVariable UUID id,
                                                        @Valid @RequestBody ApplicationStatusChange change) {
        return service.changeStatus(id, change);
    }
}
