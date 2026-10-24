package com.joblessops.joblessops.company.persistence;

import com.joblessops.joblessops.company.domain.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, UUID> {

    List<ContactPerson> findByCompanyId(UUID companyId);
}
