CREATE TABLE application_status_history
(
    id                 UUID PRIMARY KEY,
    job_application_id UUID         NOT NULL,
    status_code        VARCHAR(255) NOT NULL,
    changed_at         TIMESTAMP    NOT NULL,

    CONSTRAINT fk_job_application_application_history_status
        FOREIGN KEY (job_application_id)
        REFERENCES job_application (id),

    CONSTRAINT fk_job_application_application_status
        FOREIGN KEY (status_code)
        REFERENCES application_status (code)
)