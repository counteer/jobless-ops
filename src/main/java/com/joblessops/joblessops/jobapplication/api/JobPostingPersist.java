package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;

public interface JobPostingPersist {
    JobPosting save(CreateJobPostingRequest request);
}
