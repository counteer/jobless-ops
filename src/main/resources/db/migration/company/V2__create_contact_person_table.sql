CREATE TABLE contact_person
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    phone      VARCHAR(255),
    email      VARCHAR(255),
    title       VARCHAR(255),
    company_id UUID         NOT NULL,

    CONSTRAINT fk_contact_person_company
        FOREIGN KEY (company_id)
        REFERENCES company (id)

);