package com.joblessops.joblessops.company.api;

import java.util.UUID;
import java.util.Optional;
import com.joblessops.joblessops.company.api.dto.CompanyResponse;

public interface CompanyLookup {
    void ensureExists(UUID id);

    Optional<CompanyResponse> findByName(String name);
}
