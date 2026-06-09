-- ============================================================
-- V2: Seed data for development
-- ============================================================

-- Insert sample job applications for user with id=1
INSERT INTO job_applications (user_id, company_name, position, job_url, salary_min, salary_max, currency, source, status, applied_at)
VALUES
    (1, 'Endava', 'Backend Developer', 'https://www.endava.com/careers/1', 2500, 3500, 'EUR', 'COMPANY_WEBSITE', 'TECH_INTERVIEW', CURRENT_DATE - INTERVAL '10 days'),
    (1, 'Pentalog', 'Kotlin Developer', 'https://www.pentalog.com/jobs/1', 2000, 3000, 'EUR', 'LINKEDIN', 'HR_CALL', CURRENT_DATE - INTERVAL '7 days'),
    (1, 'Intellias', 'Software Engineer', 'https://intellias.com/jobs/1', 2800, 4000, 'EUR', 'LINKEDIN', 'APPLIED', CURRENT_DATE - INTERVAL '3 days'),
    (1, 'Booking.com', 'Java/Kotlin Developer', 'https://jobs.booking.com/1', 4500, 6000, 'EUR', 'COMPANY_WEBSITE', 'OFFER', CURRENT_DATE - INTERVAL '20 days'),
    (1, 'Playtika', 'Backend Engineer', 'https://www.playtika.com/careers/1', 3000, 4500, 'EUR', 'REFERRAL', 'REJECTED', CURRENT_DATE - INTERVAL '15 days'),
    (1, 'Porsche Digital', 'Senior Backend Developer', 'https://jobs.porsche.com/1', 5000, 7000, 'EUR', 'LINKEDIN', 'FINAL_INTERVIEW', CURRENT_DATE - INTERVAL '5 days'),
    (1, 'Orange Moldova', 'Platform Engineer', 'https://orange.md/careers/1', 1500, 2200, 'EUR', 'COMPANY_WEBSITE', 'WITHDRAWN', CURRENT_DATE - INTERVAL '25 days'),
    (1, 'Stefanini', 'Spring Boot Developer', 'https://stefanini.com/jobs/1', 2200, 3200, 'EUR', 'HH_RU', 'APPLIED', CURRENT_DATE - INTERVAL '1 day');

-- Status history
INSERT INTO status_history (application_id, from_status, to_status, comment, changed_at)
VALUES
    -- Endava: APPLIED -> HR_CALL -> TECH_INTERVIEW
    (1, NULL, 'APPLIED', 'Applied via company website', NOW() - INTERVAL '10 days'),
    (1, 'APPLIED', 'HR_CALL', 'Recruiter reached out on LinkedIn', NOW() - INTERVAL '8 days'),
    (1, 'HR_CALL', 'TECH_INTERVIEW', 'HR screen passed, technical round scheduled', NOW() - INTERVAL '5 days'),

    -- Pentalog: APPLIED -> HR_CALL
    (2, NULL, 'APPLIED', NULL, NOW() - INTERVAL '7 days'),
    (2, 'APPLIED', 'HR_CALL', 'Recruiter contacted me', NOW() - INTERVAL '5 days'),

    -- Intellias: APPLIED
    (3, NULL, 'APPLIED', NULL, NOW() - INTERVAL '3 days'),

    -- Booking.com: APPLIED -> HR_CALL -> TECH_INTERVIEW -> FINAL_INTERVIEW -> OFFER
    (4, NULL, 'APPLIED', NULL, NOW() - INTERVAL '20 days'),
    (4, 'APPLIED', 'HR_CALL', NULL, NOW() - INTERVAL '18 days'),
    (4, 'HR_CALL', 'TECH_INTERVIEW', 'Passed HR screen', NOW() - INTERVAL '15 days'),
    (4, 'TECH_INTERVIEW', 'FINAL_INTERVIEW', 'Great technical interview', NOW() - INTERVAL '10 days'),
    (4, 'FINAL_INTERVIEW', 'OFFER', 'Got an offer! 5000 EUR/month', NOW() - INTERVAL '5 days'),

    -- Playtika: APPLIED -> HR_CALL -> REJECTED
    (5, NULL, 'APPLIED', NULL, NOW() - INTERVAL '15 days'),
    (5, 'APPLIED', 'HR_CALL', NULL, NOW() - INTERVAL '13 days'),
    (5, 'HR_CALL', 'REJECTED', 'Looking for someone with more gaming industry experience', NOW() - INTERVAL '10 days'),

    -- Porsche Digital: APPLIED -> HR_CALL -> TECH_INTERVIEW -> FINAL_INTERVIEW
    (6, NULL, 'APPLIED', 'Referred by a colleague', NOW() - INTERVAL '5 days'),
    (6, 'APPLIED', 'HR_CALL', NULL, NOW() - INTERVAL '4 days'),
    (6, 'HR_CALL', 'TECH_INTERVIEW', NULL, NOW() - INTERVAL '3 days'),
    (6, 'TECH_INTERVIEW', 'FINAL_INTERVIEW', 'System design round went well', NOW() - INTERVAL '1 day'),

    -- Orange Moldova: APPLIED -> WITHDRAWN
    (7, NULL, 'APPLIED', NULL, NOW() - INTERVAL '25 days'),
    (7, 'APPLIED', 'WITHDRAWN', 'Salary below expectations', NOW() - INTERVAL '20 days'),

    -- Stefanini: APPLIED
    (8, NULL, 'APPLIED', NULL, NOW() - INTERVAL '1 day');

-- Notes
INSERT INTO application_notes (application_id, content)
VALUES
    (1, 'Interview scheduled for next Tuesday at 15:00 EET. Topics: distributed systems, Kotlin coroutines, system design.'),
    (1, 'Endava Chisinau office — hybrid work, 3 days on-site. Good work-life balance reviews on Glassdoor.'),
    (4, 'Offer details: 5000 EUR base, 15% annual bonus, relocation package to Amsterdam included.'),
    (4, 'Deadline to accept Booking.com offer: June 15. Need to compare with Porsche Digital final interview outcome.'),
    (6, 'Final interview with engineering manager and two senior devs. Prepare questions about team culture.'),
    (2, 'Pentalog works with French clients. Good chance to improve French language skills.');

-- Reminders
INSERT INTO reminders (application_id, title, remind_at, is_done)
VALUES
    (1, 'Tech interview at Endava', NOW() + INTERVAL '2 days', false),
    (4, 'Accept or decline Booking.com offer', NOW() + INTERVAL '5 days', false),
    (6, 'Final interview at Porsche Digital', NOW() + INTERVAL '1 day', false),
    (2, 'Submit Pentalog technical assessment', NOW() + INTERVAL '3 days', false),
    (3, 'Follow up with Intellias recruiter', NOW() + INTERVAL '4 days', false);
