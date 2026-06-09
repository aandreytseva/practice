-- ============================================================
-- V4: Contacts (recruiters / interviewers) per application
-- ============================================================

CREATE TABLE contacts (
    id             BIGSERIAL PRIMARY KEY,
    application_id BIGINT       NOT NULL REFERENCES job_applications(id) ON DELETE CASCADE,
    name           VARCHAR(255) NOT NULL,
    role           VARCHAR(255),
    email          VARCHAR(255),
    phone          VARCHAR(50),
    linkedin_url   TEXT,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_contacts_application_id ON contacts(application_id);
