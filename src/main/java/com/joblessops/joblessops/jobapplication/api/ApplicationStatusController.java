package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.api.dto.ApplicationStatusResponse;
import com.joblessops.joblessops.jobapplication.application.ApplicationStatusService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/application-statuses")
public class ApplicationStatusController {
    private final ApplicationStatusService service;

    public ApplicationStatusController(ApplicationStatusService service) { this.service = service; }

    @GetMapping
    public List<ApplicationStatusResponse> list() { return service.findAll(); }

    @GetMapping("/{code}")
    public ApplicationStatusResponse get(@PathVariable String code) { return service.find(code); }
}
