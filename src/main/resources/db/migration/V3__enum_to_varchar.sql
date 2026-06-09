-- ============================================================
-- V3: Convert PostgreSQL native enum columns to VARCHAR
-- so that Hibernate @Enumerated(EnumType.STRING) works correctly
-- ============================================================

ALTER TABLE job_applications
    ALTER COLUMN status  TYPE VARCHAR(50) USING status::text,
    ALTER COLUMN source  TYPE VARCHAR(50) USING source::text;

ALTER TABLE status_history
    ALTER COLUMN from_status TYPE VARCHAR(50) USING from_status::text,
    ALTER COLUMN to_status   TYPE VARCHAR(50) USING to_status::text;

DROP TYPE IF EXISTS application_status;
DROP TYPE IF EXISTS application_source;
