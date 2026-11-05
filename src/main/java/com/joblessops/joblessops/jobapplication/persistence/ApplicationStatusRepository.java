package com.joblessops.joblessops.jobapplication.persistence;

import com.joblessops.joblessops.jobapplication.domain.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "application-statuses")
interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus,UUID> {
}
