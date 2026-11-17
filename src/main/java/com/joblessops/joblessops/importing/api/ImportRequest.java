package com.joblessops.joblessops.importing.api;

import jakarta.validation.Valid;
import java.util.List;

public record ImportRequest(
        @Valid List<ImportCompanyRequest> companies
) {}