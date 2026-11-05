CREATE TABLE job_application
(
    id             UUID PRIMARY KEY,
    job_posting_id UUID NOT NULL,
    notes          TEXT,
    applied_at     DATE NOT NULL,
    CONSTRAINT fk_job_application_job_posting
        FOREIGN KEY (job_posting_id)
        REFERENCES job_posting (id)
)