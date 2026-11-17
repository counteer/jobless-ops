package com.joblessops.joblessops.jobapplication.persistence;

import com.joblessops.joblessops.jobapplication.domain.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    List<JobApplication> findByJobPostingId(UUID jobPostingId);
}
