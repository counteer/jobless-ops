package com.joblessops.joblessops.company;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class CompanyIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17");

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateCompany() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Google",
                                  "website": "https://google.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Google"))
                .andExpect(jsonPath("$.website").value("https://google.com"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = new ObjectMapper().readTree(responseBody);
        String companyId = json.get("id").asText();
        mockMvc.perform(get("/api/companies/{id}", companyId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Google"))
                .andExpect(jsonPath("$.website").value("https://google.com"))
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    void shouldCreateCompanyWithContactPerson() throws Exception {
        MvcResult companyResult = mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Google",
                                  "website": "https://google.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String responseBody = companyResult.getResponse().getContentAsString();
        JsonNode json = new ObjectMapper().readTree(responseBody);
        String companyId = json.get("id").asText();
        mockMvc.perform(post("/api/companies/{companyId}/contacts", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Recruiter One",
                                  "email": "recru@it.er",
                                  "phone": "PHONE",
                                  "title": "RECRUITER"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Recruiter One"))
                .andExpect(jsonPath("$.email").value("recru@it.er"))
                .andExpect(jsonPath("$.phone").value("PHONE"))
                .andExpect(jsonPath("$.title").value("RECRUITER"))
                .andExpect(jsonPath("$.companyId").value(companyId));

        mockMvc.perform(get("/api/companies/{companyId}/contacts", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Recruiter One"))
                .andExpect(jsonPath("$[0].email").value("recru@it.er"))
                .andExpect(jsonPath("$[0].phone").value("PHONE"))
                .andExpect(jsonPath("$[0].title").value("RECRUITER"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$.length()").value(1));
    }
}