CREATE TABLE application_status
(
    id           UUID PRIMARY KEY,
    code         VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255) NOT NULL,
    active       BOOLEAN      NOT NULL
);

INSERT INTO application_status (id, code, display_name, active)
VALUES (gen_random_uuid(), 'APPLIED', 'Applied', TRUE),
       (gen_random_uuid(), 'HR_SCREENING', 'HR Screening', TRUE),
       (gen_random_uuid(), 'TECHNICAL_INTERVIEW', 'Technical Interview', TRUE),
       (gen_random_uuid(), 'OFFER', 'Offer', TRUE),
       (gen_random_uuid(), 'REJECTED', 'Rejected', TRUE),
       (gen_random_uuid(), 'WITHDRAWN', 'Withdrawn', TRUE);