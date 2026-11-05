package com.joblessops.joblessops.company.api;

import java.util.UUID;

public interface CompanyLookup {
    void ensureExists(UUID id);
}
