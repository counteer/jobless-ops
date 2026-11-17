package com.joblessops.joblessops.jobapplication.persistence;

import com.joblessops.joblessops.jobapplication.domain.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, UUID> {
    List<ApplicationStatusHistory> findByJobApplicationIdOrderByChangedAtAsc(UUID jobApplicationId);
}
