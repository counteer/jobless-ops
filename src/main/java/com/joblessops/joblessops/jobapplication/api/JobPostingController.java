package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import com.joblessops.joblessops.jobapplication.api.dto.JobPostingResponse;
import com.joblessops.joblessops.jobapplication.application.JobPostingService;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @PostMapping("/job-postings")
    @ResponseStatus(HttpStatus.CREATED)
    public JobPostingResponse create(
            @Valid @RequestBody CreateJobPostingRequest request) {
        return convert(jobPostingService.save(request));
    }


    @GetMapping("/job-postings/{jobPostingId}")
    public JobPostingResponse get(
            @PathVariable UUID jobPostingId) {
        return convert(jobPostingService.find(jobPostingId));
    }


    @GetMapping("/companies/{companyId}/job-postings")
    public List<JobPostingResponse> findByCompanyId(
            @PathVariable UUID companyId) {
        return jobPostingService.findByCompanyId(companyId).stream().map(this::convert).toList();
    }

    private JobPostingResponse convert(JobPosting jobPosting) {
        return new JobPostingResponse(jobPosting.getId(), jobPosting.getCompanyId(), jobPosting.getTitle(), jobPosting.getUrl(), jobPosting.getDescription());
    }
}