package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.company.api.CompanyController;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.jobapplication.api.dto.CreateJobPostingRequest;
import com.joblessops.joblessops.jobapplication.application.JobPostingService;
import com.joblessops.joblessops.jobapplication.domain.JobPosting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobPostingController.class)
public class JobPostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobPostingService jobPostingService;
    //api/job-postings

    @Test
    void shouldSaveJobPosting() throws Exception {
        UUID companyId = UUID.randomUUID();
        JobPosting jobPosting = new JobPosting();
        jobPosting.setCompanyId(companyId);
        jobPosting.setUrl("URL");
        jobPosting.setDescription("DESCRIPTION");
        jobPosting.setTitle("TITLE");
        when(jobPostingService.save(any(CreateJobPostingRequest.class))).thenReturn(jobPosting);
        mockMvc.perform(post("/api/job-postings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                  "title": "TITLE",
                                  "url": "URL",
                                  "description": "DESCRIPTION",
                                  "companyId": "%s"
                                }
                                """, companyId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("TITLE"))
                .andExpect(jsonPath("$.description").value("DESCRIPTION"))
                .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                .andExpect(jsonPath("$.url").value("URL"));

        verify(jobPostingService).save(argThat(request ->
                request.companyId().equals(companyId)
                        && request.title().equals("TITLE")
                        && request.url().equals("URL")
                        && request.description().equals("DESCRIPTION")
        ));
    }

    @Test
    public void saveJobPostWithWrongParameters() throws Exception {
        UUID companyId = UUID.randomUUID();
        mockMvc.perform(post("/api/job-postings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                  "title": "",
                                  "url": "URL",
                                  "description": "DESCRIPTION",
                                  "companyId": "%s"
                                }
                                """, companyId)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(jobPostingService);
    }

    @Test
    public void getJobPosting() throws Exception {
        UUID jobPostingId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        JobPosting jobPosting = new JobPosting();
        jobPosting.setCompanyId(companyId);
        jobPosting.setUrl("URL");
        jobPosting.setDescription("DESCRIPTION");
        jobPosting.setTitle("TITLE");
        jobPosting.setId(jobPostingId);
        when(jobPostingService.find(jobPostingId)).thenReturn(jobPosting);

        mockMvc.perform(get("/api/job-postings/{jobPostingId}", jobPostingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TITLE"))
                .andExpect(jsonPath("$.description").value("DESCRIPTION"))
                .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                .andExpect(jsonPath("$.url").value("URL"));
        verify(jobPostingService).find(jobPostingId);
    }
}
