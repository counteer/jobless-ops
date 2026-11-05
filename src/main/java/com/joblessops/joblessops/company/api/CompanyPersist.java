package com.joblessops.joblessops.company.api;

import com.joblessops.joblessops.company.api.dto.CompanyResponse;
import com.joblessops.joblessops.company.api.dto.CreateCompanyRequest;

public interface CompanyPersist {

    CompanyResponse save(CreateCompanyRequest company);
}
