package com.joblessops.joblessops.company.application;

import com.joblessops.joblessops.company.api.exceptions.CompanyNotFoundException;
import com.joblessops.joblessops.company.domain.Company;
import com.joblessops.joblessops.company.persistence.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Company get(UUID id) {
        return companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found with id " + id));
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public void ensureExists(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new CompanyNotFoundException("Company not found with id " + id);
        }
    }
}
