CREATE TABLE job_posting (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255),
    description TEXT
);