-- Clear tables to start clean
-- Note: Replace the database name in USE command if needed, or simply run the script directly in your database console.
DELETE FROM refresh_tokens;
DELETE FROM appointments;
DELETE FROM doctors;
DELETE FROM patients;
DELETE FROM users;

-- 1. Insert Admin (Password: Admin123)
INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (1, 'System Admin', 'admin@careflow.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'ADMIN', 1, NOW());

-- 2. Insert Doctors (Password: Admin123)
INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (2, 'Dr. Sarah Jenkins', 'sarah.jenkins@careflow.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'DOCTOR', 1, NOW());
INSERT INTO doctors (id, user_id, specialty, qualification, experience_years, is_active)
VALUES (1, 2, 'Cardiologist', 'MD, FACC', 12, 1);

INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (3, 'Dr. Robert Chen', 'robert.chen@careflow.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'DOCTOR', 1, NOW());
INSERT INTO doctors (id, user_id, specialty, qualification, experience_years, is_active)
VALUES (2, 3, 'Neurologist', 'MD, PhD', 15, 1);

INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (4, 'Dr. Emily Watson', 'emily.watson@careflow.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'DOCTOR', 1, NOW());
INSERT INTO doctors (id, user_id, specialty, qualification, experience_years, is_active)
VALUES (3, 4, 'Pediatrician', 'MD, FAAP', 8, 1);

INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (5, 'Dr. Lisa Ray', 'lisa.ray@careflow.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'DOCTOR', 1, NOW());
INSERT INTO doctors (id, user_id, specialty, qualification, experience_years, is_active)
VALUES (4, 5, 'Dermatologist', 'MD', 6, 1);

-- 3. Insert Patients (Password: Admin123)
INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (6, 'John Doe', 'john.doe@gmail.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'PATIENT', 1, NOW());
INSERT INTO patients (id, user_id, age, blood_group, gender, phone, is_active)
VALUES (1, 6, 34, 'O+', 'Male', '+15551234567', 1);

INSERT INTO users (id, name, email, password, role, is_active, created_at)
VALUES (7, 'Jane Smith', 'jane.smith@gmail.com', '$2a$10$8.Je5/e.x1Vd3835Y5x9FeS0kP2.Y.7y8b/7w1r.gB.9Vw6u.J6/q', 'PATIENT', 1, NOW());
INSERT INTO patients (id, user_id, age, blood_group, gender, phone, is_active)
VALUES (2, 7, 28, 'A-', 'Female', '+15559876543', 1);
