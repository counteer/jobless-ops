package com.joblessops.joblessops.jobapplication.application;

import com.joblessops.joblessops.jobapplication.api.dto.ApplicationStatusResponse;
import com.joblessops.joblessops.jobapplication.exception.UnknownApplicationStatusException;
import com.joblessops.joblessops.jobapplication.persistence.ApplicationStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ApplicationStatusService {
    private final ApplicationStatusRepository repository;

    public ApplicationStatusService(ApplicationStatusRepository repository) { this.repository = repository; }

    public List<ApplicationStatusResponse> findAll() {
        return repository.findAll().stream().map(ApplicationStatusResponse::from)
                .sorted(Comparator.comparing(ApplicationStatusResponse::code)).toList();
    }

    public ApplicationStatusResponse find(String code) {
        return ApplicationStatusResponse.from(repository.findByCode(code).orElseThrow(() ->
                new UnknownApplicationStatusException("Unknown application status: " + code)));
    }
}
