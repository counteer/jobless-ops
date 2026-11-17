package com.joblessops.joblessops.importing.application;

import com.joblessops.joblessops.company.api.CompanyPersist;
import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;
import com.joblessops.joblessops.importing.api.ImportCompanyRequest;
import com.joblessops.joblessops.importing.api.ImportJobPostingRequest;
import com.joblessops.joblessops.importing.api.ImportRequest;
import com.joblessops.joblessops.jobapplication.api.JobPostingPersist;
import com.joblessops.joblessops.jobapplication.api.JobApplicationPersist;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ImportService {

    private final CompanyPersist companyPersist;

    private final JobPostingPersist jobPostingPersist;
    private final JobApplicationPersist jobApplicationPersist;

    public ImportService(CompanyPersist companyPersist, JobPostingPersist jobPostingPersist,
                         JobApplicationPersist jobApplicationPersist) {
        this.companyPersist = companyPersist;
        this.jobPostingPersist = jobPostingPersist;
        this.jobApplicationPersist = jobApplicationPersist;
    }

    @Transactional
    public void importCompanies(ImportRequest request) {
        List<ImportCompanyRequest> companies = request.companies();
        for (ImportCompanyRequest company : companies) {
            CompanyResponse save = companyPersist.save(convertToCompany(company));
            List<ImportJobPostingRequest> importJobPostingRequests = company.jobPostings();
            for (ImportJobPostingRequest jobPostingRequest : importJobPostingRequests) {
                JobPosting posting = jobPostingPersist.save(convert(save.id(), jobPostingRequest));
                for (var application : jobPostingRequest.jobApplications()) {
                    var history = application.statusHistory();
                    if (application.statusCode() != null) {
                        if (!history.isEmpty()) {
                            throw new org.springframework.web.server.ResponseStatusException(
                                    org.springframework.http.HttpStatus.BAD_REQUEST,
                                    "Provide either statusCode or statusHistory, not both");
                        }
                        history = List.of(new com.joblessops.joblessops.jobapplication.api.ApplicationStatusChange(
                                application.statusCode(), application.appliedAt().atStartOfDay()));
                    }
                    jobApplicationPersist.save(posting, application.notes(), application.appliedAt(), history);
                }
            }
        }

    }

    private CreateCompanyRequest convertToCompany(ImportCompanyRequest request) {
        return new CreateCompanyRequest(request.name(), request.website());
    }

    private CreateJobPostingRequest convert(UUID companyId, ImportJobPostingRequest postingDto) {
        return new CreateJobPostingRequest(
                companyId,
                postingDto.title(),
                postingDto.url(),
                postingDto.description()
        );
    }
}
