-- ============================================================
-- V5: Seed contacts and extra notes for all applications
-- ============================================================

-- ============================================================
-- CONTACTS
-- ============================================================

-- App 1: Endava (TECH_INTERVIEW)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(1, 'Irina Popescu',   'Technical Recruiter',  'i.popescu@endava.com',      '+373 69 111 001', 'https://linkedin.com/in/irina-popescu'),
(1, 'Andrei Moraru',   'Tech Lead',             'a.moraru@endava.com',       NULL,              'https://linkedin.com/in/andrei-moraru-endava');

-- App 2: Pentalog (HR_CALL)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(2, 'Sophie Leclerc',  'HR Manager',            'sophie.leclerc@pentalog.com', '+33 6 12 34 56 78', 'https://linkedin.com/in/sophie-leclerc-pentalog');

-- App 3: Intellias (APPLIED)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(3, 'Olena Kovalenko', 'Talent Acquisition',   'o.kovalenko@intellias.com', NULL,              'https://linkedin.com/in/olena-kovalenko');

-- App 4: Booking.com (OFFER)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(4, 'Emma van Dijk',   'HR Business Partner',  'e.vandijk@booking.com',     '+31 6 98 76 54 32', 'https://linkedin.com/in/emma-van-dijk'),
(4, 'Lars Henriksen',  'Engineering Manager',  'l.henriksen@booking.com',   NULL,              'https://linkedin.com/in/lars-henriksen-booking'),
(4, 'Priya Sharma',    'Senior Engineer',       'p.sharma@booking.com',      NULL,              NULL);

-- App 5: Playtika (REJECTED)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(5, 'Dana Goldberg',   'Recruiter',             'dana.goldberg@playtika.com','+972 52 111 2233', 'https://linkedin.com/in/dana-goldberg-playtika');

-- App 6: Porsche Digital (FINAL_INTERVIEW)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(6, 'Klaus Fischer',   'HR Partner',            'k.fischer@porsche.de',      '+49 711 911 0001', 'https://linkedin.com/in/klaus-fischer-porsche'),
(6, 'Mia Bauer',       'Engineering Manager',   'm.bauer@porsche.de',        NULL,              'https://linkedin.com/in/mia-bauer-porsche');

-- App 7: Orange Moldova (WITHDRAWN)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(7, 'Natalia Rusu',    'HR Specialist',         'n.rusu@orange.md',          '+373 22 500 700', NULL);

-- App 8: Stefanini (APPLIED)
INSERT INTO contacts (application_id, name, role, email, phone, linkedin_url) VALUES
(8, 'Carlos Mendes',   'Technical Recruiter',   'c.mendes@stefanini.com',    NULL,              'https://linkedin.com/in/carlos-mendes-stefanini');

-- ============================================================
-- NOTES (additional, for apps that had none)
-- ============================================================

INSERT INTO application_notes (application_id, content) VALUES
-- App 3: Intellias
(3, 'Position is fully remote. Stack: Kotlin + Spring Boot + AWS. Team is distributed across Ukraine and Poland.'),
(3, 'Sent CV via LinkedIn on ' || (CURRENT_DATE - INTERVAL '3 days')::text || '. No response yet — follow up in 5 days.'),

-- App 5: Playtika
(5, 'Rejection reason: need someone with 5+ years gaming experience. Worth re-applying in 1-2 years.'),

-- App 6: Porsche Digital
(6, 'Salary range confirmed: 5000–7000 EUR gross. Relocation allowance: 3000 EUR one-time. Stuttgart office.'),

-- App 7: Orange Moldova
(7, 'Withdrew after salary negotiation failed. Max offer was 2000 EUR, expectation was 2500+.'),

-- App 8: Stefanini
(8, 'Onsite in Chisinau. Brazilian company, international projects. Tech stack: Java 17, Spring Boot, Oracle DB.'),
(8, 'Applied via HeadHunter. Waiting for initial screening call.');
