package com.joblessops.joblessops.company.application;

import com.joblessops.joblessops.company.api.CompanyLookup;
import com.joblessops.joblessops.company.api.CompanyPersist;
import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;
import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.company.persistence.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService implements CompanyLookup, CompanyPersist {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public CompanyResponse save(CreateCompanyRequest company) {
        Company persisted = companyRepository.save(new Company(company.name(), company.website()));
        return new CompanyResponse(persisted.getId(), persisted.getName(), persisted.getWebsite());
    }

    public Company get(UUID id) {
        return companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found with id " + id));
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public void ensureExists(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new CompanyNotFoundException("Company not found with id " + id);
        }
    }
}
