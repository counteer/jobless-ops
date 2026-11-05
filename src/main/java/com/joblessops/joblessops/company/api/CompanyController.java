package com.joblessops.joblessops.company.api;

import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;
import com.joblessops.joblessops.company.application.CompanyService;
import com.joblessops.joblessops.company.domain.Company;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse save(@RequestBody @Valid CreateCompanyRequest company) {
        return companyService.save(company);
    }

    @GetMapping
    @RequestMapping(value = "/{companyId}")
    public CompanyResponse get(@PathVariable UUID companyId) {
        return convert(companyService.get(companyId));
    }

    @GetMapping
    public List<CompanyResponse> getAll() {
        return companyService.getAll().stream().map(this::convert).toList();
    }

    private CompanyResponse convert(Company company) {
        return new CompanyResponse(company.getId(), company.getName(), company.getWebsite());
    }
}
