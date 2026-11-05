package com.joblessops.joblessops.importing.api;

import java.util.List;

public record ImportRequest(
        List<ImportCompanyRequest> companies
) {}