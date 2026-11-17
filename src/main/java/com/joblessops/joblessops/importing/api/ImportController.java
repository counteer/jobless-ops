package com.joblessops.joblessops.importing.api;

import jakarta.validation.Valid;

import com.joblessops.joblessops.importing.application.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imports")
public class ImportController {

    @Autowired
    final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping
    public void importCompanies(@Valid @RequestBody ImportRequest request){
        System.out.println(request);
        System.out.println(request.companies().size());
        importService.importCompanies(request);

    }


}
