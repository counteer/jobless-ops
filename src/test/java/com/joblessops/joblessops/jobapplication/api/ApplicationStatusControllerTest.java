package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.application.ApplicationStatusService;
import com.joblessops.joblessops.jobapplication.api.dto.ApplicationStatusResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationStatusController.class)
class ApplicationStatusControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean ApplicationStatusService service;

    @Test
    void listsStatusCatalogue() throws Exception {
        when(service.findAll()).thenReturn(List.of(new ApplicationStatusResponse(UUID.randomUUID(), "APPLIED", "Applied", true)));
        mvc.perform(get("/api/application-statuses")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("APPLIED"))
                .andExpect(jsonPath("$[0].active").value(true));
    }
}
