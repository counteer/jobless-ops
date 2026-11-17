package com.joblessops.joblessops.importing.application;

import com.joblessops.joblessops.company.api.CompanyPersist;
import com.joblessops.joblessops.company.api.CompanyLookup;
import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;
import com.joblessops.joblessops.importing.api.ImportCompanyRequest;
import com.joblessops.joblessops.importing.api.ImportJobPostingRequest;
import com.joblessops.joblessops.importing.api.ImportRequest;
import com.joblessops.joblessops.jobapplication.api.ApplicationStatusChange;
import com.joblessops.joblessops.jobapplication.api.JobApplicationPersist;
import com.joblessops.joblessops.jobapplication.api.JobPostingPersist;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.joblessops.joblessops.importing.api.exceptions.InvalidImportException;

@Service
public class ImportService {

    private final CompanyPersist companyPersist;
    private final CompanyLookup companyLookup;

    private final JobPostingPersist jobPostingPersist;
    private final JobApplicationPersist jobApplicationPersist;

    public ImportService(CompanyPersist companyPersist, JobPostingPersist jobPostingPersist,
                         JobApplicationPersist jobApplicationPersist, CompanyLookup companyLookup) {
        this.companyPersist = companyPersist;
        this.companyLookup = companyLookup;
        this.jobPostingPersist = jobPostingPersist;
        this.jobApplicationPersist = jobApplicationPersist;
    }

    @Transactional
    public void importCompanies(ImportRequest request) {
        List<ImportCompanyRequest> companies = request.companies();
        for (ImportCompanyRequest company : companies) {
            CompanyResponse savedCompany = companyLookup.findByName(company.name())
                    .orElseGet(() -> companyPersist.save(convertToCompany(company)));
            List<ImportJobPostingRequest> importJobPostingRequests = company.jobPostings();
            for (ImportJobPostingRequest jobPostingRequest : importJobPostingRequests) {
                JobPosting posting = jobPostingPersist.save(convert(savedCompany.id(), jobPostingRequest));
                for (var application : jobPostingRequest.jobApplications()) {
                    var history = application.statusHistory();
                    if (application.statusCode() != null) {
                        if (!history.isEmpty()) {
                            throw new InvalidImportException(
                                    "Provide either statusCode or statusHistory, not both");
                        }
                        history = List.of(new ApplicationStatusChange(
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
