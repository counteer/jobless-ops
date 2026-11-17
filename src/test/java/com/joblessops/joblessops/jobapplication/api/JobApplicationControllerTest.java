package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.api.dto.JobApplicationResponse;
import com.joblessops.joblessops.jobapplication.api.dto.ApplicationStatusHistoryResponse;
import com.joblessops.joblessops.jobapplication.application.JobApplicationService;
import com.joblessops.joblessops.jobapplication.exception.JobApplicationNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobApplicationController.class)
class JobApplicationControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean JobApplicationService service;

    @Test
    void createsApplication() throws Exception {
        var id = UUID.randomUUID();
        var postingId = UUID.randomUUID();
        var date = LocalDate.of(2026, 11, 17);
        when(service.create(any())).thenReturn(new JobApplicationResponse(id, postingId, "Notes", date, "APPLIED"));
        mvc.perform(post("/api/job-applications").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"jobPostingId":"%s","notes":"Notes","appliedAt":"2026-11-17"}
                        """.formatted(postingId)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.statusCode").value("APPLIED"))
                .andExpect(jsonPath("$.jobPostingId").value(postingId.toString()));
    }

    @Test
    void rejectsMissingApplicationFields() throws Exception {
        mvc.perform(post("/api/job-applications").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void missingApplicationReturnsProblemDetail() throws Exception {
        var id = UUID.randomUUID();
        when(service.get(id)).thenThrow(new JobApplicationNotFoundException("Application missing"));
        mvc.perform(get("/api/job-applications/{id}", id)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Application missing"));
    }

    @Test
    void appendsStatusChange() throws Exception {
        var id = UUID.randomUUID();
        var time = LocalDate.of(2026, 11, 17).atTime(14, 30);
        when(service.changeStatus(eq(id), any())).thenReturn(new ApplicationStatusHistoryResponse("OFFER", time));
        mvc.perform(post("/api/job-applications/{id}/status-history", id).contentType(MediaType.APPLICATION_JSON)
                .content("{\"statusCode\":\"OFFER\",\"changedAt\":\"2026-11-17T14:30:00\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.statusCode").value("OFFER"));
        verify(service).changeStatus(eq(id), argThat(change -> change.statusCode().equals("OFFER") && change.changedAt().equals(time)));
    }
}
