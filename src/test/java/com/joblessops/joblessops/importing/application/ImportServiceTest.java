package com.joblessops.joblessops.importing.application;

import com.joblessops.joblessops.company.api.CompanyPersist;
import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;
import com.joblessops.joblessops.importing.api.ImportCompanyRequest;
import com.joblessops.joblessops.importing.api.ImportJobPostingRequest;
import com.joblessops.joblessops.importing.api.ImportRequest;
import com.joblessops.joblessops.jobapplication.api.JobPostingPersist;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImportServiceTest {

    @Mock
    CompanyPersist companyPersist;

    @Mock
    JobPostingPersist jobPostingPersist;

    @InjectMocks
    ImportService underTest;

    @Test
    void saveWithoutJobRequests() throws Exception {
        ImportCompanyRequest companyRequest = new ImportCompanyRequest("NAME", "WEBSITE", List.of());
        List<ImportCompanyRequest> companies = List.of(companyRequest);
        ImportRequest requestList = new ImportRequest(companies);
        underTest.importCompanies(requestList);
        verify(companyPersist).save(argThat(companyRequest1 ->
                companyRequest1.name().equals("NAME") &&
                        companyRequest1.website().equals("WEBSITE")));
        verifyNoInteractions(jobPostingPersist);
    }

    @Test
    void saveWithJobRequests() throws Exception {
        ImportJobPostingRequest jobPostingRequest = new ImportJobPostingRequest("TITLE", "URL", "DESCRIPTION");
        ImportCompanyRequest companyRequest = new ImportCompanyRequest("NAME", "WEBSITE", List.of(jobPostingRequest));
        List<ImportCompanyRequest> companies = List.of(companyRequest);
        ImportRequest requestList = new ImportRequest(companies);
        UUID companyId = UUID.randomUUID();
        when(companyPersist.save(any())).thenReturn(new CompanyResponse(companyId, "NAME", "WEBSITE"));
        underTest.importCompanies(requestList);
        verify(companyPersist).save(argThat(companyRequest1 ->
                companyRequest1.name().equals("NAME") &&
                        companyRequest1.website().equals("WEBSITE")));
        verify(jobPostingPersist).save(argThat(jobPostingRequestArg ->
                jobPostingRequestArg.companyId().equals(companyId) &&
                        jobPostingRequestArg.description().equals("DESCRIPTION") &&
                        jobPostingRequestArg.title().equals("TITLE") &&
                        jobPostingRequestArg.url().equals("URL")));
    }

    @Test
    void saveWithMultiplJobRequests() throws Exception {
        ImportRequest requestList = getRequestList();
        UUID companyId = UUID.randomUUID();
        UUID companyId2 = UUID.randomUUID();
        when(companyPersist.save(any())).thenAnswer(invocation -> {
            CreateCompanyRequest request = invocation.getArgument(0);

            return switch (request.name()) {
                case "NAME" -> new CompanyResponse(
                        companyId,
                        request.name(),
                        request.website()
                );
                case "NAME2" -> new CompanyResponse(
                        companyId2,
                        request.name(),
                        request.website()
                );
                default -> throw new IllegalArgumentException(
                        "Unexpected company: " + request.name()
                );
            };
        });
        underTest.importCompanies(requestList);
        verify(companyPersist).save(argThat(company ->
                company.name().equals("NAME") &&
                        company.website().equals("WEBSITE")));

        verify(companyPersist).save(argThat(company ->
                company.name().equals("NAME2") &&
                        company.website().equals("WEBSITE2")));

        verify(jobPostingPersist).save(argThat(posting ->
                posting.companyId().equals(companyId) &&
                        posting.title().equals("TITLE") &&
                        posting.url().equals("URL") &&
                        posting.description().equals("DESCRIPTION")));

        verify(jobPostingPersist).save(argThat(posting ->
                posting.companyId().equals(companyId) &&
                        posting.title().equals("TITLE2") &&
                        posting.url().equals("URL2") &&
                        posting.description().equals("DESCRIPTION2")));


        verify(jobPostingPersist).save(argThat(posting ->
                posting.companyId().equals(companyId2) &&
                        posting.title().equals("TITLE4") &&
                        posting.url().equals("URL4") &&
                        posting.description().equals("DESCRIPTION4")));

        verify(jobPostingPersist).save(argThat(posting ->
                posting.companyId().equals(companyId2) &&
                        posting.title().equals("TITLE3") &&
                        posting.url().equals("URL3") &&
                        posting.description().equals("DESCRIPTION3")));


        verifyNoMoreInteractions(companyPersist, jobPostingPersist);

    }

    private static @NonNull ImportRequest getRequestList() {
        ImportJobPostingRequest jobPostingRequest = new ImportJobPostingRequest("TITLE", "URL", "DESCRIPTION");
        ImportJobPostingRequest jobPostingRequest2 = new ImportJobPostingRequest("TITLE2", "URL2", "DESCRIPTION2");
        ImportJobPostingRequest jobPostingRequest3 = new ImportJobPostingRequest("TITLE3", "URL3", "DESCRIPTION3");
        ImportJobPostingRequest jobPostingRequest4 = new ImportJobPostingRequest("TITLE4", "URL4", "DESCRIPTION4");
        ImportCompanyRequest companyRequest = new ImportCompanyRequest("NAME", "WEBSITE", List.of(jobPostingRequest, jobPostingRequest2));
        ImportCompanyRequest companyRequest2 = new ImportCompanyRequest("NAME2", "WEBSITE2", List.of(jobPostingRequest3, jobPostingRequest4));
        List<ImportCompanyRequest> companies = List.of(companyRequest, companyRequest2);
        return new ImportRequest(companies);
    }
}
