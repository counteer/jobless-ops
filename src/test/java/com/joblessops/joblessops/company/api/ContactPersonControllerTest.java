package com.joblessops.joblessops.company.api;

import com.joblessops.joblessops.company.api.dto.CreateContactPersonRequest;
import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.company.application.ContactPersonService;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.company.domain.ContactPerson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactPersonController.class)
class ContactPersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactPersonService contactPersonService;

    @Test
    public void getAllContactPersons() throws Exception {
        UUID companyId = UUID.randomUUID();
        when(contactPersonService.findByCompanyId(companyId)).thenReturn(List.of(new ContactPerson("John Doe", "jd@jon.do", "PHONE", "RECRUITER", new Company())));
        mockMvc.perform(get("/api/companies/{id}/contacts", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("jd@jon.do"))
                .andExpect(jsonPath("$[0].title").value("RECRUITER"))
                .andExpect(jsonPath("$[0].phone").value("PHONE"))
                .andExpect(jsonPath("$.length()").value(1));
        verify(contactPersonService).findByCompanyId(companyId);
    }

    @Test
    public void getAllContactPersonsWithNoCompany() throws Exception {
        UUID companyId = UUID.randomUUID();
        when(contactPersonService.findByCompanyId(companyId)).thenThrow(new CompanyNotFoundException("Company not found with id " + companyId));
        mockMvc.perform(get("/api/companies/{id}/contacts", companyId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Company not found with id " + companyId));
        verify(contactPersonService).findByCompanyId(companyId);
    }

    @Test
    public void saveContactPersonWithFaultyData() throws Exception {
        UUID companyId = UUID.randomUUID();
        mockMvc.perform(post("/api/companies/{id}/contacts", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "email": "jd@jon.docom",
                                "title": "RECRUITER",
                                "phone": "PHONE"
                                }
                                """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(contactPersonService);
    }

    @Test
    public void saveContactPersonWithProperData() throws Exception {
        UUID companyId = UUID.randomUUID();
        CreateContactPersonRequest contactPersonDto = new CreateContactPersonRequest("John Doe", "jd@jon.docom", "PHONE", "RECRUITER");
        when(contactPersonService.save(companyId, contactPersonDto))
                .thenReturn(
                        new ContactPerson("John Doe", "jd@jon.docom", "PHONE", "RECRUITER", new Company())
                );
        mockMvc.perform(post("/api/companies/{id}/contacts", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "John Doe",
                                "email": "jd@jon.docom",
                                "title": "RECRUITER",
                                "phone": "PHONE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("jd@jon.docom"))
                .andExpect(jsonPath("$.title").value("RECRUITER"))
                .andExpect(jsonPath("$.phone").value("PHONE"));
    }
}