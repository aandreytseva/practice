-- ============================================================
-- V1: Initial schema for Job Application Tracker
-- ============================================================

-- Users
CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

-- Application status enum
CREATE TYPE application_status AS ENUM (
    'APPLIED',
    'HR_CALL',
    'TECH_INTERVIEW',
    'FINAL_INTERVIEW',
    'OFFER',
    'REJECTED',
    'WITHDRAWN'
);

-- Application source enum
CREATE TYPE application_source AS ENUM (
    'HH_RU',
    'LINKEDIN',
    'HABR_CAREER',
    'COMPANY_WEBSITE',
    'REFERRAL',
    'OTHER'
);

-- Job Applications
CREATE TABLE job_applications
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT                  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    company_name VARCHAR(255)            NOT NULL,
    position     VARCHAR(255)            NOT NULL,
    job_url      TEXT,
    salary_min   INTEGER,
    salary_max   INTEGER,
    currency     VARCHAR(10)             DEFAULT 'RUB',
    source       application_source      NOT NULL DEFAULT 'OTHER',
    status       application_status      NOT NULL DEFAULT 'APPLIED',
    applied_at   DATE                    NOT NULL DEFAULT CURRENT_DATE,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_job_applications_user_id ON job_applications (user_id);
CREATE INDEX idx_job_applications_status  ON job_applications (status);

-- Status history (state machine audit log)
CREATE TABLE status_history
(
    id             BIGSERIAL PRIMARY KEY,
    application_id BIGINT             NOT NULL REFERENCES job_applications (id) ON DELETE CASCADE,
    from_status    application_status,
    to_status      application_status NOT NULL,
    comment        TEXT,
    changed_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_status_history_application_id ON status_history (application_id);

-- Notes
CREATE TABLE application_notes
(
    id             BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL REFERENCES job_applications (id) ON DELETE CASCADE,
    content        TEXT   NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_application_notes_application_id ON application_notes (application_id);

-- Reminders
CREATE TABLE reminders
(
    id             BIGSERIAL PRIMARY KEY,
    application_id BIGINT       NOT NULL REFERENCES job_applications (id) ON DELETE CASCADE,
    title          VARCHAR(255) NOT NULL,
    remind_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    is_done        BOOLEAN                  DEFAULT FALSE NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_reminders_application_id ON reminders (application_id);
CREATE INDEX idx_reminders_remind_at      ON reminders (remind_at);
