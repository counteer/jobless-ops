package com.joblessops.joblessops.company.api;

import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.company.application.CompanyService;
import com.joblessops.joblessops.company.domain.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @Test
    void shouldGetCompany() throws Exception {
        UUID id = UUID.randomUUID();
        Company company = new Company("Google", "https://google.com");

        when(companyService.get(id)).thenReturn(company);

        mockMvc.perform(get("/api/companies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Google"))
                .andExpect(jsonPath("$.website").value("https://google.com"));

        verify(companyService).get(id);
    }

    @Test
    void shouldGetAllCompanies() throws Exception {
        List<Company> companies = List.of(new Company("Google", "https://google.com"),
                new Company("Amazon", "https://amazon.com"));

        when(companyService.getAll()).thenReturn(companies);

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Amazon", "Google")))
                .andExpect(jsonPath("$[*].website", containsInAnyOrder("https://amazon.com", "https://google.com")))
                .andExpect(jsonPath("$.length()").value(2));

        verify(companyService).getAll();
    }

    @Test
    void shouldGive404ForGetCompanyWhenCompanyNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        when(companyService.get(id)).thenThrow(new CompanyNotFoundException("Company not found with id " + id));

        mockMvc.perform(get("/api/companies/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Company not found with id " + id));
        verify(companyService).get(id);
    }

    @Test
    void shouldSaveCompany() throws Exception {
        UUID id = UUID.randomUUID();
        CompanyResponse company = new CompanyResponse(id, "Google", "https://google.com");
        when(companyService.save(any())).thenReturn(company);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Google",
                          "website": "https://google.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Google"))
                .andExpect(jsonPath("$.website").value("https://google.com"));

        verify(companyService).save(any());
    }

    @Test
    void shouldNotSaveCompanyWithBadName() throws Exception {

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "",
                          "website": "https://google.com"
                        }
                        """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(companyService);
    }
}