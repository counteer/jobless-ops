package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.domain.JobApplication;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import java.time.LocalDate;
import java.util.List;

public interface JobApplicationPersist {
    JobApplication save(JobPosting jobPosting, String notes, LocalDate appliedAt,
                        List<ApplicationStatusChange> statusHistory);
}
