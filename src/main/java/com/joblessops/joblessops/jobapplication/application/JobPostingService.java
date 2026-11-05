package com.joblessops.joblessops.jobapplication.application;

import com.joblessops.joblessops.company.api.CompanyLookup;
import com.joblessops.joblessops.jobapplication.api.JobPostingPersist;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import com.joblessops.joblessops.jobapplication.exception.JobPostingNotFoundException;
import com.joblessops.joblessops.jobapplication.persistence.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobPostingService implements JobPostingPersist {

    private final CompanyLookup companyLookup;

    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(CompanyLookup companyLookup, JobPostingRepository jobPostingRepository) {
        this.companyLookup = companyLookup;
        this.jobPostingRepository = jobPostingRepository;
    }

    @Override
    public JobPosting save(CreateJobPostingRequest request) {
        companyLookup.ensureExists(request.companyId());
        return jobPostingRepository.save(convert(request));
    }

    public JobPosting find(UUID id) {
        return jobPostingRepository.findById(id).orElseThrow(() -> new JobPostingNotFoundException("Not found"));
    }

    public List<JobPosting> findByCompanyId(UUID companyId) {
        companyLookup.ensureExists(companyId);
        return jobPostingRepository.findByCompanyId(companyId);
    }

    private JobPosting convert(CreateJobPostingRequest postingDto) {
        JobPosting posting = new JobPosting();
        posting.setCompanyId(postingDto.companyId());
        posting.setDescription(postingDto.description());
        posting.setTitle(postingDto.title());
        posting.setUrl(postingDto.url());
        return posting;
    }

}
